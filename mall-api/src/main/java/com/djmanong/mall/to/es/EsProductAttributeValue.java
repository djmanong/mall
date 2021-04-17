package com.djmanong.mall.to.es;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品属性值
 * @author DjManong
 */
@Data
public class EsProductAttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    /**属性的id*/
    private Long productAttributeId;
    /**属性值 如：3G*/
    private String value;
    /**属性参数：0->规格；1->参数 规格：销售属性(sku)；参数：筛选参数，公共属性(spu)*/
    private Integer type;

    /**属性名*/
    private String name;

    /**商品的id*/
    private Long productId;
}
