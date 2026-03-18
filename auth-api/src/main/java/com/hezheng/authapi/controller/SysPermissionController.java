package com.hezheng.authapi.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hezheng.authapi.common.Result;
import com.hezheng.authapi.entity.SysPermission;
import com.hezheng.authapi.entity.SysRolePermission;
import com.hezheng.authapi.mapper.SysRolePermissionMapper;
import com.hezheng.authapi.service.SysPermissionService;
import com.hezheng.authapi.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permission")
public class SysPermissionController {
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/treelist")
    public Result treeList() {
        List<SysPermission> list = sysPermissionService.selectPermissionList();
        return Result.success(list);
    }

    @GetMapping("/role/{id}")
    public Result rolePermission(@PathVariable Long id) {
        List<SysRolePermission> rolePermissions = sysRolePermissionMapper.selectList(new QueryWrapper<SysRolePermission>().eq("role_id", id));
        return Result.success(rolePermissions);
    }


    //模糊查询（多个）
    @GetMapping("/list")
    public Result list(SysPermission sysPermission){

        //添加查询条件
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();

        List<SysPermission> list = sysPermissionService.list(queryWrapper);

        return Result.success(list);
    }
    //单个查询（根据id）
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id){
        return Result.success(sysPermissionService.getById(id));
    }

    //添加
    @PostMapping
    public Result add(@RequestBody SysPermission sysPermission){
        sysPermissionService.save(sysPermission);
        return Result.success();
    }

    //修改
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody SysPermission sysPermission){
        sysPermission.setId(id);
        sysPermissionService.updateById(sysPermission);
        return Result.success();
    }

    //删除（支持批量删除）
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable Long[] ids){
        sysPermissionService.removeBatchByIds(Arrays.asList(ids));
        return Result.success();
    }

    @GetMapping("/getRouters")
    public Result getRouters(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = jwtUtil.getUserIdFromToken(token);

        List<SysPermission> permissions = sysPermissionService.selectPermissionByUserId(userId);

        return Result.success(permissions);
    }
}
