package com.djmanong.mall.admin.ums.controller;

import com.djmanong.mall.admin.ums.vo.UmsAdminLoginParam;
import com.djmanong.mall.admin.ums.vo.UmsAdminParam;
import com.djmanong.mall.admin.utils.JwtTokenUtil;
import com.djmanong.mall.to.CommonResult;
import com.djmanong.mall.ums.entity.Admin;
import com.djmanong.mall.ums.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台用户管理
 * @author DjManong
 * @CrossOrigin: 解决跨域问题
 */
@Slf4j
@CrossOrigin
@RestController
@Api(tags = "AdminController", description = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {
    
    @DubboReference
    private AdminService adminService;
    @Value("${mall.jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${mall.jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "用户注册")
    @PostMapping(value = "/register")
    public Object register(@Valid @RequestBody UmsAdminParam umsAdminParam, BindingResult result) {
        Admin admin = null;
        //TODO 完成注册功能

        // 校验错误数量
        // int errorCount = result.getErrorCount();
        // if (errorCount > 0) {
        //     // 校验错误
        //     List<FieldError> fieldErrors = result.getFieldErrors();
        //     for (FieldError fieldError : fieldErrors) {
        //         log.debug("属性: {}, 校验出错; 传来的值是: {}; 详细信息: {}", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        //     }
        //     return new CommonResult().validateFailed(result);
        // } else {
        //     // 注册
        // }

        int i = 10 / 0;
        log.debug("需要注册的用户详情: {}", umsAdminParam);

        return new CommonResult().success(admin);
    }

    /**
     * 登录成功以后用户的信息以jwt令牌的方式返回给前端
     * @param umsAdminLoginParam
     * @param result
     * @return
     *
     * 1. 如果前端发过来的是json字符串, 需要封装对象
     * public Object login(@RequestBody UmsAdminLoginParam umsAdminLoginParam, BindingResult result)
     * 2. 如果前端发过来的是key=value字符串, 要封装对象
     * public Object login(UmsAdminLoginParam umsAdminLoginParam, BindingResult result)
     */
    @ApiOperation(value = "登录以后返回token")
    @PostMapping(value = "/login")
    public Object login(@RequestBody UmsAdminLoginParam umsAdminLoginParam, BindingResult result) {

        //去数据库登录
        Admin admin = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());

        //登陆成功生成token，此token携带基本用户信息，以后就不用去数据库了
        String token = jwtTokenUtil.generateToken(admin);
        if (token == null) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return new CommonResult().success(tokenMap);
    }

    @ApiOperation(value = "刷新token")
    @GetMapping(value = "/token/refresh")
    public Object refreshToken(HttpServletRequest request) {
        //1、获取请求头中的Authorization完整值
        String oldToken = request.getHeader(tokenHeader);
        String refreshToken = "";

        //2、从请求头中的Authorization中分离出jwt的值
        String token = oldToken.substring(tokenHead.length());

        //3、是否可以进行刷新（未过刷新时间）
        if (jwtTokenUtil.canRefresh(token)) {
            refreshToken =  jwtTokenUtil.refreshToken(token);
        }else  if(refreshToken == null && "".equals(refreshToken)){
            return new CommonResult().failed();
        }

        //将新的token交给前端
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return new CommonResult().success(tokenMap);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public Object getAdminInfo(HttpServletRequest request) {
        String oldToken = request.getHeader(tokenHeader);
        String userName = jwtTokenUtil.getUserNameFromToken(oldToken.substring(tokenHead.length()));

        /*
        * getOne是mybatis-plus生成的，而且带有泛型
        * dubbo无法直接调用mybatis-plus中带泛型的service,可能会有兼容问题，最好不要远程调用
        * */
        // Admin umsAdmin = adminService.getOne(new QueryWrapper<Admin>().eq("username",userName));
        Admin umsAdmin = adminService.getUserInfo(userName);
        Map<String, Object> data = new HashMap<>();
        data.put("roles", new String[]{"TEST"});
        data.put("username", umsAdmin.getUsername());
        data.put("icon", umsAdmin.getIcon());
        return new CommonResult().success(data);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Object logout() {
        //TODO 用户退出

        return new CommonResult().success(null);
    }

    @ApiOperation("根据用户名或姓名分页获取用户列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "name",required = false) String name,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        //TODO 分页查询用户信息

        //TODO 响应需要包含分页信息；详细查看swagger规定
        return new CommonResult().failed();
    }

    @ApiOperation("获取指定用户信息")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Object getItem(@PathVariable Long id){

        //TODO 获取指定用户信息
        return new CommonResult().failed();
    }

    @ApiOperation("更新指定用户信息")
    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable Long id,@RequestBody Admin admin){

        //TODO 更新指定用户信息
        return new CommonResult().failed();
    }

    @ApiOperation("删除指定用户信息")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Object delete(@PathVariable Long id){
        //TODO 删除指定用户信息
        return new CommonResult().failed();
    }

    @ApiOperation("给用户分配角色")
    @RequestMapping(value = "/role/update",method = RequestMethod.POST)
    @ResponseBody
    public Object updateRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") List<Long> roleIds){
        //TODO 给用户分配角色
        return new CommonResult().failed();
    }

    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/role/{adminId}",method = RequestMethod.GET)
    @ResponseBody
    public Object getRoleList(@PathVariable Long adminId){
        //TODO 获取指定用户的角色

        return new CommonResult().success(null);
    }

    @ApiOperation("给用户分配(增减)权限")
    @RequestMapping(value = "/permission/update",method = RequestMethod.POST)
    @ResponseBody
    public Object updatePermission(@RequestParam Long adminId,
                                   @RequestParam("permissionIds") List<Long> permissionIds){
        //TODO 给用户分配(增减)权限

        return new CommonResult().failed();
    }

    @ApiOperation("获取用户所有权限（包括+-权限）")
    @RequestMapping(value = "/permission/{adminId}",method = RequestMethod.GET)
    @ResponseBody
    public Object getPermissionList(@PathVariable Long adminId){
        //TODO 获取用户所有权限（包括+-权限）
        return new CommonResult().failed();
    }
}
