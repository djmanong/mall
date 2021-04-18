package com.djmanong.mall.search.service.impl;

import com.djmanong.mall.constant.EsConstant;
import com.djmanong.mall.search.SearchProductService;
import com.djmanong.mall.vo.search.SearchParam;
import com.djmanong.mall.vo.search.SearchResponse;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author DjManong
 */
@Slf4j
@Service
@DubboService
public class SearchProductServiceImpl implements SearchProductService {

    @Autowired
    JestClient jestClient;

    @Override
    public SearchResponse searchProduct(SearchParam searchParam) {
        //构建检索条件
        String dsl = buildDsl(searchParam);
        log.debug("商品检索DSL语句: {}", dsl);

        Search search = new Search.Builder(dsl)
                .addIndex(EsConstant.PRODUCT_ES_INDEX)
                .addType(EsConstant.PRODUCT_INFO_ES_TYPE)
                .build();
        SearchResult result = null;
        try {
            // 检索
            result = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将检索结果result转换为SearchResponse
        SearchResponse searchResponse = buildSearchResponse(result);

        searchResponse.setPageNum(searchParam.getPageNum());
        searchResponse.setPageSize(searchParam.getPageSize());
        return searchResponse;
    }

    /**
     * 封装SearchResponse
     * @param result
     * @return
     */
    private SearchResponse buildSearchResponse(SearchResult result) {
        SearchResponse searchResponse = new SearchResponse();
        // // 所有可以筛选的: 属性 分类 品牌
        // searchResponse.setAttrs();
        // searchResponse.setCatelog();
        // searchResponse.setBrand();
        //
        // // 封装查询到的记录
        // searchResponse.setProducts();

        // 总记录数
        searchResponse.setTotal(result.getTotal());

        return searchResponse;
    }

    /**
     * 构建dsl语句
     * @param searchParam
     * @return
     */
    private String buildDsl(SearchParam searchParam) {

        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        /*
        * 1. 查询
        *   - 检索
        *   - 过滤
        *       1. 按照属性、品牌或者分类过滤
        * */
        // 检索
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("skuProductInfos.skuTitle", searchParam.getKeyword());
            NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("skuProductInfos", matchQuery, ScoreMode.None);
            boolQuery.must(nestedQuery);
        }
        // 过滤
        if (searchParam.getCatelog3() != null && searchParam.getCatelog3().length > 0) {
            // 按照三级分类的条件过滤
            boolQuery.filter(QueryBuilders.termsQuery("productCategoryId", searchParam.getCatelog3()));
        }
        if (searchParam.getBrand() != null && searchParam.getBrand().length > 0) {
            // 按照品牌过滤
            boolQuery.filter(QueryBuilders.termsQuery("brandName.keyword", searchParam.getBrand()));
        }
        if (searchParam.getProps() != null && searchParam.getProps().length > 0) {
            // 按照所有的筛选属性过滤
            String[] props = searchParam.getProps();
            for (String prop : props) {
                String[] split = prop.split(":");
                // 2:4g-3g 2号属性的值是4g或3g
                BoolQueryBuilder must = QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("attrValueList.productAttributeId", split[0]))
                        .must(QueryBuilders.termsQuery("attrValueList.value.keyword", split[1].split("-")));

                NestedQueryBuilder query = QueryBuilders.nestedQuery("attrValueList", must, ScoreMode.None);
                boolQuery.filter(query);
            }
        }
        // 价格区间过滤
        if (searchParam.getPriceFrom() != null || searchParam.getPriceTo() != null) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price");
            if (searchParam.getPriceFrom() != null) {
                rangeQuery.gte(searchParam.getPriceFrom());
            }
            if (searchParam.getPriceTo() != null) {
                rangeQuery.lte(searchParam.getPriceTo());
            }
            boolQuery.filter(rangeQuery);
        }

        builder.query(boolQuery);

        // 2. 高亮 有关键词时才高亮
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuProductInfos.skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            builder.highlighter(highlightBuilder);
        }

        // 3. 聚合
        // 按照品牌聚合
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg").field("brandName.keyword");
        brandAgg.subAggregation(AggregationBuilders.terms("brandId").field("brandId"));
        builder.aggregation(brandAgg);
        // 按照分类聚合
        TermsAggregationBuilder categoryAgg = AggregationBuilders.terms("category_agg").field("productCategoryName.keyword");
        categoryAgg.subAggregation(AggregationBuilders.terms("categoryId_agg").field("productCategoryId"));
        builder.aggregation(categoryAgg);
        // 按照属性聚合
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attr_agg", "attrValueList");
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrName_agg").field("attrValueList.name");
        // attrNameAgg中的两个子聚合
        attrNameAgg.subAggregation(AggregationBuilders.terms("attrValue_agg").field("attrValueList.value.keyword"));
        attrNameAgg.subAggregation(AggregationBuilders.terms("attrId_agg").field("attrValueList.productAttributeId"));
        attrAgg.subAggregation(attrNameAgg);
        builder.aggregation(attrAgg);

        // 4. 分页
        builder.from((searchParam.getPageNum() - 1) * searchParam.getPageSize());
        builder.size(searchParam.getPageSize());

        // 5. 排序
        if (!StringUtils.isEmpty(searchParam.getOrder())) {
            String order = searchParam.getOrder();
            String[] split = order.split(":");
            if (split[0].equals(EsConstant.ORDER_BY_DEFAULT)) {
                // 综合排序 默认顺序
            }
            if (split[0].equals(EsConstant.ORDER_BY_SALE)) {
                // 销量排序
                FieldSortBuilder sale = SortBuilders.fieldSort("sale");
                if (split[1].equalsIgnoreCase(EsConstant.ORDER_BY_ASC)) {
                    sale.order(SortOrder.ASC);
                } else {
                    sale.order(SortOrder.DESC);
                }
                builder.sort(sale);
            }
            if (split[0].equals(EsConstant.ORDER_BY_PRICE)) {
                // 价格排序
                FieldSortBuilder price = SortBuilders.fieldSort("price");
                if (split[1].equalsIgnoreCase(EsConstant.ORDER_BY_ASC)) {
                    price.order(SortOrder.ASC);
                } else {
                    price.order(SortOrder.DESC);
                }
                builder.sort(price);
            }
        }

        return builder.toString();
    }
}
