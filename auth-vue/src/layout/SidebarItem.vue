<!-- components/Layout/SidebarItem.vue -->
<template>
  <div v-if="!item.hidden" class="sidebar-item-wrapper">
    <template
        v-if="
        hasOneShowingChild(item.children, item) &&
        (!onlyOneChild.children || onlyOneChild.noShowingChildren)
      "
    >
      <el-menu-item :index="resolvePath(onlyOneChild.path)" class="custom-menu-item">
        <el-icon v-if="onlyOneChild.meta?.icon" class="menu-icon">
          <component :is="onlyOneChild.meta.icon" />
        </el-icon>
        <template #title>
          <span class="menu-title">{{ onlyOneChild.meta?.title }}</span>
        </template>
      </el-menu-item>
    </template>

    <el-sub-menu v-else :index="resolvePath(item.path)" class="custom-sub-menu" popper-class="custom-submenu-popper">
      <template #title>
        <el-icon v-if="item.meta?.icon" class="menu-icon">
          <component :is="item.meta.icon" />
        </el-icon>
        <span class="menu-title">{{ item.meta?.title }}</span>
        <el-icon class="arrow-icon" v-if="!isCollapse">
          <ArrowRight />
        </el-icon>
      </template>
      <sidebar-item
          v-for="child in item.children"
          :key="child.path"
          :item="child"
          :base-path="resolvePath(child.path)"
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import { computed, defineProps, inject } from "vue";
import { resolve } from "path-browserify";
import { ArrowRight } from '@element-plus/icons-vue';

// 注入折叠状态
const isCollapse = inject('isCollapse', false);

const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
  basePath: {
    type: String,
    required: true,
  },
});

const onlyOneChild = computed(() => {
  if (!props.item.children) {
    props.item.children = [];
  }
  const children = props.item.children.filter((item) => !item.hidden) || [];

  if (children.length === 1 && !children[0].children) {
    return {
      ...children[0],
      path: resolve(props.basePath, children[0].path),
      noShowingChildren: true,
    };
  }

  return {
    ...props.item,
    path: props.basePath,
    noShowingChildren: !children || children.length === 0,
  };
});

const hasOneShowingChild = (children = [], parent) => {
  if (!children) {
    children = [];
  }
  const showingChildren = children.filter((item) => !item.hidden);

  if (showingChildren.length === 1) {
    return true;
  }

  if (showingChildren.length === 0) {
    return true;
  }
  return false;
};

const resolvePath = (routePath) => {
  return resolve(props.basePath, routePath);
};
</script>

<style scoped>
.sidebar-item-wrapper {
  width: 100%;
}

/* 自定义菜单项样式 */
.custom-menu-item {
  position: relative;
  margin: 4px 8px !important;
  width: calc(100% - 16px) !important;
  border-radius: 8px !important;
  transition: all 0.3s ease !important;
}

.custom-menu-item:hover {
  background: rgba(255, 255, 255, 0.1) !important;
  transform: translateX(4px);
}

/* 激活菜单项样式 */
.custom-menu-item.is-active {
  background: linear-gradient(90deg, rgba(255, 208, 75, 0.2) 0%, rgba(255, 208, 75, 0.05) 100%) !important;
  position: relative;
}

.custom-menu-item.is-active::before {
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

/* 菜单图标样式 */
.menu-icon {
  color: rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
  margin-right: 12px;
  font-size: 18px;
}

.custom-menu-item:hover .menu-icon {
  color: #ffd04b;
  transform: scale(1.1);
}

.custom-menu-item.is-active .menu-icon {
  color: #ffd04b;
}

/* 菜单标题样式 */
.menu-title {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.custom-menu-item:hover .menu-title {
  color: #ffd04b;
}

/* 自定义子菜单样式 */
.custom-sub-menu {
  margin: 4px 8px !important;
  width: calc(100% - 16px) !important;
  border-radius: 8px !important;
  transition: all 0.3s ease !important;
}

:deep(.custom-sub-menu .el-sub-menu__title) {
  height: 56px !important;
  line-height: 56px !important;
  padding-left: 20px !important;
  border-radius: 8px !important;
  transition: all 0.3s ease !important;
}

:deep(.custom-sub-menu .el-sub-menu__title:hover) {
  background: rgba(255, 255, 255, 0.1) !important;
  transform: translateX(4px);
}

/* 子菜单激活状态 */
:deep(.custom-sub-menu.is-active .el-sub-menu__title) {
  color: #ffd04b !important;
}

:deep(.custom-sub-menu.is-active .menu-icon) {
  color: #ffd04b !important;
}

/* 箭头图标样式 */
.arrow-icon {
  margin-left: auto;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  transition: all 0.3s ease;
}

:deep(.custom-sub-menu.is-opened) .arrow-icon {
  transform: rotate(90deg);
  color: #ffd04b;
}

:deep(.custom-sub-menu .el-sub-menu__title:hover) .arrow-icon {
  color: #ffd04b;
}

/* 子菜单内容区域样式 */
:deep(.custom-sub-menu .el-menu--inline) {
  background-color: #032441 !important;
  border-radius: 8px;
  margin: 4px 0 4px 20px;
  padding: 4px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  position: relative;
}

/* 子菜单项样式 */
:deep(.custom-sub-menu .el-menu--inline .el-menu-item) {
  background-color: transparent !important;
  margin: 2px 8px !important;
  width: calc(100% - 16px) !important;
  border-radius: 6px !important;
  padding-left: 32px !important;
  height: 44px !important;
  line-height: 44px !important;
}

:deep(.custom-sub-menu .el-menu--inline .el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.1) !important;
  transform: translateX(4px);
}

:deep(.custom-sub-menu .el-menu--inline .el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(255, 208, 75, 0.15) 0%, rgba(255, 208, 75, 0.02) 100%) !important;
  position: relative;
}

:deep(.custom-sub-menu .el-menu--inline .el-menu-item.is-active::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 2px;
  height: 16px;
  background: linear-gradient(to bottom, #ffd04b, #ffb84d);
  border-radius: 0 2px 2px 0;
}

/* 三级菜单缩进 */
:deep(.custom-sub-menu .el-menu--inline .el-menu--inline .el-menu-item) {
  padding-left: 48px !important;
}

/* 折叠状态下的样式优化 */
:deep(.el-menu--collapse) .custom-menu-item,
:deep(.el-menu--collapse) .custom-sub-menu {
  margin: 4px !important;
  width: calc(100% - 8px) !important;
}

:deep(.el-menu--collapse) .custom-menu-item .menu-icon,
:deep(.el-menu--collapse) .custom-sub-menu .menu-icon {
  margin-right: 0 !important;
  font-size: 20px;
}

:deep(.el-menu--collapse) .custom-sub-menu .el-sub-menu__title {
  padding: 0 !important;
  justify-content: center;
}

/* 折叠状态下的下拉菜单样式 */
:deep(.custom-submenu-popper) {
  background: #032441 !important;
  border: none !important;
  border-radius: 8px !important;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3) !important;
  padding: 4px !important;
  margin-left: 8px !important;
}

:deep(.custom-submenu-popper .el-menu-item) {
  background-color: transparent !important;
  border-radius: 6px !important;
  margin: 2px 4px !important;
  width: calc(100% - 8px) !important;
  color: #ffffff !important;
  height: 44px !important;
  line-height: 44px !important;
  transition: all 0.2s ease !important;
}

:deep(.custom-submenu-popper .el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.1) !important;
  transform: translateX(4px);
  color: #ffd04b !important;
}

:deep(.custom-submenu-popper .el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(255, 208, 75, 0.15) 0%, rgba(255, 208, 75, 0.02) 100%) !important;
  color: #ffd04b !important;
  position: relative;
}

:deep(.custom-submenu-popper .el-menu-item.is-active::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 2px;
  height: 16px;
  background: linear-gradient(to bottom, #ffd04b, #ffb84d);
  border-radius: 0 2px 2px 0;
}

:deep(.custom-submenu-popper .el-menu-item .el-icon) {
  color: rgba(255, 255, 255, 0.8);
  margin-right: 8px;
}

:deep(.custom-submenu-popper .el-menu-item:hover .el-icon) {
  color: #ffd04b;
}

/* 动画效果 */
.custom-menu-item-enter-active,
.custom-menu-item-leave-active,
.custom-sub-menu-enter-active,
.custom-sub-menu-leave-active {
  transition: all 0.3s ease;
}

/* 分割线效果 */
:deep(.custom-sub-menu .el-menu--inline)::before {
  content: '';
  position: absolute;
  left: 12px;
  top: 8px;
  bottom: 8px;
  width: 1px;
  background: linear-gradient(to bottom, transparent, rgba(255, 255, 255, 0.1), transparent);
}
</style>