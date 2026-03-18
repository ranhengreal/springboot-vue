package com.hezheng.authapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hezheng.authapi.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface SysUserMapper extends BaseMapper<SysUser> {
    @Update("update sys_user set password = #{password} where id = #{userId}")
    public void updatePassword(@Param("userId") Long userId, @Param("password")String password);
}
