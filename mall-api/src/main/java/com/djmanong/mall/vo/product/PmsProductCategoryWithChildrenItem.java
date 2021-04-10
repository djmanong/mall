package com.djmanong.mall.vo.product;

import com.djmanong.mall.pms.entity.ProductCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 子菜单
 * @author DjManong
 */
@Data
public class PmsProductCategoryWithChildrenItem extends ProductCategory implements Serializable {
    private List<ProductCategory> children;

}
