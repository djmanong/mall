package com.djmanong.mall.admin.pms.controller;

import com.djmanong.mall.pms.service.ProductAttributeCategoryService;
import com.djmanong.mall.to.CommonResult;
import com.djmanong.mall.vo.PageInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * 商品属性分类Controller
 * @author DjManong
 */

@CrossOrigin
@RestController
@Api(tags = "PmsProductAttributeCategoryController", description = "商品属性分类管理")
@RequestMapping("/productAttribute/category")
public class PmsProductAttributeCategoryController {
    
    @DubboReference
    private ProductAttributeCategoryService productAttributeCategoryService;

    @ApiOperation("添加商品属性分类")
    @PostMapping(value = "/create")
    public Object create(@RequestParam String name) {

        //TODO 添加商品属性分类
        return new CommonResult().success(null);
    }

    @ApiOperation("修改商品属性分类")
    @PostMapping(value = "/update/{id}")
    public Object update(@PathVariable Long id, @RequestParam String name) {
        //TODO 修改商品属性分类
        return new CommonResult().success(null);
    }

    @ApiOperation("删除单个商品属性分类")
    @GetMapping(value = "/delete/{id}")
    public Object delete(@PathVariable Long id) {
        //TODO 删除单个商品属性分类
        return new CommonResult().success(null);
    }

    @ApiOperation("获取单个商品属性分类信息")
    @GetMapping(value = "/{id}")
    public Object getItem(@PathVariable Long id) {
        //TODO 获取单个商品属性分类信息
        return new CommonResult().success(null);
    }

    @ApiOperation("分页获取所有商品属性分类")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object getList(@RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum) {
        //TODO 分页获取所有商品属性分类
        PageInfoVo pageInfoVo = productAttributeCategoryService.productAttributeCategoryPageInfo(pageNum, pageSize);
        return new CommonResult().success(pageInfoVo);
    }

    @ApiOperation("获取所有商品属性分类及其下属性【难度较高】")
    @RequestMapping(value = "/list/withAttr", method = RequestMethod.GET)
    @ResponseBody
    public Object getListWithAttr() {

        //TODO 获取所有商品属性分类及其下属性
        return new CommonResult().success(null);
    }
}
