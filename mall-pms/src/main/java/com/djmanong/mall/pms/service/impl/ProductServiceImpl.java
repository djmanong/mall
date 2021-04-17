package com.djmanong.mall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djmanong.mall.constant.EsConstant;
import com.djmanong.mall.pms.entity.*;
import com.djmanong.mall.pms.mapper.*;
import com.djmanong.mall.pms.service.ProductService;
import com.djmanong.mall.to.es.EsProduct;
import com.djmanong.mall.to.es.EsProductAttributeValue;
import com.djmanong.mall.to.es.EsSkuProductInfo;
import com.djmanong.mall.vo.PageInfoVo;
import com.djmanong.mall.vo.product.PmsProductParam;
import com.djmanong.mall.vo.product.PmsProductQueryParam;
import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
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
import java.util.ArrayList;
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

    @Autowired
    JestClient jestClient;

    /**当前线程共享的数据，保存商品id; 也可使用map: private Map<Thread, Long> map = new HashMap<>() 取值:map.get(Thread.getCurrentThread())*/
    private ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Override
    public Product productInfo(Long id) {
        return productMapper.selectById(id);
    }

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

    @Override
    public void updatePublishStatus(List<Long> ids, Integer publishStatus) {

        if (publishStatus == 0) {
            // 下架
            // 改数据库状态，删除es信息
            ids.forEach(id -> {
                setProductPublishStatus(publishStatus, id);
                deleteProductFromEs(id);
            });
        } else {
            // 上架
            // 改数据库状态，添加es信息
            // 1. 对于数据库是修改商品的状态位
            ids.forEach((id) -> {
                Product productInfo = setProductPublishStatus(publishStatus, id);
                saveProductToEs(id, productInfo);
            });
        }

    }

    private void deleteProductFromEs(Long id) {
        Delete delete = new Delete.Builder(id.toString())
                .index(EsConstant.PRODUCT_ES_INDEX)
                .type(EsConstant.PRODUCT_INFO_ES_TYPE)
                .build();
        try {
            DocumentResult execute = jestClient.execute(delete);
            if (execute.isSucceeded()) {
                log.info("ES中id为{}的商品下架成功", id);
            } else {
                log.error("ES中id为{}的商品下架失败", id);
                deleteProductFromEs(id);
            }
        } catch (Exception e) {
            deleteProductFromEs(id);
            log.error("ES中id为{}的商品下架异常: {}", id, e.getMessage());
        }
    }

    /**
     * dubbo远程调用插入数据服务，可能会经常超时，dubbo会默认重试，导致方法会被调用多次，可能会导致数据库同样的数据有多个
     * 给数据库做数据的，最好使用dubbo的快速失败模式。手动重试
     * @param id
     * @param productInfo
     */
    public void saveProductToEs(Long id, Product productInfo) {
        // 2. 对于es是要保存商品信息, 还需要查出商品的sku，给es中保存
        // 复制sku信息:
        List<SkuStock> stocks = skuStockMapper.selectList(new QueryWrapper<SkuStock>().eq("product_id", id));

        EsProduct esProduct = new EsProduct();
        BeanUtils.copyProperties(productInfo, esProduct);
        List<EsSkuProductInfo> esSkuProductInfoList = new ArrayList<>(stocks.size());

        // 所有的sku信息拿出来
        // 查出所有销售属性名
        List<ProductAttribute> skuAttributeNames = productAttributeValueMapper.selectProductSaleAttributeName(id);
        stocks.forEach((skuStock) -> {
            EsSkuProductInfo esSkuProductInfo = new EsSkuProductInfo();
            BeanUtils.copyProperties(skuStock, esSkuProductInfo);

            // sku标题
            String subTitle = esProduct.getName();
            if (!StringUtils.isEmpty(skuStock.getSp1())) {
                subTitle += " " + skuStock.getSp1();
            }
            if (!StringUtils.isEmpty(skuStock.getSp2())) {
                subTitle += " " + skuStock.getSp2();
            }
            if (!StringUtils.isEmpty(skuStock.getSp3())) {
                subTitle += " " + skuStock.getSp3();
            }
            // sku特色标题
            esSkuProductInfo.setSkuTitle(subTitle);

            List<EsProductAttributeValue> skuAttributeValues = new ArrayList<>();

            for (int i = 0; i < skuAttributeNames.size(); i++) {
                EsProductAttributeValue value = new EsProductAttributeValue();
                value.setId(id);
                value.setName(skuAttributeNames.get(i).getName());
                value.setProductAttributeId(skuAttributeNames.get(i).getId());
                value.setType(skuAttributeNames.get(i).getType());

                // 设置值 颜色尺码等信息的值; 改掉查询商品的属性分类里面所有属性的时候，按照sort字段排序好
                // value.setValue();
                if (i == 0) {
                    value.setValue(skuStock.getSp1());
                }
                if (i == 1) {
                    value.setValue(skuStock.getSp2());
                }
                if (i == 2) {
                    value.setValue(skuStock.getSp3());
                }

                skuAttributeValues.add(value);
            }

            esSkuProductInfo.setAttributeValues(skuAttributeValues);
            esSkuProductInfoList.add(esSkuProductInfo);
        });

        // 这个sku所有销售属性对应的值 统计该sku中所有的值
        esProduct.setSkuProductInfos(esSkuProductInfoList);

        // 3. 复制spu公共属性信息
        List<EsProductAttributeValue> esProductAttributeValueList = productAttributeValueMapper.selectProductBaseAttributeAndValue(id);
        esProduct.setAttrValueList(esProductAttributeValueList);

        // 商品保存在es中
        try {
            Index build = new Index
                    .Builder(esProduct)
                    .index(EsConstant.PRODUCT_ES_INDEX)
                    .type(EsConstant.PRODUCT_INFO_ES_TYPE)
                    .id(id.toString())
                    .build();
            DocumentResult execute = jestClient.execute(build);
            boolean succeeded = execute.isSucceeded();
            if (succeeded) {
                log.info("ES中id为{}的商品上架成功!", id);
            } else {
                log.error("ES中id为{}的商品保存失败, 开始重试", id);
                saveProductToEs(id, productInfo);
            }
        } catch (Exception e) {
            log.error("ES中id为{}的商品数据保存异常: {}", id,e.getMessage());
            saveProductToEs(id, productInfo);
        }
    }

    public Product setProductPublishStatus(Integer publishStatus, Long id) {
        Product productInfo = productInfo(id);
        Product product = new Product();
        product.setId(id);
        product.setPublishStatus(publishStatus);
        // mybatis-plus自带的更新方式是: 哪个字段有值就修改哪个字段
        productMapper.updateById(product);
        productInfo.setPublishStatus(publishStatus);
        return productInfo;
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
