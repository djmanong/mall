package com.djmanong.mall.oms.service.impl;

import com.djmanong.mall.oms.entity.CartItem;
import com.djmanong.mall.oms.mapper.CartItemMapper;
import com.djmanong.mall.oms.service.CartItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

}
