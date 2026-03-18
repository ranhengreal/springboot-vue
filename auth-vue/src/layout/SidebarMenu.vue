<template>
  <div class="sidebar-menu-container">
    <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        router
        class="sidebar-menu"
        active-text-color="#ffd04b"
        background-color="#042E55"
        text-color="#ffffff"
        :collapse-transition="true"
        unique-opened
    >
      <sidebar-item
          v-for="route in menuRoutes"
          :key="route.path"
          :item="route"
          :base-path="route.path"
      />
    </el-menu>

    <!-- 折叠时的底部提示 -->
    <div class="menu-footer" v-if="isCollapse">
      <el-tooltip content="展开菜单" placement="right">
        <div class="collapse-tip">
          <el-icon><Expand /></el-icon>
        </div>
      </el-tooltip>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, provide } from "vue";
import { useRoute } from "vue-router";
import SidebarItem from "@/layout/SidebarItem.vue";
import { usePermissionStore } from "@/store/permission.js";
import { Expand } from '@element-plus/icons-vue';

const menuRoutes = ref([]);
const route = useRoute();
const permissionStore = usePermissionStore();

// 定义属性 - 这里没有 activeMenu
const props = defineProps({
  isCollapse: {
    type: Boolean,
    default: false,
  },
});

// 提供折叠状态给子组件
provide('isCollapse', props.isCollapse);

// 计算当前激活的菜单 - 只声明一次
const activeMenu = computed(() => {
  const { meta, path } = route;
  return meta.activeMenu || path;
});

onMounted(() => {
  menuRoutes.value = permissionStore.routers;
});
</script>

<style scoped>
.sidebar-menu-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  background-color: #042E55;
}

.sidebar-menu {
  border-right: none !important;
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  transition: all 0.3s ease;
  background-color: #042E55;
}

/* 自定义滚动条 */
.sidebar-menu::-webkit-scrollbar {
  width: 4px;
}

.sidebar-menu::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
}

.sidebar-menu::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.3);
}

.sidebar-menu::-webkit-scrollbar-track {
  background: transparent;
}

/* 菜单项样式优化 */
:deep(.el-menu-item) {
  position: relative;
  transition: all 0.3s ease;
  margin: 4px 8px;
  width: calc(100% - 16px);
  border-radius: 8px;
  padding-left: 20px !important;
}

/* 菜单项悬停效果 */
:deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.1) !important;
  transform: translateX(4px);
}

/* 激活菜单项样式 */
:deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(255, 208, 75, 0.2) 0%, rgba(255, 208, 75, 0.05) 100%) !important;
  position: relative;
}

/* 激活菜单项左侧装饰条 */
:deep(.el-menu-item.is-active::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background: linear-gradient(to bottom, #ffd04b, #ffb84d);
  border-radius: 0 3px 3px 0;
  box-shadow: 0 2px 8px rgba(255, 208, 75, 0.4);
}

/* 子菜单样式 */
:deep(.el-sub-menu__title) {
  transition: all 0.3s ease;
  margin: 4px 8px;
  width: calc(100% - 16px);
  border-radius: 8px;
  padding-left: 20px !important;
}

:deep(.el-sub-menu__title:hover) {
  background: rgba(255, 255, 255, 0.1) !important;
  transform: translateX(4px);
}

/* 子菜单图标样式 */
:deep(.el-sub-menu__title .el-icon) {
  color: rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
}

:deep(.el-sub-menu__title:hover .el-icon) {
  color: #ffd04b;
  transform: scale(1.1);
}

/* 菜单项图标样式 */
:deep(.el-menu-item .el-icon) {
  color: rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
  margin-right: 12px;
}

:deep(.el-menu-item:hover .el-icon) {
  color: #ffd04b;
  transform: scale(1.1);
}

:deep(.el-menu-item.is-active .el-icon) {
  color: #ffd04b;
}

/* 折叠状态样式优化 */
:deep(.el-menu--collapse) {
  width: 60px !important;
}

:deep(.el-menu--collapse .el-menu-item) {
  margin: 4px;
  width: calc(100% - 8px);
  padding-left: 0 !important;
  justify-content: center;
}

:deep(.el-menu--collapse .el-sub-menu__title) {
  margin: 4px;
  width: calc(100% - 8px);
  padding-left: 0 !important;
  justify-content: center;
}

:deep(.el-menu--collapse .el-menu-item .el-icon) {
  margin-right: 0;
  font-size: 20px;
}

:deep(.el-menu--collapse .el-sub-menu__title .el-icon) {
  margin-right: 0;
  font-size: 20px;
}

/* 折叠状态下拉菜单样式 */
:deep(.el-menu--collapse .el-sub-menu .el-menu) {
  background-color: #032441 !important;
  border-radius: 8px;
  padding: 4px 0;
  margin-left: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

:deep(.el-menu--collapse .el-sub-menu .el-menu .el-menu-item) {
  background-color: transparent !important;
  margin: 2px 8px;
  width: calc(100% - 16px);
  border-radius: 6px;
}

/* 菜单分组标题样式 */
:deep(.el-menu-item-group__title) {
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
  padding-left: 28px;
  margin-top: 8px;
}

/* 折叠时的底部提示 */
.menu-footer {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  background-color: #032441;
}

.collapse-tip {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  transition: all 0.3s ease;
}

.collapse-tip:hover {
  background: rgba(255, 208, 75, 0.2);
  color: #ffd04b;
  transform: rotate(180deg);
}

/* 折叠状态下的动画 */
.sidebar-menu:deep(.el-menu-item),
.sidebar-menu:deep(.el-sub-menu__title) {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 添加渐变背景效果 */
.sidebar-menu-container::after {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 1px;
  height: 100%;
  background: linear-gradient(to bottom, transparent, rgba(255, 255, 255, 0.1), transparent);
}

/* 展开/折叠过渡动画 */
.sidebar-menu-enter-active,
.sidebar-menu-leave-active {
  transition: width 0.3s ease;
}

/* 菜单项文字样式 */
:deep(.el-menu-item span) {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.5px;
}

:deep(.el-sub-menu__title span) {
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.5px;
}

/* 子菜单缩进优化 */
:deep(.el-menu--inline .el-menu-item) {
  padding-left: 48px !important;
}

:deep(.el-menu--collapse .el-menu--inline .el-menu-item) {
  padding-left: 24px !important;
}

/* 菜单项之间的分隔线 */
:deep(.el-menu-item:not(:last-child)) {
  margin-bottom: 2px;
}

/* 激活状态的子菜单标题 */
:deep(.el-sub-menu.is-active .el-sub-menu__title) {
  color: #ffd04b !important;
}

:deep(.el-sub-menu.is-active .el-sub-menu__title .el-icon) {
  color: #ffd04b;
}
</style>