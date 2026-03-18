<template>
  <el-container class="layout-container-demo" style="height: 100vh">
    <el-aside
        :width="isCollapse ? '60px' : '200px'"
        style="background-color: #042E55; transition: width 200ms linear"
    >
      <el-scrollbar>
        <div class="logo">
          <img src="@/assets/img/logo.png" alt="">
          <h3 style="font-weight: lighter" v-show="!isCollapse">后台管理系统</h3>
        </div>
        <!-- 修复：使用 :is-collapse 进行变量绑定 -->
        <SidebarMenu :is-collapse="isCollapse"></SidebarMenu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 16px;display: flex;justify-content: space-between;align-items: center;background-color:white;border-bottom: 1px solid #ddd">
        <!-- 优化折叠按钮 -->
        <el-icon
            @click="handleCollapse"
            style="margin-right: 15px; cursor: pointer; font-size: 20px; transition: transform 0.3s;"
            :class="{ 'rotate-icon': isCollapse }"
        >
          <Operation />
        </el-icon>

        <el-breadcrumb separator="/" style="flex:1;">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <!-- 添加 :key 属性 -->
          <el-breadcrumb-item v-for="item in breadcrumbList" :key="item.path">{{item.meta.title}}</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="toolbar">
          <el-dropdown>
            <span style="display: flex; align-items: center; outline: none; cursor: pointer;">
              <img src="@/assets/img/avatar.jpg" alt="" style="width: 30px;height: 30px;border-radius: 50%;margin-right: 8px;">
              <span>{{userStore.userInfo?.nickname}}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goto('/profile')">个人中心</el-dropdown-item>
                <el-dropdown-item @click="goto('/changePwd')">修改密码</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">注销登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script lang="ts" setup>
import {onMounted, ref, watch} from 'vue'
import { Operation } from '@element-plus/icons-vue'
import {useUserStore} from '@/store/user'
import {ElMessage, ElMessageBox} from "element-plus"
import {useRouter, useRoute} from 'vue-router'
import SidebarMenu from "@/layout/SidebarMenu.vue";

const userStore = useUserStore();
const isCollapse = ref(false);
const router = useRouter();
const route = useRoute();
const breadcrumbList = ref([]);

const updateBreadcrumb = () => {
  breadcrumbList.value = route.matched.filter((item) => item.meta?.title);
};

onMounted(() => {
  updateBreadcrumb();
});

watch(
    () => route.path,
    () => {
      updateBreadcrumb();
    },
    { immediate: true },
);

const handleLogout = () => {
  ElMessageBox.confirm("确定要注销登录吗？", "提示", { type: "warning" }).then(() => {
    userStore.logout();
    router.push("login");
  });
};

const handleCollapse = () => {
  isCollapse.value = !isCollapse.value;
};

const goto = (path) => {
  router.push(path);
};
</script>

<style>
/* 全局样式，去除body和html的默认边距 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}
</style>

<style scoped>
div.logo {
  display: flex;
  align-items: center;
  justify-content: space-around;
  height: 60px;
  background-color: #042E55;
  color: #fff;
  border-bottom: 1px solid #bbb;
  padding: 0 8px;
}

div.logo img {
  width: 35px;
  height: 35px;
}

/* 确保el-aside完全贴边 */
.layout-container-demo .el-aside {
  color: var(--el-text-color-primary);
  margin: 0;
  padding: 0;
}

.layout-container-demo .el-menu {
  border-right: none;
  margin: 0;
  padding: 0;
}

.layout-container-demo .el-main {
  padding: 0;
}

.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}

/* 移除el-scrollbar的默认内边距 */
.layout-container-demo .el-scrollbar {
  padding: 0;
}

/* 确保el-menu-item也完全贴边 */
.layout-container-demo .el-menu-item {
  padding-left: 20px !important;
}

/* 折叠按钮旋转动画 */
.rotate-icon {
  transform: rotate(90deg);
}

/* 面包屑样式优化 */
:deep(.el-breadcrumb__inner) {
  color: #606266;
}

:deep(.el-breadcrumb__inner:hover) {
  color: #409eff;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #2c3e50;
  font-weight: 500;
}
</style>