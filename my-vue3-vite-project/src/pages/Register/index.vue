<template>
  <div class="register-container">
    <el-form
      :model="registerForm"
      ref="formRef"
      label-width="80px"
      class="register-form"
      :rules="registerRules"
    >
      <h2>用戶註冊</h2>
          <el-form-item label="姓名" prop="nickName">
          <el-input
            v-model="registerForm.nickName"
            autocomplete="off"
            ref="nickName"
            name="nickName"
            placeholder="請輸入姓名"
          ></el-input>
        </el-form-item>
      <el-form-item label="用戶名" prop="username">
        <el-input
          v-model="registerForm.username"
          autocomplete="off"
          ref="username"
          name="username"
          placeholder="請輸入用戶名"
        ></el-input>
      </el-form-item>
      <el-form-item label="密碼" prop="userPwd">
        <el-input
          type="password"
          v-model="registerForm.userPwd"
          ref="userPwd"
          name="userPwd"
          autocomplete="off"
          placeholder="請輸入密碼"
        ></el-input>
      </el-form-item>
      <!-- prop="confirmPassword" -->
      <el-form-item label="確認密碼"  prop="confirmPassword">
        <el-input
          type="password"
          v-model="registerForm.confirmPassword"
          autocomplete="off"
          ref="confirmPassword"
          name="confirmPassword"
          placeholder="請確認密碼"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="register">註冊</el-button>
        <el-button type="danger" @click="resetForm">重置</el-button>
        <el-button type="success" @click="goLogin">去登入</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
 import { defineComponent } from 'vue'
  export default  defineComponent({
    name:'Register'
  })
</script>
<script lang="ts" setup>
import { ref } from "vue"
import { useRouter } from 'vue-router'
import { ElMessage, FormInstance } from 'element-plus';
import { registerValidateApi, registerApi } from "../../api/index"
const router = useRouter()
// 初始化註冊參數
const registerForm = ref({
  username: "",
  userPwd: "",
  confirmPassword: "",
  nickName:''
})
const formRef = ref<FormInstance>()
  // 校驗規則
const validateUsername = (rule: any, value: any, callback: any) => {
  if (value.length < 4) {
    callback(new Error('用戶名長度不能小於4位'))
  } else {
    callback()
  }
}
// 校驗規則
const validatePassword = (rule: any, value: any, callback: any) => {
  if (value.length < 6) {
    callback(new Error('密碼長度不能小於6位'))
  } else {
    callback()
  }
}
// 校驗規則
const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value.length < 6) {
    callback(new Error('密碼長度不能小於6位'))
  } else {
    callback()
  }
}
// 校驗規則
const validateNickName = (rule: any, value: any, callback: any) => {
  if (value.length >= 2  && value.length  <= 6  ) {
    callback()
  } else {
    callback(new Error('姓名必須在2-6位'))
}
}
// 校驗規則
const registerRules = {
  nickName: [{ required: true, trigger: 'blur', validator: validateNickName }],
  username: [{ required: true, validator: validateUsername }],
  userPwd: [{ required: true, trigger: 'blur', validator: validatePassword }],
  confirmPassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassword }]
}

//點擊註冊的回調
const register = async () => {
  await formRef.value?.validate()
  if (registerForm.value.userPwd == registerForm.value.confirmPassword) {
    // 調用用戶名校驗接口
    await registerValidateApi(registerForm.value.username)
    //  整理參數
    const obj = {
      username: "",
      userPwd: "",
      nickName: ''
    }
    obj.username = registerForm.value.username
    obj.userPwd = registerForm.value.userPwd
    obj.nickName = registerForm.value.nickName
    //  調用註冊接口
    await registerApi(obj)
    formRef.value?.resetFields()
    ElMessage.success("註冊成功")
    } else {
      return ElMessage.error("密碼和確定密碼必須一致")
    }
    
}
//點擊去登入的回調
const goLogin = () => {
  router.push({path:"/login"})
}

//點擊重置的回調
const resetForm = () => {
  //重置表單
  formRef.value?.resetFields()
}
  
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
.register-form {
  width: 400px;
  text-align: center;
}
</style>
