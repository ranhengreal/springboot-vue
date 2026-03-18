package com.hezheng.authapi.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hezheng.authapi.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    /**
     * 用户登录
     * @param user
     * @return
     */
    public SysUser login(SysUser user);

    public void updatePassword(Long userId,String password);
}
