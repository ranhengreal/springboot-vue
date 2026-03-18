package com.hezheng.authapi.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@TableName("sys_role" )
public class SysRole  implements Serializable {

	private static final long serialVersionUID =  4637606561309990539L;

	/**
	 * 主键ID
	 */
   	@TableId(value = "id",type = IdType.AUTO)
	private Long id;

	/**
	 * 角色编码
	 */
   	@TableField("role_code" )
	private String roleCode;

	/**
	 * 角色名称
	 */
   	@TableField("role_name" )
	private String roleName;

	/**
	 * 描述
	 */
   	@TableField("description" )
	private String description;

	/**
	 * 状态：0-禁用，1-启用
	 */
   	@TableField("status" )
	private Integer status;

	/**
	 * 创建时间
	 */
   	@TableField("create_time" )
	private Date createTime;

	/**
	 * 更新时间
	 */
   	@TableField("update_time" )
	private Date updateTime;

	/**
	 * 是否删除：0-未删除，1-已删除
	 */
   	@TableField("deleted" )
	@TableLogic
	private Integer deleted;

	@TableField(exist = false)
	private List<Long> permissionIds;//权限编号的集合
}
