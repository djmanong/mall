package com.djmanong.mall.vo.ums;

import lombok.Data;

/**
 * 登录信息封装
 * @author DjManong
 */
@Data
public class LoginResponseVo {

    /**用户名*/
    private String username;

    /**等级*/
    private Long memberLevelId;

    /**昵称*/
    private String nickname;

    /**手机号码*/
    private String phone;

    /**访问令牌*/
    private String accessToken;
}
