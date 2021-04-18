package com.djmanong.mall.constant;

/**
 * Elasticsearch常量
 * @author DjManong
 */
public class EsConstant {
    /**索引*/
    public static final String PRODUCT_ES_INDEX = "product";
    /**类型*/
    public static final String PRODUCT_INFO_ES_TYPE = "info";

    /**默认排序*/
    public static final String ORDER_BY_DEFAULT = "0";
    /**销量排序*/
    public static final String ORDER_BY_SALE = "1";
    /**价格排序*/
    public static final String ORDER_BY_PRICE = "2";
    /**升降序*/
    public static final String ORDER_BY_ASC = "asc";
}
