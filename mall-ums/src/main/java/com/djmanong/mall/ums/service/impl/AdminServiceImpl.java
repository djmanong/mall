package com.djmanong.mall.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djmanong.mall.ums.entity.Admin;
import com.djmanong.mall.ums.mapper.AdminMapper;
import com.djmanong.mall.ums.service.AdminService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
@DubboService
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String username, String password) {

        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>().eq("username", username).eq("password", md5Password);
        return adminMapper.selectOne(queryWrapper);
    }

    @Override
    public Admin getUserInfo(String userName) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", userName));
    }
}
