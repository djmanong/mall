package com.djmanong.mall.search;

import com.djmanong.mall.vo.search.SearchParam;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallSearchApplicationTests {

    @Autowired
    JestClient jestClient;

    @Autowired
    SearchProductService service;

    @Test
    public void dslTest() {
        SearchParam searchParam = new SearchParam();
        searchParam.setKeyword("手机");
        searchParam.setBrand(new String[]{"苹果"});
        searchParam.setCatelog3(new String[]{"19", "20"});
        searchParam.setPriceFrom(5000);
        searchParam.setPriceTo(10000);
        searchParam.setProps(new String[]{"45:4.7", "46:4G"});

        service.searchProduct(searchParam);
    }

    @Test
    public void contextLoads() throws IOException {
        Search search = new Search.Builder(null).addIndex("product").addType("info").build();
        SearchResult result = jestClient.execute(search);
        System.out.println(result.getTotal());
    }

    @Test
    public void testSearchSourceBuilder() {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must();

        builder.query(boolQueryBuilder);
        System.out.println(builder.toString());
    }

}
