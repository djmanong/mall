package com.djmanong.mall.search;

import com.djmanong.mall.vo.search.SearchParam;
import com.djmanong.mall.vo.search.SearchResponse;

/**
 * 商品检索服务
 * @author DjManong
 */
public interface SearchProductService {

    /**
     * 商品检索
     * @param searchParam
     * @return
     */
    SearchResponse searchProduct(SearchParam searchParam);
}
