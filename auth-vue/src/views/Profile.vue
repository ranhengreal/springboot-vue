<template>
  <div class="profile-wrapper">
    <div class="profile-container">
      <div class="page-header">
        <h2>个人中心</h2>
        <p class="header-tip">管理您的个人信息和账户设置</p>
      </div>

      <el-card class="profile-card" :body-style="{ padding: '32px 40px' }" shadow="never">
        <el-form
            :model="form"
            label-width="100px"
            ref="formRef"
            status-icon
            class="custom-form"
        >
          <el-form-item label="用户名" prop="username">
            <el-input
                v-model="form.username"
                placeholder="请输入用户名"
                class="form-input"
                clearable
            />
            <span class="input-tip">用户名可修改，请谨慎操作</span>
          </el-form-item>

          <el-form-item label="昵称" prop="nickname">
            <el-input
                v-model="form.nickname"
                placeholder="请输入昵称"
                class="form-input"
                clearable
            />
          </el-form-item>

          <el-form-item label="电子邮件" prop="email">
            <el-input
                v-model="form.email"
                placeholder="请输入电子邮件"
                class="form-input"
                clearable
            />
          </el-form-item>

          <el-form-item label="电话" prop="phone">
            <el-input
                v-model="form.phone"
                placeholder="请输入电话"
                class="form-input"
                clearable
            />
          </el-form-item>

          <el-form-item label="角色" prop="roleIds">
            <div class="role-tags">
              <el-tag
                  v-for="item in roleList"
                  :key="item.id"
                  class="role-tag"
                  type="info"
                  effect="plain"
                  size="large"
              >
                {{item.roleName}}
              </el-tag>
              <span v-if="!roleList.length" class="empty-text">暂无角色</span>
            </div>
          </el-form-item>

          <el-form-item label="用户状态" prop="status">
            <el-tag
                :type="form.status == 1 ? 'success' : 'danger'"
                effect="light"
                size="large"
                class="status-tag"
            >
              <span class="status-dot" :class="{ 'success': form.status == 1, 'danger': form.status != 1 }"></span>
              {{ form.status == 1 ? '启用' : '禁用' }}
            </el-tag>
          </el-form-item>

          <el-divider class="form-divider" />

          <el-form-item class="form-footer">
            <el-button
                type="primary"
                @click="handleSubmitForm"
                class="submit-btn"
                :loading="loading"
            >
              确认修改
            </el-button>
            <el-button @click="resetForm" class="reset-btn">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useUserStore } from '@/store/user.js';
import { listRoles } from '@/api/role.js';
import { ElMessage } from "element-plus";

const userStore = useUserStore();
const roleList = ref([]);
const form = ref({});
const loading = ref(false);
const originalForm = ref({}); // 保存原始数据用于重置

const handleSubmitForm = async () => {
  loading.value = true;
  try {
    let result = await userStore.updateUser(form.value);
    if (result) {
      ElMessage.success("个人信息修改成功！");
      // 更新成功后保存当前数据作为新的原始数据
      originalForm.value = { ...form.value };
    } else {
      ElMessage.error("修改失败！");
    }
  } catch (error) {
    ElMessage.error("修改失败：" + (error.message || '未知错误'));
  } finally {
    loading.value = false;
  }
};

// 重置表单
const resetForm = () => {
  form.value = { ...originalForm.value };
  ElMessage.info("已重置为原始数据");
};

onMounted(() => {
  // 复制用户信息到form
  form.value = { ...userStore.userInfo };
  originalForm.value = { ...userStore.userInfo };

  // 加载角色信息
  listRoles({ pageSize: 50 }).then(res => {
    roleList.value = res.data.list.filter(item =>
        userStore.userInfo.roleIds && userStore.userInfo.roleIds.includes(item.id)
    );
  });
});
</script>

<style scoped>
.profile-wrapper {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100%;
}

.profile-container {
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 1.8rem;
  font-weight: 500;
  color: #1e293b;
  margin: 0 0 8px 0;
  letter-spacing: 0.3px;
}

.header-tip {
  font-size: 0.95rem;
  color: #64748b;
  margin: 0;
  padding-left: 4px;
}

.profile-card {
  border-radius: 8px;
  border: 1px solid #e6eaf0;
  background-color: #ffffff;
}

.profile-card :deep(.el-card__body) {
  padding: 32px 40px;
}

.custom-form {
  width: 100%;
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #334e68;
  font-size: 0.95rem;
  padding-right: 16px;
}

:deep(.el-input__wrapper) {
  border-radius: 6px;
  box-shadow: 0 0 0 1px #d9e2ef inset;
  transition: box-shadow 0.2s ease;
  padding: 4px 12px;
  background-color: #fafbfc;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #a0b8d4 inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px #409EFF inset;
}

:deep(.el-input__inner) {
  height: 40px;
  font-size: 0.95rem;
  color: #1e293b;
}

:deep(.el-input__inner::placeholder) {
  color: #9aa9b9;
  font-size: 0.9rem;
}

:deep(.el-input__clear) {
  font-size: 16px;
  color: #94a3b8;
}

:deep(.el-input__clear:hover) {
  color: #409EFF;
}

/* 移除之前的禁用样式，现在所有输入框都是可编辑状态 */

.input-tip {
  margin-left: 12px;
  font-size: 0.85rem;
  color: #94a3b8;
}

.role-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
}

.role-tag {
  padding: 0 16px;
  height: 32px;
  line-height: 30px;
  border-radius: 4px;
  background-color: #f0f7ff;
  border-color: #b3d8ff;
  color: #409EFF;
  font-weight: 400;
}

.role-tag :deep(.el-tag__content) {
  font-size: 0.95rem;
}

.empty-text {
  color: #94a3b8;
  font-size: 0.95rem;
}

.status-tag {
  padding: 0 16px;
  height: 32px;
  line-height: 30px;
  border-radius: 4px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.status-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: currentColor;
}

.status-dot.success {
  background-color: #67c23a;
}

.status-dot.danger {
  background-color: #f56c6c;
}

.form-divider {
  margin: 32px 0 24px 0;
  border-color: #e9eef3;
}

.form-footer {
  margin-top: 16px;
  margin-bottom: 0;
  display: flex;
  justify-content: center;
  gap: 16px;
}

.form-footer :deep(.el-form-item__content) {
  justify-content: center;
  margin-left: 0 !important;
}

.submit-btn {
  padding: 12px 40px;
  border-radius: 6px;
  font-weight: 500;
  font-size: 1rem;
  letter-spacing: 0.5px;
  background-color: #409eff;
  border-color: #409eff;
  transition: all 0.2s ease;
}

.submit-btn:hover {
  background-color: #a0cfff;
  border-color: #a0cfff;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.submit-btn:active {
  transform: translateY(0);
}

.reset-btn {
  padding: 12px 40px;
  border-radius: 6px;
  font-weight: 500;
  font-size: 1rem;
  border: 1px solid #d9e2ef;
  background-color: #ffffff;
  color: #64748b;
  transition: all 0.2s ease;
}

.reset-btn:hover {
  background-color: #f8fafd;
  border-color: #a0b8d4;
  color: #334e68;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .profile-wrapper {
    padding: 16px;
  }

  .profile-card :deep(.el-card__body) {
    padding: 24px 20px;
  }

  .page-header h2 {
    font-size: 1.5rem;
  }

  .input-tip {
    display: block;
    margin-left: 0;
    margin-top: 6px;
  }

  .form-footer {
    flex-direction: column;
    gap: 12px;
  }

  .submit-btn,
  .reset-btn {
    width: 100%;
  }
}
</style>