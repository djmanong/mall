package com.djmanong.mall.portal.controller;

import com.djmanong.mall.search.SearchProductService;
import com.djmanong.mall.vo.search.SearchParam;
import com.djmanong.mall.vo.search.SearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品检索
 * @author DjManong
 */
@RestController
@Api(tags = "ProductSearchController", description = "商品检索")
public class ProductSearchController {

    @DubboReference
    SearchProductService searchProductService;

    @ApiOperation(value = "查询商品信息")
    @GetMapping("/search")
    public SearchResponse productSearchResponse(SearchParam searchParam) {
        SearchResponse searchResponse = searchProductService.searchProduct(searchParam);
        return searchResponse;
    }

}
