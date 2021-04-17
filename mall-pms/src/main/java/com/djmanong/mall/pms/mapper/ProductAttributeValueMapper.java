package com.djmanong.mall.pms.mapper;

import com.djmanong.mall.pms.entity.ProductAttribute;
import com.djmanong.mall.pms.entity.ProductAttributeValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djmanong.mall.to.es.EsProductAttributeValue;

import java.util.List;

/**
 * <p>
 * 存储产品参数信息的表 Mapper 接口
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
public interface ProductAttributeValueMapper extends BaseMapper<ProductAttributeValue> {

    /**
     * 查询商品公共属性(spu)
     * @param id 商品id
     * @return
     */
    List<EsProductAttributeValue> selectProductBaseAttributeAndValue(Long id);

    /**
     * 获取商品sku属性名
     * @param id
     * @return
     */
    List<ProductAttribute> selectProductSaleAttributeName(Long id);

}
