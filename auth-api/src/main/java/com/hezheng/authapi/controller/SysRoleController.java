package com.hezheng.authapi.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hezheng.authapi.common.Result;
import com.hezheng.authapi.entity.SysRole;
import com.hezheng.authapi.entity.SysUser;
import com.hezheng.authapi.service.SysRoleService;
import com.hezheng.authapi.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    //增删改查

    //模糊查询（多个）
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize, SysRole sysRole){

        //添加查询条件
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(sysRole.getRoleName()),"role_name",sysRole.getRoleName());

        //添加分页数据
        Page<SysRole> page = Page.of(pageNum,pageSize);
        List<SysRole> list = sysRoleService.list(page, queryWrapper);

        //组织返回数值
        Map<String,Object> map = new HashMap<>();
        map.put("total",page.getTotal());
        map.put("list",list);
        return Result.success(map);
    }
    //单个查询（根据id）
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id){
        return Result.success(sysRoleService.getById(id));
    }

    //添加
    @PostMapping
    public Result add(@RequestBody SysRole sysRole){
        sysRoleService.save(sysRole);
        return Result.success();
    }

    //修改
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody SysRole sysRole){
        sysRole.setId(id);
        sysRoleService.updateById(sysRole);
        return Result.success();
    }

    //删除（支持批量删除）
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable Long[] ids){
        sysRoleService.removeBatchByIds(Arrays.asList(ids));
        return Result.success();
    }

    //角色授权
    //前端传递的参数需要有两个：json对象
    // 1.角色编：id
    // 2.权限编号：permissionIds
    @PostMapping("/assign")
    public Result assign(@RequestBody SysRole sysRole){
        sysRoleService.assignPermission(sysRole.getId(),sysRole.getPermissionIds());
        return Result.success();
    }

    //导出
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysRole sysRole) throws IOException {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(sysRole.getRoleName()),"roleName",sysRole.getRoleName());

        List<SysRole> list = sysRoleService.list(queryWrapper);

        ExcelUtils.export(response,list,"角色数据");
    }

    //导入
    @PostMapping("/import")
    public Result importData(HttpServletResponse response,@RequestParam("file") MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        //调用Hutool中ExcelReader的readAll方法，将读取的数据封装成List集合
        List<SysRole> list = reader.readAll(SysRole.class);
        //批量保存到数据库
        Integer count = 0;
        for (SysRole item : list) {
            item.setId(null);
            sysRoleService.save(item);
            count ++;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("count",count);
        return Result.success(map);
    }
}
