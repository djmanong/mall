package com.djmanong.mall.pms;

import com.djmanong.mall.pms.entity.Brand;
import com.djmanong.mall.pms.entity.Product;
import com.djmanong.mall.pms.service.BrandService;
import com.djmanong.mall.pms.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MallPmsApplicationTests {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @Autowired
    // StringRedisTemplate redisTemplate;
    RedisTemplate<String, Object> redisTemplate;

    @Test
    public void contextLoads() {
        Product product = productService.getById(1);
        System.out.println(product);

        // Brand brand = new Brand();
        // brand.setName("哈哈");
        // brandService.save(brand);
        // System.out.println("保存成功");
    }

    @Test
    public void test() {
        // redisTemplate.opsForValue().set("k1", "v1");
        // System.out.println(redisTemplate.opsForValue().get("k1"));


        Brand brand = new Brand();
        brand.setName("数据");
        redisTemplate.opsForValue().set("redis", brand);

        Brand redis = (Brand) redisTemplate.opsForValue().get("redis");
        System.out.println("获取数据======> redis: " + redis.getName());

    }

}
