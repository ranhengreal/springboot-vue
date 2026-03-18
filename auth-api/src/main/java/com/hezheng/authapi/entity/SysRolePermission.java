package com.hezheng.authapi.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description  
 * @Author  hezheng
 * @Date 2026-03-13 
 */

@Data
@TableName("sys_role_permission")
public class SysRolePermission  implements Serializable {

	private static final long serialVersionUID =  7034762554085568683L;

	/**
	 * 主键ID
	 */
   	@TableId(value = "id",type = IdType.AUTO)
	private Long id;

	/**
	 * 角色ID
	 */
   	@TableField("role_id")
	private Long roleId;

	/**
	 * 权限ID
	 */
   	@TableField("permission_id")
	private Long permissionId;

}
