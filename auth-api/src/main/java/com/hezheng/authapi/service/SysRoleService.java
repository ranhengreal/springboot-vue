package com.hezheng.authapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hezheng.authapi.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {
    /**
     * 给角色授权
     * @param roleId 角色编号
     * @param permissionIds 权限编号的集合
     */
    public void assignPermission(Long roleId, List<Long> permissionIds);
}
