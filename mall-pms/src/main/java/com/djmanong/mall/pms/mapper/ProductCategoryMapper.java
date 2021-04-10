package com.djmanong.mall.pms.mapper;

import com.djmanong.mall.pms.entity.ProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djmanong.mall.vo.product.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * <p>
 * 产品分类 Mapper 接口
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

    /**
     * 查询菜单及其子菜单
     * @param i
     * @return
     */
    List<PmsProductCategoryWithChildrenItem> listCategoryWithChildrenItem(Integer i);
}
