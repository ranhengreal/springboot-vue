package com.hezheng.authapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hezheng.authapi.entity.SysRole;
import com.hezheng.authapi.entity.SysRolePermission;
import com.hezheng.authapi.mapper.SysRoleMapper;
import com.hezheng.authapi.mapper.SysRolePermissionMapper;
import com.hezheng.authapi.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService {
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Override
    public void assignPermission(Long roleId, List<Long> permissionIds) {
        //1.删除角色之前的权限
        sysRolePermissionMapper.delete(new QueryWrapper<SysRolePermission>().eq("role_id",roleId));
        //2.批量添加角色的权限
        for (Long permissionId : permissionIds) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setRoleId(roleId);
            sysRolePermission.setPermissionId(permissionId);

            sysRolePermissionMapper.insert(sysRolePermission);
        }
    }
}
