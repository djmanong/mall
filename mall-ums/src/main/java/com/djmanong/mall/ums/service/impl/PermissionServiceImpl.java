package com.djmanong.mall.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djmanong.mall.ums.entity.Permission;
import com.djmanong.mall.ums.mapper.PermissionMapper;
import com.djmanong.mall.ums.service.PermissionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户权限表 服务实现类
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
