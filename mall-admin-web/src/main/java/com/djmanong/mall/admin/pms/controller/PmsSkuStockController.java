package com.djmanong.mall.admin.pms.controller;

import com.djmanong.mall.pms.entity.SkuStock;
import com.djmanong.mall.pms.service.SkuStockService;
import com.djmanong.mall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * sku库存Controller
 * @author DjManong
 */
@CrossOrigin
@RestController
@Api(tags = "PmsSkuStockController", description = "sku商品库存管理")
@RequestMapping("/sku")
public class PmsSkuStockController {

    @DubboReference
    private SkuStockService skuStockService;

    @ApiOperation("根据商品编号及编号模糊搜索sku库存")
    @RequestMapping(value = "/{pid}", method = RequestMethod.GET)
    @ResponseBody
    public Object getList(@PathVariable Long pid, @RequestParam(value = "keyword",required = false) String keyword) {

        //TODO 根据商品编号及编号模糊搜索sku库存
        return new CommonResult().success(null);
    }
    @ApiOperation("批量更新库存信息")
    @RequestMapping(value ="/update/{pid}",method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable Long pid,@RequestBody List<SkuStock> skuStockList){

        //TODO 批量更新库存信息
        return new CommonResult().success(null);
    }
}
