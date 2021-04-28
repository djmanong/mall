package com.djmanong.mall.portal.controller;

import com.djmanong.mall.pms.service.ProductService;
import com.djmanong.mall.to.es.EsProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 商品详情
 * @author DjManong
 */
@RestController
@Api(tags = "ProductItemController", description = "商品详情")
public class ProductItemController {

    @DubboReference
    ProductService productService;

    @Qualifier("mainThreadPoolExecutor")
    @Autowired
    ThreadPoolExecutor mainThreadPoolExecutor;

    @Qualifier("otherThreadPoolExecutor")
    @Autowired
    ThreadPoolExecutor otherThreadPoolExecutor;

    /**
     * 查询商品各类信息(基本信息，销售属性，促销情况等), 整合异步任务
     * @param id
     * @return
     */
    public EsProduct product(Long id) {

        // 1. 查询基本信息
        CompletableFuture<EsProduct> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询基本信息...");
            EsProduct esProduct = new EsProduct();
            return esProduct;
        }, mainThreadPoolExecutor).whenComplete((result, e) -> {
            // result: 结果 e: 异常
        });

        // 2. 查询销售信息...
        // ...
        return null;
    }

    /**
     * 商品详情
     * @param id
     * @return
     */
    @ApiOperation(value = "检索商品详情")
    @GetMapping("/item/{id}.html")
    public EsProduct productInfo(@PathVariable("id") Long id) {
        return productService.productAllInfo(id);
    }

    /**
     * 商品详情sku
     * @param id
     * @return
     */
    @ApiOperation(value = "检索商品销售属性")
    @GetMapping("/item/sku/{id}.html")
    public EsProduct productSkuInfo(@PathVariable("id") Long id) {
        return productService.productSkuInfo(id);
    }

}
