package com.djmanong.mall.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djmanong.mall.constant.SystemCacheConstant;
import com.djmanong.mall.pms.entity.ProductCategory;
import com.djmanong.mall.pms.mapper.ProductCategoryMapper;
import com.djmanong.mall.pms.service.ProductCategoryService;
import com.djmanong.mall.vo.product.PmsProductCategoryWithChildrenItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Slf4j
@Service
@DubboService
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    ProductCategoryMapper categoryMapper;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<PmsProductCategoryWithChildrenItem> listCategoryWithChildrenItem(Integer i) {

        Object cacheMenu = redisTemplate.opsForValue().get(SystemCacheConstant.CATEGORY_MENU_CACHE_KEY);
        List<PmsProductCategoryWithChildrenItem> items;
        if (cacheMenu != null) {
            // 缓存中存在分类信息
            log.info("多级分类数据命中缓存...");
            items = (List<PmsProductCategoryWithChildrenItem>) cacheMenu;
        } else {
            items = categoryMapper.listCategoryWithChildrenItem(i);
            redisTemplate.opsForValue().set(SystemCacheConstant.CATEGORY_MENU_CACHE_KEY, items);
        }

        return items;
    }
}
