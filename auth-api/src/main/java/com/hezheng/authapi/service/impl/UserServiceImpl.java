package com.hezheng.authapi.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hezheng.authapi.common.CustomException;
import com.hezheng.authapi.entity.*;
import com.hezheng.authapi.mapper.*;
import com.hezheng.authapi.service.SysUserService;
import com.hezheng.authapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;


    @Override
    public List<SysUser> list(IPage<SysUser> page, Wrapper<SysUser> queryWrapper) {
        List<SysUser> list = super.list(page, queryWrapper);

        //查询用户所拥有的角色
        list.forEach(item->{
            //根据用户编号查询相关的角色信息
            List<SysRole> roleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>().inSql("id",
                    "SELECT role_id from sys_user_role where user_id=" + item.getId()));
            item.setRoleList(roleList);
        });


        return list;
    }

    @Override
    public SysUser getById(Serializable id) {
        SysUser sysUser = super.getById(id);
        List<SysRole> roleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>().inSql("id",
                "SELECT role_id from sys_user_role where user_id=" + id));
        sysUser.setRoleIds(roleList.stream().map(item->item.getId()).collect(Collectors.toList()));
        return sysUser;
    }

    @Override
    public boolean save(SysUser entity) {
        boolean result = super.save(entity);
        //添加用户角色
        for (Long roleId : entity.getRoleIds()) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(entity.getId());
            sysUserRole.setRoleId(roleId);

            sysUserRoleMapper.insert(sysUserRole);
        }
        return result;
    }

    @Override
    public boolean updateById(SysUser entity) {
        boolean result = super.updateById(entity);
        //判断是否要修改用户的角色

        if(entity.getRoleIds()!=null&&entity.getRoleIds().size()>0){
            //修改用户角色：删除已有角色添加现在的角色
            sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id",entity.getId()));

            //添加用户角色
            for (Long roleId : entity.getRoleIds()) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(entity.getId());
                sysUserRole.setRoleId(roleId);

                sysUserRoleMapper.insert(sysUserRole);
            }
        }
        return result;
    }

    @Override
    public SysUser login(SysUser user) {
        SysUser sysUser = baseMapper.selectOne(new QueryWrapper<SysUser>().eq("username", user.getUsername()));
        if (sysUser == null){
            throw new CustomException("用户不存在");
        }
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        if (!sysUser.getPassword().equals(md5Password)){
            throw new CustomException("密码不正确");
        }
        if (sysUser.getStatus() == 0) {
            throw new CustomException("用户已被禁用");
        }
        //查询用户拥有的角色编号
        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", sysUser.getId()));
        List<Long> roleIds = userRoleList.stream().map(sysUserRole -> sysUserRole.getRoleId()).collect(Collectors.toList());
        sysUser.setRoleIds(roleIds);

        //查询用户拥有的权限字符串
        List<SysRolePermission> rolePermissionList = sysRolePermissionMapper.selectList(new QueryWrapper<SysRolePermission>().in("role_id", roleIds));
        List<SysPermission> permissionList = sysPermissionMapper.selectList(new QueryWrapper<SysPermission>().in("id", rolePermissionList.stream().map(sysRolePermission -> sysRolePermission.getPermissionId()).collect(Collectors.toList())));
        sysUser.setPermissions(permissionList.stream().map(sysPermission -> sysPermission.getPermissionCode()).collect(Collectors.toList()));


        String token = jwtUtil.generateToken(sysUser.getId(), sysUser.getUsername());
        sysUser.setToken(token);
        return sysUser;
    }

    @Override
    public void updatePassword(Long userId, String password) {
        baseMapper.updatePassword(userId,password);
    }
}
