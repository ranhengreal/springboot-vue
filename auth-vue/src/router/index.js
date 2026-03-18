import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import {usePermissionStore} from '@/store/permission.js'
import {useUserStore} from "@/store/user.js";

//静态路由
export const constantRoutes = [
  {
    path: '/login',
    name: 'login',
    hidden: true,
    component: Login
  },
  {
    path: '/notfound',
    name: 'notfound',
    hidden: true,
    component: ()=>import('../views/404.vue')
  },
  {
    path: '/:pathMatch(.*)',
    hidden: true,
    redirect:'/notfound'
  },
  {
    path: '/',
    name: 'main',
    component: ()=>import('../layout/MainLayout.vue'),
    redirect:'/dashboard',
    children:[
      {
        path:'dashboard',
        name:'dashboard',
        meta:{
          title:'首页',
          icon:'HomeFilled'
        },
        component: ()=>import('../views/Dashboard.vue')
      },
      {
        path:'profile',
        name:'profile',
        hidden: true,
        component: ()=>import('../views/Profile.vue')
      },
      {
        path:'changePwd',
        name:'changePwd',
        hidden: true,
        component: ()=>import('../views/ChangePwd.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoutes,
})
//定义白名单
const whiteList = ["/login"];
//路由前置导航守卫
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore();
  if (!userStore.token && !whiteList.includes(to.path)) {
    next("/login");
  }
  if (to.path != "/login") {
    const permissionStore = usePermissionStore();
    if (permissionStore.routers.length == 0) {
      console.log("加载动态路由.....");
      //加载动态路由
      const routes = await permissionStore.generateRoutes();
      routes.forEach((route) => {
        router.addRoute(route);
      });
      if (!to.redirectedFrom) {
        next({ ...to, replace: true });
      } else {
        next();
      }
    } else {
      next();
    }
  } else {
    next();
  }
});
export default router
