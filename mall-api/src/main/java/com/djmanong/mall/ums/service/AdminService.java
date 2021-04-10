package com.djmanong.mall.ums.service;

import com.djmanong.mall.ums.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
public interface AdminService extends IService<Admin> {

    /**
     * 登录验证
     * @param username 用户名
     * @param password 密码
     * @return
     */
    Admin login(String username, String password);

    /**
     * 获取用户详情
     * @param userName
     * @return
     */
    Admin getUserInfo(String userName);
}
