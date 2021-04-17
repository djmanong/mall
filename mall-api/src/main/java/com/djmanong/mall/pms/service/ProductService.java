package com.djmanong.mall.pms.service;

import com.djmanong.mall.pms.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djmanong.mall.vo.PageInfoVo;
import com.djmanong.mall.vo.product.PmsProductParam;
import com.djmanong.mall.vo.product.PmsProductQueryParam;

import java.util.List;

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
     * 查询商品详情
     * @param id
     * @return
     */
    Product productInfo(Long id);

    /**
     * 根据复杂查询条件返回分页数据
     * @param productQueryParam
     * @return
     */
    PageInfoVo productPageInfo(PmsProductQueryParam productQueryParam);

    /**
     * 保存商品数据
     * @param productParam
     */
    void saveProduct(PmsProductParam productParam);

    /**
     * 批量商品上下架
     * @param ids
     * @param publishStatus
     */
    void updatePublishStatus(List<Long> ids, Integer publishStatus);
}
