package com.djmanong.mall.vo.product;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询单个产品进行修改时返回的结果
 * @author DjManong
 */
@Data
public class PmsProductResult extends PmsProductParam implements Serializable {
    /**商品所选分类的父id*/
    private Long cateParentId;

}
