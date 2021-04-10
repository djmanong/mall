package com.djmanong.mall.pms.service.impl;

import com.djmanong.mall.pms.entity.Product;
import com.djmanong.mall.pms.mapper.ProductMapper;
import com.djmanong.mall.pms.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-10
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
