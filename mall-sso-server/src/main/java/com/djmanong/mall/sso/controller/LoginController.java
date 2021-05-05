package com.djmanong.mall.sso.controller;

import com.djmanong.mall.constant.SystemCacheConstant;
import com.djmanong.mall.to.CommonResult;
import com.djmanong.mall.ums.entity.Member;
import com.djmanong.mall.ums.service.MemberService;
import com.djmanong.mall.vo.ums.LoginResponseVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author DjManong
 */
@RestController
@Api(tags = "LoginController", description = "用户登录操作")
public class LoginController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @DubboReference
    MemberService memberService;

    @ApiOperation(value = "查询用户详细信息")
    @GetMapping("/userInfo")
    public CommonResult getUserInfo(@RequestParam("accessToken") String accessToken) throws IOException {
        String redisKey = SystemCacheConstant.LOGIN_MEMBER + accessToken;
        String memberJson = redisTemplate.opsForValue().get(redisKey);
        Member member = new ObjectMapper().readValue(memberJson, Member.class);
        member.setId(null);
        member.setPassword(null);
        return new CommonResult().success(member);
    }

    @ApiOperation(value = "登录验证")
    @PostMapping("/appLogin")
    public CommonResult loginForMall(@RequestParam(value = "username") String username,
                                     @RequestParam(value = "password") String password) throws JsonProcessingException {
        Member member = memberService.login(username, password);
        if (member == null) {
            // 登录失败
            CommonResult failed = new CommonResult().failed();
            failed.setMessage("用户名或密码错误!");
            return failed;
        } else {
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            String memberJson = new ObjectMapper().writeValueAsString(member);
            redisTemplate.opsForValue().set(SystemCacheConstant.LOGIN_MEMBER + token, memberJson, SystemCacheConstant.LOGIN_MEMBER_TIMEOUT, TimeUnit.SECONDS);
            LoginResponseVo responseVo = new LoginResponseVo();
            BeanUtils.copyProperties(member, responseVo);
            // 设置token
            responseVo.setAccessToken(token);
            return new CommonResult().success(responseVo);
        }
    }

}
