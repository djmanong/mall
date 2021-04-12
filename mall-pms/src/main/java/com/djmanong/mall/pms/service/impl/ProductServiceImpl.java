package com.djmanong.mall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djmanong.mall.pms.entity.*;
import com.djmanong.mall.pms.mapper.*;
import com.djmanong.mall.pms.service.ProductService;
import com.djmanong.mall.vo.PageInfoVo;
import com.djmanong.mall.vo.product.PmsProductParam;
import com.djmanong.mall.vo.product.PmsProductQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
@DubboService
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    /**商品属性值mapper*/
    @Autowired
    private ProductAttributeValueMapper productAttributeValueMapper;

    @Autowired
    private ProductFullReductionMapper productFullReductionMapper;

    @Autowired
    private ProductLadderMapper productLadderMapper;

    @Autowired
    private SkuStockMapper skuStockMapper;

    /**当前线程共享的数据，保存商品id; 也可使用map: private Map<Thread, Long> map = new HashMap<>() 取值:map.get(Thread.getCurrentThread())*/
    private ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Override
    public PageInfoVo productPageInfo(PmsProductQueryParam param) {

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        if (param.getBrandId() != null) {
            wrapper.eq("brand_id", param.getBrandId());
        }
        if (!StringUtils.isEmpty(param.getKeyword())) {
            wrapper.like("name", param.getKeyword());
        }
        if (param.getProductCategoryId() != null) {
            wrapper.eq("product_category_id", param.getProductCategoryId());
        }
        if (!StringUtils.isEmpty(param.getProductSn())) {
            wrapper.like("product_sn", param.getProductSn());
        }
        if (param.getPublishStatus() != null) {
            wrapper.eq("publish_status", param.getPublishStatus());
        }
        if (param.getVerifyStatus() != null) {
            wrapper.eq("verify_status", param.getVerifyStatus());
        }

        IPage<Product> page = productMapper.selectPage(new Page<Product>(param.getPageNumber(), param.getPageSize()), wrapper);

        return new PageInfoVo(page.getTotal(), page.getPages(), param.getPageSize(), page.getRecords(), page.getCurrent());
    }

    /**
     * 大保存
     * 事务问题
     * 注意: 直接调用类内部的方法会导致给该方法上配置的注解型事务的失效，只有调用者自身的事务生效
     * 原因：service自己调用自己的方法，没有经过动态代理，被调用方法的事务会失效
     * 解决方式：对象.方法()调用
     * @param productParam
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveProduct(PmsProductParam productParam) {

        ProductServiceImpl proxy = (ProductServiceImpl) AopContext.currentProxy();

        // 1. 保存商品基本信息 pms_product
        proxy.saveBaseInfo(productParam);

        // 5. 库存表 pms_sku_stock
        proxy.saveSkuStock(productParam);

        // 一下操作都可以进行try-catch操作
        // 2. 保存这个商品对应的所有属性的值 pms_product_attribute_value
        proxy.saveProductAttributeValue(productParam);

        // 3. 商品满减信息 pms_product_full_reduction
        proxy.saveProductFullReduction(productParam);

        // 4. 阶梯价格 pms_product_leader
        try {
            proxy.saveProductLeader(productParam);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }

        // 在多线程的情况下建议对threadLocal进行回收
        threadLocal.remove();
    }

    /**
     * 保存商品基本信息
     * @param productParam
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveBaseInfo(PmsProductParam productParam) {
        Product product = new Product();
        // 拷贝同名属性
        BeanUtils.copyProperties(productParam, product);
        productMapper.insert(product);
        threadLocal.set(product.getId());
        log.debug("新添加的商品id: {}", product.getId());
        log.debug("当前线程: {} ===> id: {}", Thread.currentThread().getName(), Thread.currentThread().getId());
    }

    /**
     * 保存库存信息
     * @param productParam
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveSkuStock(PmsProductParam productParam) {
        List<SkuStock> skuStockList = productParam.getSkuStockList();
        for (int i = 1; i <= skuStockList.size(); i++) {
            SkuStock skuStock = skuStockList.get(i - 1);
            if (StringUtils.isEmpty(skuStock.getSkuCode())) {
                // skuCode必须有
                // 生成规则 商品id_sku自增id
                skuStock.setSkuCode(threadLocal.get() + "_" + i);
            }
            skuStock.setProductId(threadLocal.get());
            skuStockMapper.insert(skuStock);
        }
        log.debug("当前线程: {} ===> id: {}", Thread.currentThread().getName(), Thread.currentThread().getId());
    }

    /**
     * 保存商品满减信息
     * @param productParam
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {FileNotFoundException.class},
    noRollbackFor = {ArithmeticException.class, NullPointerException.class})
    public void saveProductLeader(PmsProductParam productParam) throws FileNotFoundException {
        List<ProductLadder> productLadderList = productParam.getProductLadderList();
        productLadderList.forEach((productLadder) -> {
            productLadder.setProductId(threadLocal.get());
            productLadderMapper.insert(productLadder);
        });
        log.debug("当前线程: {} ===> id: {}", Thread.currentThread().getName(), Thread.currentThread().getId());
        int i = 10 / 0;
        // File xxxxx = new File("xxxxx");
        // new FileInputStream(xxxxx);
    }

    /**
     * 保存商品属性值信息
     * @param productParam
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveProductAttributeValue(PmsProductParam productParam) {
        List<ProductAttributeValue> valueList = productParam.getProductAttributeValueList();
        valueList.forEach((item) -> {
            item.setProductId(threadLocal.get());
            productAttributeValueMapper.insert(item);
        });
        log.debug("当前线程: {} ===> id: {}", Thread.currentThread().getName(), Thread.currentThread().getId());
    }

    /**
     * 保存商品满减信息
     * @param productParam
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveProductFullReduction(PmsProductParam productParam) {
        List<ProductFullReduction> fullReductionList = productParam.getProductFullReductionList();
        fullReductionList.forEach((fullReduction) -> {
            fullReduction.setProductId(threadLocal.get());
            productFullReductionMapper.insert(fullReduction);
        });
        log.debug("当前线程: {} ===> id: {}", Thread.currentThread().getName(), Thread.currentThread().getId());
    }
}
