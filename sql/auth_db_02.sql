/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : localhost:3306
 Source Schema         : auth_db_02

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 11/03/2026 11:17:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `permission_code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限编码',
  `permission_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限名称',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '描述',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父权限ID',
  `resource_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT 'menu' COMMENT '资源类型：menu-菜单，button-按钮',
  `path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '路径',
  `component` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图标',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_permission_code`(`permission_code` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_deleted`(`deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, 'system:management', '系统管理', '系统管理模块', NULL, 'menu', '/system', 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:46', 0);
INSERT INTO `sys_permission` VALUES (2, 'content:management', '内容管理', '内容管理模块', NULL, 'menu', '/content', 'Profile', 'document', '2025-11-10 16:19:34', '2025-11-12 17:26:02', 0);
INSERT INTO `sys_permission` VALUES (3, 'user:management', '用户管理', '用户管理菜单', 1, 'menu', 'user', 'User', 'User', '2025-11-10 16:19:34', '2025-11-13 23:51:09', 0);
INSERT INTO `sys_permission` VALUES (4, 'role:management', '角色管理', '角色管理菜单', 1, 'menu', 'role', 'Role', 'Grid', '2025-11-10 16:19:34', '2025-11-13 23:54:54', 0);
INSERT INTO `sys_permission` VALUES (5, 'permission:management', '权限管理', '权限管理菜单', 1, 'menu', 'permission', 'Permission', 'Promotion', '2025-11-10 16:19:34', '2025-11-13 23:55:08', 0);
INSERT INTO `sys_permission` VALUES (6, 'article:management', '文章管理', '文章管理菜单', 2, 'menu', 'article', 'Profile', 'article', '2025-11-10 16:19:34', '2025-11-13 15:08:48', 0);
INSERT INTO `sys_permission` VALUES (7, 'category:management', '分类管理', '分类管理菜单', 2, 'menu', 'category', 'Profile', 'Bicycle', '2025-11-10 16:19:34', '2025-11-13 15:08:52', 0);
INSERT INTO `sys_permission` VALUES (8, 'user:add', '新增用户', '新增用户按钮', 3, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (9, 'user:edit', '修改用户', '修改用户按钮', 3, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (10, 'user:delete', '删除用户', '删除用户按钮', 3, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (11, 'user:import', '导入用户', '导入用户按钮', 3, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (12, 'user:export', '导出用户', '导出用户按钮', 3, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (13, 'role:add', '新增角色', '新增角色按钮', 4, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (14, 'role:edit', '修改角色', '修改角色按钮', 4, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (15, 'role:delete', '删除角色', '删除角色按钮', 4, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (16, 'role:import', '导入角色', '导入角色按钮', 4, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (17, 'role:export', '导出角色', '导出角色按钮', 4, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (18, 'permission:add', '新增权限', '新增权限按钮', 5, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (19, 'permission:edit', '修改权限', '修改权限按钮', 5, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (20, 'permission:delete', '删除权限', '删除权限按钮', 5, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (21, 'permission:import', '导入权限', '导入权限按钮', 5, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (22, 'permission:export', '导出权限', '导出权限按钮', 5, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (23, 'article:add', '新增文章', '新增文章按钮', 6, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (24, 'article:edit', '修改文章', '修改文章按钮', 6, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (25, 'article:delete', '删除文章', '删除文章按钮', 6, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (26, 'article:import', '导入文章', '导入文章按钮', 6, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (27, 'article:export', '导出文章', '导出文章按钮', 6, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (28, 'category:add', '新增分类', '新增分类按钮', 7, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (29, 'category:edit', '修改分类', '修改分类按钮', 7, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (30, 'category:delete', '删除分类', '删除分类按钮', 7, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (31, 'category:import', '导入分类', '导入分类按钮', 7, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (32, 'category:export', '导出分类', '导出分类按钮', 7, 'button', NULL, 'Profile', 'Setting', '2025-11-10 16:19:34', '2025-11-13 23:53:06', 0);
INSERT INTO `sys_permission` VALUES (33, 'picture:list', '轮播图管理', '管理首页轮播图', 2, 'menu', '/caracoul', 'Profile', 'Upload', '2025-11-10 22:38:30', '2025-11-12 17:26:02', 1);
INSERT INTO `sys_permission` VALUES (34, 'category:list', '轮播图管理', '', 2, 'menu', '/lunbotu', 'Profile', 'Tools', '2025-11-11 09:54:06', '2025-11-12 17:26:02', 0);
INSERT INTO `sys_permission` VALUES (35, 'system:news', '新闻资讯管理', '', 2, 'menu', 'news', 'News', 'Basketball', '2025-11-17 14:52:51', '2025-11-17 14:52:51', 0);
INSERT INTO `sys_permission` VALUES (36, 'lunbotu', '轮播图管理', '', 2, 'menu', '/lunbotu', 'LunBoTu', 'Briefcase', '2025-11-17 15:33:28', '2025-11-17 15:33:28', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_code`(`role_code` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_deleted`(`deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin', '管理员', '系统管理员', 1, '2026-03-11 11:00:41', '2026-03-11 11:00:41', 0);
INSERT INTO `sys_role` VALUES (2, 'user', '普通用户', '普通用户角色', 1, '2026-03-11 11:00:41', '2026-03-11 11:00:41', 0);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_permission`(`role_id` ASC, `permission_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_permission_id`(`permission_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 1, 1);
INSERT INTO `sys_role_permission` VALUES (2, 1, 2);
INSERT INTO `sys_role_permission` VALUES (3, 1, 3);
INSERT INTO `sys_role_permission` VALUES (4, 1, 4);
INSERT INTO `sys_role_permission` VALUES (5, 1, 5);
INSERT INTO `sys_role_permission` VALUES (6, 1, 6);
INSERT INTO `sys_role_permission` VALUES (7, 1, 7);
INSERT INTO `sys_role_permission` VALUES (8, 1, 8);
INSERT INTO `sys_role_permission` VALUES (9, 1, 9);
INSERT INTO `sys_role_permission` VALUES (10, 1, 10);
INSERT INTO `sys_role_permission` VALUES (11, 1, 11);
INSERT INTO `sys_role_permission` VALUES (12, 1, 12);
INSERT INTO `sys_role_permission` VALUES (13, 1, 13);
INSERT INTO `sys_role_permission` VALUES (14, 1, 14);
INSERT INTO `sys_role_permission` VALUES (15, 1, 15);
INSERT INTO `sys_role_permission` VALUES (16, 1, 16);
INSERT INTO `sys_role_permission` VALUES (17, 1, 17);
INSERT INTO `sys_role_permission` VALUES (18, 1, 18);
INSERT INTO `sys_role_permission` VALUES (19, 1, 19);
INSERT INTO `sys_role_permission` VALUES (20, 1, 20);
INSERT INTO `sys_role_permission` VALUES (21, 1, 21);
INSERT INTO `sys_role_permission` VALUES (22, 1, 22);
INSERT INTO `sys_role_permission` VALUES (23, 1, 23);
INSERT INTO `sys_role_permission` VALUES (24, 1, 24);
INSERT INTO `sys_role_permission` VALUES (25, 1, 25);
INSERT INTO `sys_role_permission` VALUES (26, 1, 26);
INSERT INTO `sys_role_permission` VALUES (27, 1, 27);
INSERT INTO `sys_role_permission` VALUES (28, 1, 28);
INSERT INTO `sys_role_permission` VALUES (29, 1, 29);
INSERT INTO `sys_role_permission` VALUES (30, 1, 30);
INSERT INTO `sys_role_permission` VALUES (31, 1, 31);
INSERT INTO `sys_role_permission` VALUES (32, 1, 32);
INSERT INTO `sys_role_permission` VALUES (36, 1, 33);
INSERT INTO `sys_role_permission` VALUES (33, 1, 34);
INSERT INTO `sys_role_permission` VALUES (34, 1, 35);
INSERT INTO `sys_role_permission` VALUES (35, 1, 36);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码（MD5加密）',
  `nickname` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_deleted`(`deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 'admin@example.com', NULL, 1, '2026-03-11 11:00:41', '2026-03-11 11:00:41', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id` ASC, `role_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
