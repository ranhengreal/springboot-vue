import {defineStore} from 'pinia'
import {login} from '@/api/login.js'
import {ElMessage} from 'element-plus'
import {updateUser, getUser} from '@/api/user.js'
import {usePermissionStore} from "@/store/permission.js";

// 安全的 JSON 解析函数
function safeJSONParse(item, defaultValue = null) {
    if (!item || item === 'undefined' || item === 'null') {
        return defaultValue
    }
    try {
        return JSON.parse(item)
    } catch (error) {
        console.error('JSON解析失败:', error)
        return defaultValue
    }
}

export const useUserStore = defineStore('user', {
    state: () => ({
        // userInfo 是对象，需要用 JSON.parse
        userInfo: safeJSONParse(localStorage.getItem("userInfo")),
        // token 是字符串，直接获取，不需要 JSON.parse
        token: localStorage.getItem("token") || null,
        // 修复：处理 permissions 可能为 null 的情况
        permissions: (() => {
            const perms = localStorage.getItem("permissions")
            return perms ? perms.split("_") : []
        })(),
    }),
    actions: {
        setPermissions(permissions) {
            // 修复：处理 permissions 为空的情况
            if (permissions && Array.isArray(permissions) && permissions.length > 0) {
                localStorage.setItem("permissions", permissions.join("_"))
                this.permissions = permissions
            } else {
                localStorage.removeItem("permissions")
                this.permissions = []
            }
        },

        setUserInfo(userInfo) {
            if (userInfo) {
                localStorage.setItem("userInfo", JSON.stringify(userInfo))
            } else {
                localStorage.removeItem("userInfo")
            }
            this.userInfo = userInfo
        },

        setToken(token) {
            if (token) {
                // token 是字符串，直接存储
                localStorage.setItem("token", token)
            } else {
                localStorage.removeItem("token")
            }
            this.token = token
        },

        async login(data) {
            try {
                const res = await login(data)

                if (res.code == 200) {
                    // 假设 res.data 包含用户信息和 token
                    // 根据您的实际接口结构调整
                    const { userInfo, token, permissions } = res.data

                    // 保存用户信息
                    if (userInfo) {
                        this.setUserInfo(userInfo)
                    } else {
                        // 如果返回的是整个用户对象，直接保存
                        this.setUserInfo(res.data)
                    }

                    // 保存 token
                    if (token) {
                        this.setToken(token)
                    } else if (res.data.token) {
                        this.setToken(res.data.token)
                    }

                    // 保存权限
                    if (permissions) {
                        this.setPermissions(permissions)
                    } else if (res.data.permissions) {
                        this.setPermissions(res.data.permissions)
                    }

                    ElMessage.success('登录成功')
                    return true
                } else {
                    ElMessage.error(res.message || '登录失败')
                    return false
                }
            } catch (error) {
                console.error('登录请求失败:', error)
                ElMessage.error('登录请求失败：' + (error.message || '未知错误'))
                return false
            }
        },

        logout() {
            this.setUserInfo(null)
            this.setToken(null)
            this.setPermissions(null)  // 使用 setPermissions 处理清空
            localStorage.removeItem("userInfo")
            localStorage.removeItem("token")
            localStorage.removeItem("permissions")
            ElMessage.success('已退出登录')

            // 清空路由菜单
            const permissionStore = usePermissionStore()
            permissionStore.setRouters([]);
        },

        async updateUser(data) {
            delete data.roleIds
            delete data.password
            let res = await updateUser(data.id, data)
            if (res.code == 200) {
                let u = await getUser(data.id);
                this.setUserInfo(u.data)
                return true
            }
            return false;
        }
    },
})