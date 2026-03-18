<template>
  <div class="password-modify-wrapper">
    <div class="password-modify-container">
      <div class="page-header">
        <h2>修改密码</h2>
        <p class="header-tip">请妥善保管您的账户密码</p>
      </div>
      <el-card class="password-modify-card" :body-style="{ padding: '32px 40px' }" shadow="never">
        <el-form
            :model="form"
            ref="formRef"
            label-width="90px"
            label-position="right"
            class="custom-form"
        >
          <el-form-item label="原密码" prop="password">
            <el-input
                type="password"
                v-model="form.password"
                placeholder="请输入当前密码"
                show-password
                clearable
            />
          </el-form-item>
          <el-form-item label="新密码" prop="newpassword">
            <el-input
                type="password"
                v-model="form.newpassword"
                placeholder="请输入新密码（6-20位字符）"
                show-password
                clearable
            />
          </el-form-item>
          <el-form-item label="确认密码" prop="repassword">
            <el-input
                type="password"
                v-model="form.repassword"
                placeholder="请再次输入新密码"
                show-password
                clearable
            />
          </el-form-item>
          <el-form-item class="form-footer">
            <el-button type="primary" @click="onSubmit" :loading="loading" class="submit-btn">
              确认修改
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { changePwd } from '@/api/login.js';
import { ElMessage } from "element-plus";

const form = ref({
  password: '',
  newpassword: '',
  repassword: ''
});
const loading = ref(false);
const formRef = ref(null);

const onSubmit = () => {
  loading.value = true;
  changePwd(form.value).then(res => {
    if (res.code == 200) {
      ElMessage.success("密码修改成功! 下次登录时生效");
      form.value = { password: '', newpassword: '', repassword: '' };
    } else {
      ElMessage.error("修改失败: " + (res.message || '未知错误'));
    }
  }).finally(() => {
    loading.value = false;
  });
};
</script>

<style scoped>
.password-modify-wrapper {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100%;
}

.password-modify-container {
  max-width: 680px;
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

.password-modify-card {
  border-radius: 8px;
  border: 1px solid #e6eaf0;
  background-color: #ffffff;
}

.password-modify-card :deep(.el-card__body) {
  padding: 32px 40px;
}

.custom-form {
  width: 100%;
}

:deep(.el-form-item) {
  margin-bottom: 28px;
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

.form-footer {
  margin-top: 40px;
  margin-bottom: 0;
  display: flex;
  justify-content: center;
}

.form-footer :deep(.el-form-item__content) {
  justify-content: center;
  margin-left: 0 !important;
}

.submit-btn {
  padding: 12px 48px;
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
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

.submit-btn:active {
  transform: translateY(0);
}

.submit-btn.is-loading {
  opacity: 0.8;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .password-modify-wrapper {
    padding: 16px;
  }

  .password-modify-card :deep(.el-card__body) {
    padding: 24px 20px;
  }

  .page-header h2 {
    font-size: 1.5rem;
  }

  .submit-btn {
    width: 100%;
  }
}
</style>