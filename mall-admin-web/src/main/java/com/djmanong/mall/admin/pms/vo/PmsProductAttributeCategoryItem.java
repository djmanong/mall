package com.djmanong.mall.admin.pms.vo;

import com.djmanong.mall.pms.entity.ProductAttribute;
import com.djmanong.mall.pms.entity.ProductAttributeCategory;
import lombok.Data;

import java.util.List;

/**
 * 包含有分类下属性的vo
 * @author DjManong
 */
@Data
public class PmsProductAttributeCategoryItem extends ProductAttributeCategory {
    private List<ProductAttribute> productAttributeList;


}
