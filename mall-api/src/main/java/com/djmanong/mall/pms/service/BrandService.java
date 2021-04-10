package com.djmanong.mall.pms.service;

import com.djmanong.mall.pms.entity.Brand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djmanong.mall.vo.PageInfoVo;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
public interface BrandService extends IService<Brand> {

    /**
     * 根据分页信息查询品牌
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfoVo brandPageInfo(String keyword, Integer pageNum, Integer pageSize);
}
