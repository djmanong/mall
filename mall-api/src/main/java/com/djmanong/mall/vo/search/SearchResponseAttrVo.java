package com.djmanong.mall.vo.search;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DjManong
 */
@Data
public class SearchResponseAttrVo implements Serializable {

    /**1*/
    private Long productAttributeId;

    /**当前属性值的所有值*/
    private List<String> value = new ArrayList<>();

    /**属性名称,例如: 网络制式，分类等*/
    private String name;
}
