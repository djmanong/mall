package com.djmanong.mall.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djmanong.mall.pms.entity.ProductCategory;
import com.djmanong.mall.pms.mapper.ProductCategoryMapper;
import com.djmanong.mall.pms.service.ProductCategoryService;
import com.djmanong.mall.vo.product.PmsProductCategoryWithChildrenItem;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
@DubboService
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    ProductCategoryMapper categoryMapper;

    @Override
    public List<PmsProductCategoryWithChildrenItem> listCategoryWithChildrenItem(Integer i) {
        return categoryMapper.listCategoryWithChildrenItem(i);
    }
}
