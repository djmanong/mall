package com.djmanong.mall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djmanong.mall.pms.entity.Product;
import com.djmanong.mall.pms.mapper.ProductMapper;
import com.djmanong.mall.pms.service.ProductService;
import com.djmanong.mall.vo.PageInfoVo;
import com.djmanong.mall.vo.product.PmsProductQueryParam;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

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
}
