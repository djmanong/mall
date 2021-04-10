package com.djmanong.mall.pms.service.impl;

import com.djmanong.mall.pms.entity.ProductFullReduction;
import com.djmanong.mall.pms.mapper.ProductFullReductionMapper;
import com.djmanong.mall.pms.service.ProductFullReductionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品满减表(只针对同商品) 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-10
 */
@Service
public class ProductFullReductionServiceImpl extends ServiceImpl<ProductFullReductionMapper, ProductFullReduction> implements ProductFullReductionService {

}
