package com.djmanong.mall.vo.search;

import com.djmanong.mall.to.es.EsProduct;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DjManong
 */
@Data
public class SearchResponse implements Serializable {

    /**品牌*/
    private SearchResponseAttrVo brand;

    /**分类*/
    private SearchResponseAttrVo catelog;

    /**所有商品的顶头显示的筛选属性*/
    private List<SearchResponseAttrVo> attrs = new ArrayList<>();

    /**检索出来的商品信息*/
    private List<EsProduct> products = new ArrayList<>();

    /**总记录数*/
    private Long total;

    /**每页显示的内容长度*/
    private Integer pageSize;

    /**当前页码*/
    private Integer pageNum;
}
