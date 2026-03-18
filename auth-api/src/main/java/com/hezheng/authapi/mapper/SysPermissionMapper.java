package com.hezheng.authapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hezheng.authapi.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    @Select("SELECT permission_id from sys_role_permission where role_id in (SELECT role_id from sys_user_role where user_id = #{userId})")
    public List<Long> selectPermissionIdsByUserId(@Param("userId") Long userId);
}
