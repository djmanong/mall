package com.djmanong.mall.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djmanong.mall.pms.entity.Comment;
import com.djmanong.mall.pms.mapper.CommentMapper;
import com.djmanong.mall.pms.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品评价表 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
