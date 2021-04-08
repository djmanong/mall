package com.djmanong.mall.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallAdminWebApplicationTests {

    @Test
    public void contextLoads() {

        System.out.println("加密结果: " + DigestUtils.md5DigestAsHex("123456".getBytes()));
    }

}
