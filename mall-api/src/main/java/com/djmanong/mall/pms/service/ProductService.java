package com.djmanong.mall.pms.service;

import com.djmanong.mall.pms.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djmanong.mall.vo.PageInfoVo;
import com.djmanong.mall.vo.product.PmsProductQueryParam;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
public interface ProductService extends IService<Product> {

    /**
     * 根据复杂查询条件返回分页数据
     * @param productQueryParam
     * @return
     */
    PageInfoVo productPageInfo(PmsProductQueryParam productQueryParam);
}
