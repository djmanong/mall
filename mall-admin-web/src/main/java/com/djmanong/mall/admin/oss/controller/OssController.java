package com.djmanong.mall.admin.oss.controller;

import com.djmanong.mall.admin.oss.OssComponent;
import com.djmanong.mall.constant.OssPolicyResult;
import com.djmanong.mall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Oss相关操作接口
 * @author DjManong
 */
@CrossOrigin
@Controller
@Api(tags = "OssController",description = "Oss管理")
@RequestMapping("/aliyun/oss")
public class OssController {

	@Autowired
	private OssComponent ossComponent;

	@ApiOperation(value = "oss上传签名生成")
	@GetMapping(value = "/policy")
	@ResponseBody
	public Object policy() {
		OssPolicyResult result = ossComponent.policy();
		return new CommonResult().success(result);
	}

}
