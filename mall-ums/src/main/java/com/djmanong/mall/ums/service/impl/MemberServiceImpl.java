package com.djmanong.mall.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djmanong.mall.ums.entity.Member;
import com.djmanong.mall.ums.mapper.MemberMapper;
import com.djmanong.mall.ums.service.MemberService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
@DubboService
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    MemberMapper memberMapper;

    @Override
    public Member login(String username, String password) {
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        return memberMapper.selectOne(new QueryWrapper<Member>()
                .eq("username", username)
                .eq("password", md5Password));
    }
}
