package com.djmanong.mall.pms.service;

import com.djmanong.mall.pms.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djmanong.mall.vo.product.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
public interface ProductCategoryService extends IService<ProductCategory> {

    /**
     * 查询这个菜单以及它的子菜单
     * @param i
     * @return
     */
    List<PmsProductCategoryWithChildrenItem> listCategoryWithChildrenItem(Integer i);
}
