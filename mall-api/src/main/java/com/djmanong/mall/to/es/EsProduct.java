package com.djmanong.mall.to.es;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author DjManong
 */
@Data
public class EsProduct implements Serializable {

    private static final long serialVersionUID = -1L;
    /**spu-id*/
    private Long id;
    /**spuId-skuId*/
    private String productSn;
    private Long brandId;
    private String brandName;

    private Long productCategoryId;
    private String productCategoryName;
    private String pic;

    /**这是需要检索的字段 分词*/
    private String name;
    /**也可以检索，这是一个加分字段*/
    private String subTitle;
    /**也可以检索，这是一个加分字段*/
    private String keywords;
    /**sku-price*/
    private BigDecimal price;
    /**sku-sale*/
    private Integer sale;
    private Integer newStatus;
    private Integer recommandStatus;
    /**sku-stock*/
    private Integer stock;
    /**促销类型*/
    private Integer promotionType;
    /**排序*/
    private Integer sort;
    /**评论数量共享spu*/
    private Integer commentCount;

    // 在es中, 嵌入式对象的mapping一定要用nested声明, 这样才能正确的检索到数据, 例如attrValueList和skuProductInfos
    /**商品的筛选属性(SPU属性)*/
    private List<EsProductAttributeValue> attrValueList;
//    网络制式：3G 4G 5G，
//    操作系统：Android IOS

    /**商品的sku信息*/
    private List<EsSkuProductInfo> skuProductInfos;
}
