<template>
  <div class="login-container">
    <div class="login-box">
      <!-- 左侧装饰区域 - 仅在较大屏幕显示 -->
      <div class="login-left">
        <div class="left-content">
          <img src="@/assets/img/logo.png" alt="logo" class="logo-img">
          <h2 class="system-title">通用后台管理系统</h2>
          <p class="system-desc">专业 · 高效 · 安全</p>
          <div class="feature-list">
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>权限精细化管理</span>
            </div>
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>数据可视化分析</span>
            </div>
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>多维度安全防护</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧登录表单区域 -->
      <div class="login-right">
        <div class="welcome-text">
          <h2>欢迎回来</h2>
          <p>请登录您的账户</p>
        </div>

        <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="rules"
            class="login-form"
        >
          <el-form-item prop="username">
            <el-input
                v-model="loginForm.username"
                placeholder="请输入用户名"
                size="large"
                :prefix-icon="User"
                clearable
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                :prefix-icon="Lock"
                show-password
                clearable
                @keyup.enter="handleLogin"
            />
          </el-form-item>

          <div class="form-options">
            <el-checkbox v-model="rememberPassword">记住密码</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码？</el-link>
          </div>

          <el-form-item>
            <el-button
                type="primary"
                size="large"
                :loading="loading"
                @click="handleLogin"
                class="login-btn"
            >
              登 录
            </el-button>
          </el-form-item>
        </el-form>

        <div class="copyright">
          Author By @程序员何峥
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { User, Lock, Check } from '@element-plus/icons-vue';
import { useUserStore } from '@/store/user.js';
import { useRouter } from 'vue-router';
import { ElMessage } from "element-plus";

const loginForm = ref({
  username: '',
  password: ''
});
const loginFormRef = ref();
const userStore = useUserStore();
const router = useRouter();
const loading = ref(false);
const rememberPassword = ref(false);

const rules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 3, message: "用户名长度不能小于3位", trigger: "blur" }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, message: "密码长度不能小于6位", trigger: "blur" }
  ],
};

const handleLogin = () => {
  loginFormRef.value.validate(async valid => {
    if (valid) {
      loading.value = true;
      try {
        let result = await userStore.login(loginForm.value);
        if (result) {
          ElMessage.success("登录成功！");
          router.push("/");
        }
      } catch (error) {
        ElMessage.error("登录失败：" + (error.message || '未知错误'));
      } finally {
        loading.value = false;
      }
    }
  });
};
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e9edf3 100%);
  position: relative;
  overflow: hidden;
}

/* 添加背景装饰 */
.login-container::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  background: url("../assets/img/login_bg.jpg") no-repeat center center;
  background-size: cover;
  opacity: 0.1;
  z-index: 0;
}

.login-box {
  width: 900px;
  height: 550px;
  display: flex;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  box-shadow: 0 20px 50px rgba(4, 46, 85, 0.15);
  overflow: hidden;
  z-index: 1;
  position: relative;
}

/* 左侧区域 */
.login-left {
  flex: 1;
  background: linear-gradient(145deg, #042E55 0%, #0a3f6f 100%);
  padding: 40px 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.left-content {
  text-align: center;
}

.logo-img {
  width: 80px;
  height: 80px;
  margin-bottom: 20px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  padding: 10px;
}

.system-title {
  font-size: 1.8rem;
  font-weight: 500;
  margin: 0 0 10px 0;
  color: white;
  letter-spacing: 1px;
}

.system-desc {
  font-size: 1rem;
  opacity: 0.9;
  margin-bottom: 40px;
  color: rgba(255, 255, 255, 0.9);
}

.feature-list {
  text-align: left;
  margin-top: 30px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  font-size: 0.95rem;
  color: rgba(255, 255, 255, 0.9);
}

.feature-item :deep(.el-icon) {
  font-size: 18px;
  color: #4caf50;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  padding: 4px;
}

/* 右侧区域 */
.login-right {
  flex: 1;
  padding: 50px 40px;
  display: flex;
  flex-direction: column;
}

.welcome-text {
  margin-bottom: 30px;
}

.welcome-text h2 {
  font-size: 2rem;
  font-weight: 500;
  color: #1e293b;
  margin: 0 0 8px 0;
}

.welcome-text p {
  font-size: 0.95rem;
  color: #64748b;
  margin: 0;
}

.login-form {
  margin-top: 10px;
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px #d9e2ef inset;
  transition: all 0.2s ease;
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
  height: 48px;
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

:deep(.el-input__prefix) {
  margin-right: 8px;
}

:deep(.el-input__prefix-inner) {
  font-size: 18px;
  color: #7c8fa3;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: -8px 0 24px 0;
}

:deep(.el-checkbox__label) {
  color: #64748b;
  font-size: 0.9rem;
}

:deep(.el-link) {
  font-size: 0.9rem;
}

.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 8px;
  font-weight: 500;
  font-size: 1.1rem;
  letter-spacing: 2px;
  background-color: #042E55;
  border-color: #042E55;
  transition: all 0.2s ease;
  margin-top: 10px;
}

.login-btn:hover {
  background-color: #0a3f6f;
  border-color: #0a3f6f;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(4, 46, 85, 0.3);
}

.login-btn:active {
  transform: translateY(0);
}

.login-btn.is-loading {
  opacity: 0.8;
}

.copyright {
  text-align: center;
  color: #94a3b8;
  font-size: 0.85rem;
  margin-top: auto;
  padding-top: 30px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .login-box {
    width: 90%;
    height: auto;
    flex-direction: column;
  }

  .login-left {
    display: none;
  }

  .login-right {
    padding: 40px 30px;
  }

  .welcome-text h2 {
    font-size: 1.8rem;
  }
}
</style>