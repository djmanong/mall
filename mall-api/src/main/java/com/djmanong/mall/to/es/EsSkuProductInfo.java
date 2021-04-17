package com.djmanong.mall.to.es;

import com.djmanong.mall.pms.entity.SkuStock;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品的sku信息
 * @author DjManong
 */
@Data
public class EsSkuProductInfo extends SkuStock implements Serializable {

    /**
     * sku的特定标题
     */
    private String skuTitle;

    /**
     * 每个sku不同的属性以及它的值
     * 颜色：黑色
     * 内存: 8G
     */
    private List<EsProductAttributeValue> attributeValues;
}
