package com.djmanong.mall.admin.ums.controller;

import com.djmanong.mall.to.CommonResult;
import com.djmanong.mall.ums.entity.MemberLevel;
import com.djmanong.mall.ums.service.MemberLevelService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author DjManong
 */
@CrossOrigin
@RestController
public class UmsMemberLevelController {

    @DubboReference
    MemberLevelService memberLevelService;

    /**
     * 查出所有会员等级
     * @return
     */
    @GetMapping("/memberLevel/list")
    public Object memberLevelList() {
        List<MemberLevel> list = memberLevelService.list();
        return new CommonResult().success(list);
    }
}
