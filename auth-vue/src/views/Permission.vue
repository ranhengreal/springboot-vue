<template>
  <div style="padding: 15px; height: 100%;">
    <el-card class="permission-management-card" :body-style="{ padding: '20px', height: '100%' }">
      <template #header>
        <div class="card-header">
          <span class="card-title">权限管理</span>
        </div>
      </template>

      <!-- 搜索表单 - 固定部分 -->
      <el-form :inline="true" :model="queryParam" class="search-form">
        <el-form-item label="权限名称">
          <el-input v-model="queryParam.permissionName" placeholder="请输入权限名称" clearable class="search-input" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" icon="Search" class="search-btn">搜索</el-button>
          <el-button @click="handleReset" icon="RefreshLeft" class="reset-btn">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 - 固定部分 -->
      <div class="action-bar">
        <el-button
            v-has-permi="['permission:add']"
            type="primary"
            icon="Plus"
            @click="handleAdd"
            class="action-btn add-btn"
        >添加</el-button>
        <!--暂未添加该功能 -->
        <el-button
            v-has-permi="['permission:export']"
            type="warning"
            icon="Download"
            v-if="false"
            @click="handleExport"
            class="action-btn export-btn">批量导出</el-button>
        <el-upload
            style="margin-left: 10px;"
            v-has-permi="['permission:import']"
            :show-file-list="false"
            :on-success="handleImportSuccess"
            :action="importUrl"
            :headers="uploadHeaders"
        >
          <!--暂未添加该功能 -->
          <el-button
              type="info"
              icon="Upload"
              v-if="false"
              class="action-btn import-btn"
          >批量导入</el-button>
        </el-upload>
      </div>

      <!-- 表格区域 - 允许水平滚动 -->
      <div class="table-wrapper"
           :style="{ height: tableHeight + 'px' }"
           :class="{ 'no-border': !hasData }"
           style="overflow-x: auto;">

        <!-- 加载中显示骨架屏 -->
        <div v-if="loading" class="skeleton-wrapper">
          <el-skeleton :rows="10" animated />
        </div>

        <!-- 表格内容 - 设置最小宽度确保所有列都能显示 -->
        <el-table
            v-else
            :data="tableData"
            style="width: 100%; min-width: 1200px;"
            :border="hasData"
            height="100%"
            class="custom-table"
            stripe
            row-key="id"
            :default-expand-all="expandAll"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        >
          <!-- 权限名称 -->
          <el-table-column prop="permissionName" label="权限名称" width="160" fixed="left" show-overflow-tooltip>
            <template #default="scope">
              <div class="permission-name-cell">
      <span class="permission-name" :class="{ 'is-menu': scope.row.resourceType === 'menu', 'is-button': scope.row.resourceType === 'button' }">
        <el-icon v-if="scope.row.icon" class="permission-icon"><component :is="scope.row.icon" /></el-icon>
        <!-- 使用 v-html 渲染高亮文本 -->
        <span v-html="highlightText(scope.row.permissionName, highlightKeyword)"></span>
      </span>
              </div>
            </template>
          </el-table-column>

          <!-- 权限编码 -->
          <el-table-column prop="permissionCode" label="权限编码" width="140" show-overflow-tooltip>
            <template #default="scope">
              <el-tag size="small" class="code-tag">{{ scope.row.permissionCode }}</el-tag>
            </template>
          </el-table-column>

          <!-- 资源类型 -->
          <el-table-column prop="resourceType" label="资源类型" width="80" align="center">
            <template #default="scope">
              <el-tag
                  :type="scope.row.resourceType === 'menu' ? 'primary' : 'success'"
                  size="small"
                  effect="light"
                  class="type-tag"
              >
                {{ scope.row.resourceType === 'menu' ? '菜单' : '按钮' }}
              </el-tag>
            </template>
          </el-table-column>

          <!-- 图标 -->
          <el-table-column prop="icon" label="图标" width="60" align="center">
            <template #default="scope">
              <el-icon v-if="scope.row.icon" class="table-icon" size="18">
                <component :is="scope.row.icon" />
              </el-icon>
              <span v-else class="no-icon">-</span>
            </template>
          </el-table-column>

          <!-- 路径 -->
          <el-table-column prop="path" label="路径" width="150" show-overflow-tooltip>
            <template #default="scope">
              <span class="path">{{ scope.row.path || '-' }}</span>
            </template>
          </el-table-column>

          <!-- 组件路径 -->
          <el-table-column prop="component" label="组件路径" width="160" show-overflow-tooltip>
            <template #default="scope">
              <span class="component">{{ scope.row.component || '-' }}</span>
            </template>
          </el-table-column>

          <!-- 描述列 - 增加宽度 -->
          <el-table-column
              prop="description"
              label="描述"
              width="220"
              show-overflow-tooltip
          >
            <template #default="scope">
              <el-tooltip
                  v-if="scope.row.description && scope.row.description.length > 25"
                  :content="scope.row.description"
                  placement="top"
                  effect="light"
              >
                <span class="description">{{ scope.row.description.slice(0, 25) }}...</span>
              </el-tooltip>
              <span v-else class="description">{{ scope.row.description || '暂无描述' }}</span>
            </template>
          </el-table-column>

          <!-- 操作列 - 增加宽度确保按钮不重叠 -->
          <el-table-column label="操作" width="200" fixed="right" align="center">
            <template #default="scope">
              <div class="action-buttons">
                <el-button
                    v-has-permi="['permission:edit']"
                    size="small"
                    type="primary"
                    @click="handleEdit(scope.row)"
                    class="table-action-btn edit-btn"
                    icon="Edit"
                    plain
                >
                  编辑
                </el-button>
                <el-button
                    v-has-permi="['permission:delete']"
                    size="small"
                    type="danger"
                    @click="handleDelete(scope.row)"
                    class="table-action-btn delete-btn"
                    icon="Delete"
                    plain
                >
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>

          <!-- 空数据模板 -->
          <template #empty>
            <el-empty description="暂无权限数据" :image-size="100">
              <el-button
                  v-has-permi="['permission:add']"
                  type="primary"
                  @click="handleAdd"
              >添加权限</el-button>
            </el-empty>
          </template>
        </el-table>
      </div>
    </el-card>

    <!-- 权限表单对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="title"
        width="700px"
        :before-close="handleClose"
        destroy-on-close
        class="custom-dialog"
    >
      <el-form :model="form" label-width="100px" :rules="rules" ref="formRef" status-icon class="custom-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="上级菜单" prop="parentId">
              <el-tree-select
                  v-model="form.parentId"
                  :data="tableData"
                  :props="treeProps"
                  check-strictly
                  node-key="id"
                  :render-after-expand="false"
                  placeholder="请选择上级菜单"
                  clearable
                  class="form-tree-select"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资源类型" prop="resourceType">
              <el-select v-model="form.resourceType" placeholder="请选择资源类型" class="form-select" clearable>
                <el-option label="菜单" value="menu" class="type-option">
                  <el-icon class="option-icon"><Menu /></el-icon> 菜单
                </el-option>
                <el-option label="按钮" value="button" class="type-option">
                  <el-icon class="option-icon"><SwitchButton /></el-icon> 按钮
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限名称" prop="permissionName">
              <el-input v-model="form.permissionName" placeholder="请输入权限名称" class="form-input" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限编码" prop="permissionCode">
              <el-input v-model="form.permissionCode" placeholder="请输入权限编码" class="form-input" clearable />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="路径" prop="path" v-if="form.resourceType === 'menu'">
              <el-input v-model="form.path" placeholder="请输入路由路径" class="form-input" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组件路径" prop="component" v-if="form.resourceType === 'menu'">
              <el-input v-model="form.component" placeholder="请输入组件路径" class="form-input" clearable />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="图标" prop="icon">
              <el-icon-picker v-model="form.icon" class="form-icon-picker" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="描述" prop="description">
          <el-input
              v-model="form.description"
              placeholder="请对权限进行描述"
              class="form-input"
              type="textarea"
              :rows="3"
              clearable
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting" class="submit-btn">
            {{ isAdd ? '添 加' : '保 存' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick, computed } from 'vue'
import { listPermissions, deletePermissions, addPermission, getPermission, updatePermission } from '@/api/permission.js'
import ElIconPicker from "@/components/ElIconPicker.vue";
import { ElMessage, ElMessageBox } from "element-plus"
import { download } from '@/util/request.js'
import { Menu, SwitchButton } from '@element-plus/icons-vue'

// 状态变量
const treeProps = {
  label: 'permissionName',
  children: 'children',
  value: 'id'
}

const queryParam = ref({
  pageNum: 1,
  pageSize: 10,
  permissionName: ''
})
const tableData = ref([])
const dialogVisible = ref(false)
const title = ref('添加权限')
const isAdd = ref(true)
const form = ref({
  parentId: null,
  permissionName: '',
  permissionCode: '',
  description: '',
  resourceType: 'menu',
  path: '',
  component: '',
  icon: '',
  sort: 1
})
const formRef = ref()
const submitting = ref(false)
const loading = ref(false)
const tableHeight = ref(400)

// 添加高亮和展开控制变量
const highlightKeyword = ref('') // 存储搜索关键词用于高亮
const expandAll = ref(false) // 控制是否全部展开，默认不展开

// 添加计算属性判断是否有数据
const hasData = computed(() => tableData.value.length > 0)

// 上传配置
const importUrl = ref(import.meta.env.VITE_APP_API_BASE_URL + '/permission/import')
const uploadHeaders = ref({})

// 表单验证规则
const rules = {
  permissionName: [
    { required: true, message: '必须填写权限名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  permissionCode: [
    { required: true, message: '必须填写权限编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_:]+$/, message: '权限编码只能包含字母、数字、下划线和冒号', trigger: 'blur' }
  ],
  resourceType: [
    { required: true, message: '请选择资源类型', trigger: 'change' }
  ]
}

// 计算表格高度
const calculateTableHeight = () => {
  nextTick(() => {
    const windowHeight = window.innerHeight
    // 计算其他元素高度
    const headerHeight = 60 // 页面头部
    const cardHeaderHeight = 60 // 卡片头部
    const searchFormHeight = 80 // 搜索表单
    const actionBarHeight = 50 // 操作按钮栏
    const paddingHeight = 60 // 内外边距总和

    const otherHeight = headerHeight + cardHeaderHeight + searchFormHeight +
        actionBarHeight + paddingHeight

    const availableHeight = windowHeight - otherHeight
    tableHeight.value = Math.max(500, availableHeight)
  })
}

// 加载权限数据
const loadData = () => {
  loading.value = true
  listPermissions(queryParam.value).then(res => {
    tableData.value = res.data || []
  }).catch(error => {
    ElMessage.error('加载权限数据失败：' + error.message)
    tableData.value = []
  }).finally(() => {
    loading.value = false
  })
}

// 导入成功回调
const handleImportSuccess = (res) => {
  if (res.code == 200) {
    ElMessage.success('导入成功，共导入' + (res.data?.count || 0) + '条数据')
    loadData()
  } else {
    ElMessage.error('导入失败：' + (res.message || '未知错误'))
  }
}

// 导出数据
const handleExport = () => {
  ElMessageBox.confirm('确定要导出权限数据吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    download('/permission/export', queryParam.value)
  }).catch(() => {})
}

// 添加权限
const handleAdd = () => {
  title.value = "添加权限"
  form.value = {
    parentId: null,
    permissionName: '',
    permissionCode: '',
    description: '',
    resourceType: 'menu',
    path: '',
    component: '',
    icon: '',
    sort: 1
  }
  isAdd.value = true
  dialogVisible.value = true
  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

// 编辑权限
const handleEdit = (row) => {
  title.value = '修改权限'
  dialogVisible.value = true
  isAdd.value = false

  getPermission(row.id).then(res => {
    form.value = { ...res.data }
  }).catch(error => {
    ElMessage.error('获取权限信息失败：' + error.message)
    dialogVisible.value = false
  })
}

// 提交表单
const handleSubmit = () => {
  formRef.value.validate(valid => {
    if (valid) {
      submitting.value = true

      if (isAdd.value) {
        addPermission(form.value).then(res => {
          if (res.code == 200) {
            ElMessage.success("添加成功")
            dialogVisible.value = false
            loadData()
          } else {
            ElMessage.error(res.message || "添加失败")
          }
        }).catch(error => {
          ElMessage.error('添加失败：' + error.message)
        }).finally(() => {
          submitting.value = false
        })
      } else {
        updatePermission(form.value.id, form.value).then(res => {
          if (res.code == 200) {
            ElMessage.success("修改成功")
            dialogVisible.value = false
            loadData()
          } else {
            ElMessage.error(res.message || "修改失败")
          }
        }).catch(error => {
          ElMessage.error('修改失败：' + error.message)
        }).finally(() => {
          submitting.value = false
        })
      }
    }
  })
}

// 搜索
const handleSearch = () => {
  queryParam.value.pageNum = 1
  highlightKeyword.value = queryParam.value.permissionName || '' // 保存搜索关键词
  expandAll.value = !!queryParam.value.permissionName // 有搜索词时展开全部
  loadData()
}

// 重置
const handleReset = () => {
  queryParam.value = {
    pageNum: 1,
    pageSize: 10,
    permissionName: ''
  }
  highlightKeyword.value = '' // 清空高亮
  expandAll.value = false // 重置时收起全部
  loadData()
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除权限「${row.permissionName}」吗？`, "提示", {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(() => {
    deletePermissions(row.id).then(res => {
      if (res.code == 200) {
        ElMessage.success('删除数据成功')
        loadData()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    }).catch(error => {
      ElMessage.error('删除失败：' + error.message)
    })
  }).catch(() => {})
}

// 关闭对话框前的确认
const handleClose = (done) => {
  if (formRef.value && Object.keys(form.value).length > 0) {
    ElMessageBox.confirm('确定要关闭吗？未保存的数据将会丢失', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      done()
    }).catch(() => {})
  } else {
    done()
  }
}

// 高亮文本
const highlightText = (text, keyword) => {
  if (!keyword || !text) return text
  try {
    const reg = new RegExp(keyword.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'gi')
    return text.replace(reg, match => `<span class="highlight">${match}</span>`)
  } catch (e) {
    return text
  }
}

// 窗口大小变化处理
const handleResize = () => {
  calculateTableHeight()
}

onMounted(() => {
  calculateTableHeight()
  window.addEventListener('resize', handleResize)
  loadData()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
/* 卡片样式优化 */
.permission-management-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}
/* 高亮样式 */
:deep(.highlight) {
  background-color: #ffeb3b;
  color: #d32f2f;
  padding: 0 2px;
  border-radius: 3px;
  font-weight: 600;
}

.permission-management-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

:deep(.el-card__header) {
  padding: 16px 24px;
  border-bottom: 1px solid #f0f2f5;
  background: linear-gradient(to right, #fafafa, #ffffff);
  flex-shrink: 0;
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
}

:deep(.el-card__body) {
  height: calc(100% - 60px);
  display: flex;
  flex-direction: column;
  padding: 20px !important;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  position: relative;
  padding-left: 12px;
}

.card-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 18px;
  background: linear-gradient(to bottom, #409eff, #79bbff);
  border-radius: 2px;
}

/* 搜索表单样式 */
.search-form {
  flex-shrink: 0;
  margin-bottom: 16px;
  padding: 16px;
  background-color: #f8fafd;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.search-form:hover {
  background-color: #f0f4fa;
}

.search-input {
  width: 250px;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 20px;
  transition: all 0.3s ease;
}

.search-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #409eff inset;
}

.search-btn {
  border-radius: 20px;
  padding: 8px 20px;
  transition: all 0.3s ease;
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.reset-btn {
  border-radius: 20px;
  padding: 8px 20px;
  transition: all 0.3s ease;
}

.reset-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 操作按钮栏样式 */
.action-bar {
  flex-shrink: 0;
  display: flex;
  margin-bottom: 16px;
  gap: 8px;
}

.action-btn {
  border-radius: 20px;
  padding: 8px 16px;
  transition: all 0.3s ease;
  font-weight: 500;
}

.action-btn:hover {
  transform: translateY(-2px);
}

.add-btn:hover {
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.export-btn:hover {
  box-shadow: 0 4px 12px rgba(230, 162, 60, 0.3);
}

.import-btn:hover {
  box-shadow: 0 4px 12px rgba(144, 147, 153, 0.3);
}

/* 表格包装器样式 - 允许水平滚动 */
.table-wrapper {
  flex: 1;
  min-height: 500px;
  margin: 16px 0;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow-x: auto;
  overflow-y: hidden;
  transition: all 0.3s ease;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.02);
}

.table-wrapper:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
}

/* 无数据时隐藏边框 */
.table-wrapper.no-border {
  border: none;
  box-shadow: none;
}

.skeleton-wrapper {
  height: 100%;
  padding: 20px;
  background: #fff;
}

/* 自定义表格样式 */
.custom-table {
  --el-table-border-color: #f0f2f5;
}

.custom-table :deep(.el-table__header) {
  background: linear-gradient(to bottom, #f8fafd, #f0f4fa);
}

.custom-table :deep(.el-table__header th) {
  font-weight: 600;
  color: #2c3e50;
  background-color: transparent;
}

.custom-table :deep(.el-table__row) {
  transition: all 0.2s ease;
}

.custom-table :deep(.el-table__row:hover) {
  background-color: #ecf5ff !important;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

/* 权限名称样式 */
.permission-name-cell {
  display: flex;
  align-items: center;
}

.permission-name {
  font-weight: 500;
  padding: 2px 8px;
  border-radius: 4px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.permission-name.is-menu {
  color: #409eff;
  background: rgba(64, 158, 255, 0.1);
}

.permission-name.is-button {
  color: #67c23a;
  background: rgba(103, 194, 58, 0.1);
}

.permission-icon {
  font-size: 16px;
}

/* 权限编码标签 */
.code-tag {
  font-family: 'Monaco', 'Consolas', monospace;
  border-radius: 12px;
}

/* 资源类型标签 */
.type-tag {
  border-radius: 12px;
  padding: 2px 8px;
}

/* 表格图标 */
.table-icon {
  color: #409eff;
}

.no-icon {
  color: #999;
  font-size: 12px;
}

/* 路径样式 */
.path, .component {
  font-family: 'Monaco', 'Consolas', monospace;
  color: #606266;
  font-size: 12px;
  background: #f5f7fa;
  padding: 2px 6px;
  border-radius: 4px;
}

/* 描述文本样式 */
.description {
  color: #606266;
  font-size: 13px;
  line-height: 1.5;
  display: inline-block;
  max-width: 100%;
  word-break: break-word;
}

/* 表格操作按钮样式 */
.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: nowrap;
}

.table-action-btn {
  border-radius: 16px;
  padding: 4px 8px;
  font-size: 12px;
  transition: all 0.2s ease;
  min-width: 58px;
}

.table-action-btn:hover {
  transform: translateY(-1px);
}

/* 对话框样式 */
.custom-dialog {
  border-radius: 12px;
  overflow: hidden;
}

.custom-dialog :deep(.el-dialog__header) {
  background: linear-gradient(to right, #f8fafd, #ffffff);
  padding: 20px 24px;
  border-bottom: 1px solid #f0f2f5;
  margin: 0;
}

.custom-dialog :deep(.el-dialog__title) {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

.custom-dialog :deep(.el-dialog__body) {
  padding: 24px;
  max-height: 70vh;
  overflow-y: auto;
}

.custom-dialog :deep(.el-dialog__footer) {
  padding: 16px 24px;
  border-top: 1px solid #f0f2f5;
  background: #f8fafd;
}

/* 表单样式 */
.custom-form {
  margin-top: 8px;
}

.form-input :deep(.el-input__wrapper),
.form-tree-select :deep(.el-input__wrapper),
.form-select :deep(.el-select__wrapper) {
  border-radius: 8px;
  transition: all 0.2s ease;
}

.form-input :deep(.el-input__wrapper:hover),
.form-tree-select :deep(.el-input__wrapper:hover),
.form-select :deep(.el-select__wrapper:hover) {
  box-shadow: 0 0 0 1px #409eff inset;
}

.form-input-number {
  width: 100%;
}

.form-input-number :deep(.el-input-number__increase),
.form-input-number :deep(.el-input-number__decrease) {
  border-radius: 0 8px 8px 0;
}

.form-icon-picker {
  width: 100%;
}

.form-icon-picker :deep(.el-input__wrapper) {
  border-radius: 8px;
}

/* 类型选项样式 */
.type-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.option-icon {
  font-size: 16px;
}

/* 对话框按钮样式 */
.cancel-btn {
  border-radius: 20px;
  padding: 8px 24px;
  transition: all 0.2s ease;
}

.cancel-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.submit-btn {
  border-radius: 20px;
  padding: 8px 24px;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #409eff, #79bbff);
  border: none;
  color: white;
}

.submit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
}

.submit-btn:active {
  transform: translateY(1px);
}

/* 滚动条美化 */
:deep(::-webkit-scrollbar) {
  width: 8px;
  height: 8px;
}

:deep(::-webkit-scrollbar-thumb) {
  background: linear-gradient(135deg, #ddd, #ccc);
  border-radius: 4px;
  transition: all 0.2s ease;
}

:deep(::-webkit-scrollbar-thumb:hover) {
  background: linear-gradient(135deg, #ccc, #bbb);
}

:deep(::-webkit-scrollbar-track) {
  background: #f5f5f5;
  border-radius: 4px;
}

/* 树形表格缩进优化 */
:deep(.el-table .el-table__indent) {
  padding-left: 20px;
}

:deep(.el-table .el-table__placeholder) {
  width: 20px;
}
</style>