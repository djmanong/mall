package com.djmanong.mall.oms.service.impl;

import com.djmanong.mall.oms.entity.Order;
import com.djmanong.mall.oms.mapper.OrderMapper;
import com.djmanong.mall.oms.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
