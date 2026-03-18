package com.hezheng.authapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hezheng.authapi.entity.SysPermission;

import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {
    public List<SysPermission> selectPermissionList();

    /**
     * 根据用户编号查询该用户拥有的权限
     * @param userId
     * @return
     */
    public List<SysPermission> selectPermissionByUserId(Long userId);
}
