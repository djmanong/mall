package com.djmanong.mall.ums.service.impl;

import com.djmanong.mall.ums.entity.Admin;
import com.djmanong.mall.ums.mapper.AdminMapper;
import com.djmanong.mall.ums.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
