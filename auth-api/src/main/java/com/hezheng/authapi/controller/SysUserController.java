package com.hezheng.authapi.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hezheng.authapi.entity.SysUser;
import com.hezheng.authapi.service.SysUserService;
import com.hezheng.authapi.common.Result;
import com.hezheng.authapi.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    //增删改查

    //模糊查询（多个）
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize, SysUser sysUser){

        //添加查询条件
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(sysUser.getUsername()),"username",sysUser.getUsername());
        queryWrapper.like(StrUtil.isNotBlank(sysUser.getNickname()),"nickname",sysUser.getNickname());

        queryWrapper.inSql(sysUser.getRoleIds() != null&&sysUser.getRoleIds().size()>0,
                "id",
                "SELECT user_id from sys_user_role WHERE role_id in("+ ArrayUtil.join(sysUser.getRoleIds().toArray(),",")+")");

        //添加分页数据
        Page<SysUser> page = Page.of(pageNum,pageSize);
        List<SysUser> list = sysUserService.list(page, queryWrapper);

        //组织返回数值
        Map<String,Object> map = new HashMap<>();
        map.put("total",page.getTotal());
        map.put("list",list);
        return Result.success(map);
    }
    //单个查询（根据id）
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id){
        return Result.success(sysUserService.getById(id));
    }

    //添加
    @PostMapping
    public Result add(@RequestBody SysUser sysUser){
        //对密码进行MD5加密处理
        sysUser.setPassword(DigestUtil.md5Hex(sysUser.getPassword().getBytes()));
        sysUserService.save(sysUser);
        return Result.success();
    }

    //修改
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody SysUser sysUser){
        sysUser.setId(id);
        sysUserService.updateById(sysUser);
        return Result.success();
    }

    //删除（支持批量删除）
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable Long[] ids){
        sysUserService.removeBatchByIds(Arrays.asList(ids));
        return Result.success();
    }

    //导出
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysUser sysUser) throws IOException {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(sysUser.getUsername()),"username",sysUser.getUsername());
        queryWrapper.like(StrUtil.isNotBlank(sysUser.getNickname()),"nickname",sysUser.getNickname());
        queryWrapper.inSql(sysUser.getRoleIds() != null&&sysUser.getRoleIds().size()>0,
                "id",
                "SELECT user_id from sys_user_role WHERE role_id in("+ ArrayUtil.join(sysUser.getRoleIds().toArray(),",")+")");


        List<SysUser> list = sysUserService.list(queryWrapper);

        ExcelUtils.export(response,list,"用户数据");
    }

    //导入
    @PostMapping("/import")
    public Result importData(HttpServletResponse response,@RequestParam("file") MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        //调用Hutool中ExcelReader的readAll方法，将读取的数据封装成List集合
        List<SysUser> list = reader.readAll(SysUser.class);
        //批量保存到数据库
        Integer count = 0;
        for (SysUser item : list) {
            item.setId(null);
            sysUserService.save(item);
            count ++;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("count",count);
        return Result.success(map);
    }
}
