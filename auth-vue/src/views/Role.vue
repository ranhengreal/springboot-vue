<template>
  <div style="padding: 15px; height: 100%;">
    <el-card class="role-management-card" :body-style="{ padding: '20px', height: '100%' }">
      <template #header>
        <div class="card-header">
          <span class="card-title">角色管理</span>
          <el-tag type="info" effect="plain" size="small">共 {{ total }} 条记录</el-tag>
        </div>
      </template>

      <!-- 搜索表单 - 固定部分 -->
      <el-form :inline="true" :model="queryParam" class="search-form">
        <el-form-item label="角色名称">
          <el-input v-model="queryParam.roleName" placeholder="请输入角色名称" clearable class="search-input" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" icon="Search" class="search-btn">搜索</el-button>
          <el-button @click="handleReset" icon="RefreshLeft" class="reset-btn">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 - 固定部分 -->
      <div class="action-bar">
        <el-button
            v-has-permi="['role:add']"
            type="primary"
            icon="Plus"
            @click="handleAdd"
            class="action-btn add-btn"
        >添加</el-button>
        <el-button
            v-has-permi="['role:edit']"
            type="success"
            icon="EditPen"
            :disabled="selectIds.length != 1"
            @click="handleEdit"
            class="action-btn edit-btn"
        >编辑</el-button>
        <el-button
            v-has-permi="['role:delete']"
            type="danger"
            icon="Remove"
            @click="handleDelete"
            :disabled="selectIds.length == 0"
            class="action-btn delete-btn"
        >批量删除</el-button>
        <el-button
            v-has-permi="['role:export']"
            type="warning"
            icon="Download"
            @click="handleExport"
            class="action-btn export-btn"
        >批量导出</el-button>
        <el-upload
            style="margin-left: 10px;"
            v-has-permi="['role:import']"
            :show-file-list="false"
            :on-success="handleImportSuccess"
            :action="importUrl"
            :headers="uploadHeaders"
        >
          <el-button type="info" icon="Upload" class="action-btn import-btn">批量导入</el-button>
        </el-upload>
      </div>

      <!-- 表格区域 - 固定高度避免抖动 -->
      <div class="table-wrapper" :style="{ height: tableHeight + 'px' }">
        <!-- 加载中显示骨架屏 -->
        <div v-if="loading" class="skeleton-wrapper">
          <el-skeleton :rows="10" animated />
        </div>

        <!-- 表格内容 -->
        <el-table
            v-else
            :data="tableData"
            style="height: 100%; width: 100%;"
            @selection-change="handleSelectionChange"
            border
            height="100%"
            class="custom-table"
            stripe
        >
          <el-table-column type="selection" prop="id" width="55" fixed="left" class="selection-column" />
          <el-table-column prop="roleCode" label="角色编号" width="180" fixed="left" show-overflow-tooltip>
            <template #default="scope">
              <span class="role-code">{{ scope.row.roleCode }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="roleName" label="角色名称" width="180" show-overflow-tooltip>
            <template #default="scope">
              <span class="role-name">{{ scope.row.roleName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述" show-overflow-tooltip>
            <template #default="scope">
              <span class="role-description">{{ scope.row.description || '暂无描述' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80" align="center">
            <template #default="scope">
              <el-tag
                  v-if="scope.row.status==1"
                  type="success"
                  effect="light"
                  size="small"
                  class="status-tag success-tag"
              >
                <span class="dot success-dot"></span> 启用
              </el-tag>
              <el-tag
                  v-else
                  type="danger"
                  effect="light"
                  size="small"
                  class="status-tag danger-tag"
              >
                <span class="dot danger-dot"></span> 禁用
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220" fixed="right" align="center">
            <template #default="scope">
              <div class="action-buttons">
                <el-button
                    v-has-permi="['role:edit']"
                    size="small"
                    type="primary"
                    @click="handleEdit(scope.row)"
                    class="table-action-btn edit-btn"
                    icon="Edit"
                >编辑
                </el-button>
                <el-button
                    v-has-permi="['role:delete']"
                    size="small"
                    type="danger"
                    @click="handleDelete(scope.row)"
                    class="table-action-btn delete-btn"
                    icon="Delete"
                >
                  删除
                </el-button>
                <el-button size="small" type="warning" @click="handleAssign(scope.row)" class="table-action-btn assign-btn" icon="Setting">
                  授权
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 - 固定在底部 -->
      <div class="pagination-wrapper">
        <el-pagination
            v-model:current-page="queryParam.pageNum"
            v-model:page-size="queryParam.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            class="custom-pagination"
            background
        />
      </div>
    </el-card>

    <!-- 用户表单对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="title"
        width="600px"
        :before-close="handleClose"
        destroy-on-close
        class="custom-dialog"
    >
      <el-form :model="form" label-width="100px" :rules="rules" ref="formRef" status-icon class="custom-form">
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="form.roleCode" placeholder="请输入角色编码" class="form-input" clearable />
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" class="form-input" clearable />
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input v-model="form.description" placeholder="请对角色进行描述" class="form-input" type="textarea" :rows="3" clearable />
        </el-form-item>
        <el-form-item label="角色状态">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%" class="form-select">
            <el-option label="启用" :value="1" class="status-option">
              <span class="option-dot success-dot"></span> 启用
            </el-option>
            <el-option label="禁用" :value="0" class="status-option">
              <span class="option-dot danger-dot"></span> 禁用
            </el-option>
          </el-select>
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

    <!-- 角色授权对话框 -->
    <el-dialog
        v-model="assignVisible"
        title="角色授权"
        width="600px"
        :before-close="handleClose"
        destroy-on-close
        class="custom-dialog assign-dialog"
    >
      <div class="assign-content">
        <div class="assign-info">
          <el-alert
              :title="`正在为角色「${role.roleName || ''}」分配权限`"
              type="info"
              :closable="false"
              show-icon
              class="assign-alert"
          />
        </div>
        <el-tree
            :data="permissionList"
            ref="treeRef"
            show-checkbox
            node-key="id"
            :default-expanded-keys="[1,2]"
            :props="defaultProps"
            class="permission-tree"
            default-expand-all
        />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="assignVisible = false" class="cancel-btn">取 消</el-button>
          <el-button type="primary" @click="handleAssignSubmit" :loading="submitting" class="submit-btn">授 权</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { listRoles, deleteRoles, addRole, getRole, updateRole,assignPermission } from '@/api/role.js'
import { treelist,listPermissionByRoleId } from '@/api/permission.js'
import { ElMessage, ElMessageBox } from "element-plus"
import { download } from '@/util/request.js'

// 状态变量
const queryParam = ref({
  pageNum: 1,
  pageSize: 10,
})
const tableData = ref([])
const total = ref(0)
const selectIds = ref([])
const dialogVisible = ref(false)
const title = ref('添加角色')
const isAdd = ref(true)
const form = ref({})
const formRef = ref()
const submitting = ref(false)
const loading = ref(false) // 添加加载状态
const tableHeight = ref(400) // 表格高度
const assignVisible = ref(false)
const treeRef = ref();
const defaultProps = ref({
  children: 'children',
  label: 'permissionName',
})
const permissionList = ref([])
const role = ref({})//当前要授权的角色
// 上传配置
const importUrl = ref(import.meta.env.VITE_APP_API_BASE_URL + '/role/import')
const uploadHeaders = ref({})

// 表单验证规则
const rules = {
  roleName: [
    { required: true, message: '必须填写角色名称', trigger: 'blur' }
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
    const paginationHeight = 60 // 分页组件
    const paddingHeight = 60 // 内外边距总和

    const otherHeight = headerHeight + cardHeaderHeight + searchFormHeight +
        actionBarHeight + paginationHeight + paddingHeight

    const availableHeight = windowHeight - otherHeight
    tableHeight.value = Math.max(400, availableHeight) // 最小400px
  })
}

// 加载角色数据
const loadData = () => {
  loading.value = true
  listRoles(queryParam.value).then(res => {
    tableData.value = res.data.list
    total.value = res.data.total
  }).catch(error => {
    ElMessage.error('加载角色数据失败：' + error.message)
  }).finally(() => {
    loading.value = false
  })
}


// 导入成功回调
const handleImportSuccess = (res) => {
  if (res.code == 200) {
    ElMessage.success('导入成功，共导入' + res.data.count + '条数据')
    loadData()
  } else {
    ElMessage.error('导入失败：' + res.message)
  }
}

// 导出数据
const handleExport = () => {
  ElMessageBox.confirm('确定要导出角色数据吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    download('/role/export', queryParam.value)
  }).catch(() => {
  })
}

//角色授权
const handleAssign = (row) => {
  assignVisible.value = true
  role.value = row;
  treelist().then(res => {
    permissionList.value = res.data


    //加载角色拥有的权限，并选择el-tree的节点
    listPermissionByRoleId(row.id).then(r=>{
      r.data.forEach((item) => {
        treeRef.value.setChecked(item.permissionId, true, false);
      });
    })
  });
}
const handleAssignSubmit=()=>{
  //获取选择的权限节点
  let nodes = treeRef.value.getCheckedNodes(false, true);
  assignPermission({
    id: role.value.id,
    permissionIds: nodes.map(item => item.id)
  }).then(res=>{
    if(res.code == 200){
      ElMessage.success("授权成功！")
      assignVisible.value = false
    }else {
      ElMessage.error("授权失败！"+res.message)
    }
  })
}

// 添加角色
const handleAdd = () => {
  title.value = "添加角色"
  form.value = {};
  isAdd.value = true
  dialogVisible.value = true
  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

// 编辑角色
const handleEdit = (row) => {
  title.value = '修改角色'
  dialogVisible.value = true
  isAdd.value = false

  let id = row.id || selectIds.value[0]

  getRole(id).then(res => {
    form.value = res.data
  })
}

// 提交表单
const handleSubmit = () => {
  formRef.value.validate(valid => {
    if (valid) {
      submitting.value = true

      if (isAdd.value) {
        addRole(form.value).then(res => {
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
        updateRole(form.value.id, form.value).then(res => {
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

// 选择变化
const handleSelectionChange = (rows) => {
  selectIds.value = rows.map(item => item.id)
}

// 搜索
const handleSearch = () => {
  queryParam.value.pageNum = 1
  loadData()
}

// 重置
const handleReset = () => {
  queryParam.value.pageNum = 1;
  queryParam.value.roleName = '';
  loadData()
}

// 删除
const handleDelete = (row) => {
  const ids = row.id || selectIds.value
  if (!ids || (Array.isArray(ids) && ids.length === 0)) {
    ElMessage.warning('请选择要删除的数据')
    return
  }

  ElMessageBox.confirm("确定要删除该数据吗？", "提示", {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(() => {
    deleteRoles(ids).then(res => {
      if (res.code == 200) {
        ElMessage.success('删除数据成功')
        loadData()
        selectIds.value = []
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    }).catch(error => {
      ElMessage.error('删除失败：' + error.message)
    })
  }).catch(() => {
  })
}

// 分页大小变化
const handleSizeChange = () => {
  queryParam.value.pageNum = 1
  loadData()
}

// 当前页变化
const handleCurrentChange = () => {
  loadData()
}

// 关闭对话框前的确认
const handleClose = (done) => {
  if (formRef.value) {
    ElMessageBox.confirm('确定要关闭吗？未保存的数据将会丢失', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      done()
    }).catch(() => {
    })
  } else {
    done()
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
.role-management-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.role-management-card:hover {
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
  width: 220px;
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

.edit-btn:hover {
  box-shadow: 0 4px 12px rgba(103, 194, 58, 0.3);
}

.delete-btn:hover {
  box-shadow: 0 4px 12px rgba(245, 108, 108, 0.3);
}

.export-btn:hover {
  box-shadow: 0 4px 12px rgba(230, 162, 60, 0.3);
}

.import-btn:hover {
  box-shadow: 0 4px 12px rgba(144, 147, 153, 0.3);
}

/* 表格包装器样式 */
.table-wrapper {
  flex: 1;
  min-height: 400px;
  margin: 16px 0;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.02);
}

.table-wrapper:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
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

/* 角色编码样式 */
.role-code {
  font-family: 'Monaco', 'Consolas', monospace;
  font-weight: 500;
  color: #409eff;
  background: rgba(64, 158, 255, 0.1);
  padding: 2px 6px;
  border-radius: 4px;
}

/* 角色名称样式 */
.role-name {
  font-weight: 500;
  color: #2c3e50;
}

/* 角色描述样式 */
.role-description {
  color: #606266;
  font-size: 13px;
}

/* 状态标签样式 */
.status-tag {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  border-radius: 20px;
  font-weight: 500;
  border: none;
}

.success-tag {
  background: rgba(103, 194, 58, 0.1);
  color: #67c23a;
}

.danger-tag {
  background: rgba(245, 108, 108, 0.1);
  color: #f56c6c;
}

.dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.success-dot {
  background: #67c23a;
  box-shadow: 0 0 0 2px rgba(103, 194, 58, 0.2);
}

.danger-dot {
  background: #f56c6c;
  box-shadow: 0 0 0 2px rgba(245, 108, 108, 0.2);
}

/* 表格操作按钮样式 */
.action-buttons {
  display: flex;
  gap: 4px;
  justify-content: center;
}

.table-action-btn {
  border-radius: 16px;
  padding: 4px 8px;
  font-size: 12px;
  transition: all 0.2s ease;
}

.table-action-btn:hover {
  transform: translateY(-1px);
}

/* 分页样式 */
.pagination-wrapper {
  flex-shrink: 0;
  display: flex;
  justify-content: flex-end;
  padding: 16px 0 8px;
  border-top: 1px solid #f0f2f5;
  background: #fff;
}

.custom-pagination {
  --el-pagination-button-bg-color: #f5f7fa;
}

.custom-pagination :deep(.el-pager li) {
  border-radius: 20px;
  margin: 0 2px;
  transition: all 0.2s ease;
}

.custom-pagination :deep(.el-pager li:hover) {
  transform: translateY(-1px);
}

.custom-pagination :deep(.el-pager li.active) {
  background: linear-gradient(135deg, #409eff, #79bbff);
  box-shadow: 0 4px 8px rgba(64, 158, 255, 0.3);
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
  max-height: 60vh;
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

.form-input :deep(.el-input__wrapper) {
  border-radius: 8px;
  transition: all 0.2s ease;
}

.form-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #409eff inset;
}

.form-input :deep(.el-textarea__inner) {
  border-radius: 8px;
  transition: all 0.2s ease;
}

.form-input :deep(.el-textarea__inner:hover) {
  border-color: #409eff;
}

.form-select {
  border-radius: 8px;
}

.status-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.option-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 6px;
}

/* 授权对话框样式 */
.assign-dialog .assign-content {
  padding: 8px 0;
}

.assign-alert {
  margin-bottom: 20px;
  border-radius: 8px;
}

.permission-tree {
  max-height: 400px;
  overflow-y: auto;
  padding: 8px;
  background: #fafafa;
  border-radius: 8px;
}

.permission-tree :deep(.el-tree-node) {
  transition: all 0.2s ease;
}

.permission-tree :deep(.el-tree-node:hover) {
  background-color: #ecf5ff;
  border-radius: 4px;
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
  transition: all 0.2s ease;
  background: linear-gradient(135deg, #409eff, #79bbff);
  border: none;
}

.submit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
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
</style>