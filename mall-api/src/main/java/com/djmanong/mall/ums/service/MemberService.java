package com.djmanong.mall.ums.service;

import com.djmanong.mall.ums.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
public interface MemberService extends IService<Member> {

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    Member login(String username, String password);
}
