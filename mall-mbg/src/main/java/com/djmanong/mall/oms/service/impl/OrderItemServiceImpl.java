package com.djmanong.mall.oms.service.impl;

import com.djmanong.mall.oms.entity.OrderItem;
import com.djmanong.mall.oms.mapper.OrderItemMapper;
import com.djmanong.mall.oms.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单中所包含的商品 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
