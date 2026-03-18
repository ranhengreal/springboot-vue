import { defineStore } from "pinia";
import { getRouters } from "@/api/permission.js";
import { constantRoutes } from "@/router/index.js";

export const usePermissionStore = defineStore("permission", {
    state: () => ({
        routers: [],
    }),
    actions: {
        setRouters(routes) {
            this.routers = routes;
        },
        //生成路由
        generateRoutes() {
            return new Promise(async (resolve) => {
                const accessRouters = await getRouters();
                const array = new Array();
                //转换格式，将返回的权限菜单转成前端路由所需要的格式
                accessRouters.data.forEach((item) => {
                    let tem = {
                        path: item.path,
                        name: item.name,
                        meta: {
                            title: item.permissionName, //标题文字
                            icon: item.icon, //图标
                        },
                        component: () => import("../layout/MainLayout.vue"),
                        children: item.children.map((child) => {
                            return {
                                path: child.path,
                                name: child.name,
                                meta: {
                                    title: child.permissionName,
                                    icon: child.icon,
                                },
                                component: () => import("../views/" + child.component + ".vue"),
                            };
                        }),
                    };
                    array.push(tem);
                });
                const routes = [...constantRoutes, ...array];
                this.setRouters(routes);
                resolve(array);
            });
        },
    },
});