package com.hezheng.authapi.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.hezheng.authapi.common.CustomException;
import com.hezheng.authapi.common.Result;
import com.hezheng.authapi.entity.SysUser;
import com.hezheng.authapi.service.SysUserService;
import com.hezheng.authapi.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public Result login(@RequestBody SysUser sysUser){
        SysUser user = sysUserService.login(sysUser);
        return Result.success(user);
    }

    @PostMapping("/changePwd")
    public Result changePwd(HttpServletRequest request, @RequestBody Map<String, String> map) {
        //查询登录用户的信息
        String token = request.getHeader("token");
        Long userId = jwtUtil.getUserIdFromToken(token);
        SysUser user = sysUserService.getById(userId);

        if (!user.getPassword().equals(DigestUtil.md5Hex(map.get("password").getBytes()))){
            throw new CustomException("原密码错误");
        }
        sysUserService.updatePassword(userId,DigestUtil.md5Hex(map.get("newpassword").getBytes()));
        return Result.success();
    }
}
