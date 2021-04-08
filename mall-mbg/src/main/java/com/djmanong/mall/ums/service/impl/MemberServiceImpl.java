package com.djmanong.mall.ums.service.impl;

import com.djmanong.mall.ums.entity.Member;
import com.djmanong.mall.ums.mapper.MemberMapper;
import com.djmanong.mall.ums.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

}
