package com.hezheng.authapi.entity;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.annotation.PropIgnore;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@TableName("sys_user")
public class SysUser implements Serializable {

 private static final long serialVersionUID = 3334727334840684396L;

  @TableId(value = "id",type = IdType.AUTO)
  @Alias(value = "编号")
  private Long id;

  @TableField("username")
  private String username;

  @TableField("password")
  private String password;

  @TableField("nickname")
  private String nickname;

  @TableField("email")
  private String email;

  @TableField("phone")
  private String phone;

  @TableField("status")
  private long status;

  @TableField("create_time")
  private java.sql.Timestamp createTime;

  @TableField("update_time")
  private java.sql.Timestamp updateTime;

  @TableField("deleted")
  @TableLogic
  private long deleted;

  @TableField(exist = false)
  @PropIgnore
  private List<SysRole> roleList;//用户拥有的角色

  @TableField(exist = false)
  @PropIgnore
  private List<Long> roleIds = new ArrayList<>();//角色编号

  @TableField(exist = false)
  private String token;

  @TableField(exist = false)
  private List<String> permissions;//用户拥有的权限编码
}
