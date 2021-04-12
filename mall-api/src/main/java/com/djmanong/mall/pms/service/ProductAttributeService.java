package com.djmanong.mall.pms.service;

import com.djmanong.mall.pms.entity.ProductAttribute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djmanong.mall.vo.PageInfoVo;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
public interface ProductAttributeService extends IService<ProductAttribute> {

    /**
     * 查询某个属性分类下的所有销售属性和参数
     * @param cid
     * @param type
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageInfoVo getCategoryAttributes(Long cid, Integer type, Integer pageSize, Integer pageNum);
}
