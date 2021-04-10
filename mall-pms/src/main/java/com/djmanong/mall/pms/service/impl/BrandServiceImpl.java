package com.djmanong.mall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djmanong.mall.pms.entity.Brand;
import com.djmanong.mall.pms.mapper.BrandMapper;
import com.djmanong.mall.pms.service.BrandService;
import com.djmanong.mall.vo.PageInfoVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
@DubboService
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageInfoVo brandPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        QueryWrapper<Brand> queryWrapper = null;
        if (!StringUtils.isEmpty(keyword)) {
            // 自动拼接%
            queryWrapper = new QueryWrapper<Brand>().like("name", keyword);
        }
        IPage<Brand> page = brandMapper.selectPage(new Page<Brand>(pageNum.longValue(), pageSize.longValue()), queryWrapper);

        return new PageInfoVo(page.getTotal(), page.getPages(), pageSize.longValue(), page.getRecords(), page.getCurrent());
    }
}
