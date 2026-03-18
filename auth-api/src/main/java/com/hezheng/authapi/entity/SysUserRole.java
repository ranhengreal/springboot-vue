package com.hezheng.authapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import lombok.Data;


/**
 * @Description  
 * @Author  hezheng
 * @Date 2026-03-14 
 */

@Data
@TableName ("sys_user_role" )
public class SysUserRole  implements Serializable {

	private static final long serialVersionUID =  2548686709033672695L;

	/**
	 * 主键ID
	 */
   	@TableId(value = "id",type = IdType.AUTO)
	private Long id;

	/**
	 * 用户ID
	 */
   	@TableField("user_id" )
	private Long userId;

	/**
	 * 角色ID
	 */
   	@TableField("role_id" )
	private Long roleId;

}
