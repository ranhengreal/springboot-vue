package com.hezheng.authapi.entity;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @Description  
 * @Author  hezheng
 * @Date 2026-03-13 
 */
@Data
@TableName("sys_permission" )
public class SysPermission  implements Serializable {

	private static final long serialVersionUID =  3760492647246854493L;

	/**
	 * 主键ID
	 */
   	@TableId(value = "id",type = IdType.AUTO)
	private Long id;

	/**
	 * 权限编码
	 */
   	@TableField("permission_code" )
	private String permissionCode;

	/**
	 * 权限名称
	 */
   	@TableField("permission_name" )
	private String permissionName;

	/**
	 * 描述
	 */
   	@TableField("description" )
	private String description;

	/**
	 * 父权限ID
	 */
   	@TableField("parent_id" )
	private Long parentId;

	/**
	 * 资源类型：menu-菜单，button-按钮
	 */
   	@TableField("resource_type" )
	private String resourceType;

	/**
	 * 路径
	 */
   	@TableField("path" )
	private String path;

	/**
	 * 组件路径
	 */
   	@TableField("component" )
	private String component;

	/**
	 * 图标
	 */
   	@TableField("icon" )
	private String icon;

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
    private List<SysPermission> children;
}
