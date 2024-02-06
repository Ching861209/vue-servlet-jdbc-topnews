import axios from "axios";
import { ElMessage } from 'element-plus';
import pinia from '../stores/index';
import { useUserInfoStore } from '../stores/userInfo';
import NProgress from "nprogress";
import "nprogress/nprogress.css";
// 配置新建一個 axios 實例
const service = axios.create({
  baseURL: "/app-dev/",
  timeout: 50000,
});

// 添加請求攔截器
service.interceptors.request.use((config) => {
  NProgress.start()//開啟進度條
  // 如果有token, 通過請求頭攜帶給後台
  const userInfoStore = useUserInfoStore(pinia) // 如果不是在組件中調用,必須傳入pinia
  const token = userInfoStore.token
     if (token) {
      // config.headers['token'] = token  // 報錯: headers對象並沒有聲明有token, 不能隨便添加
      (config.headers)['token'] = token
    }
  return config;
});

// 添加響應攔截器
service.interceptors.response.use(
  (response) => {
  NProgress.done()//關閉進度條

    if(response.data.code !== 200){
    // 判斷響應狀態碼
    if (response.data.code == 501)  return  Promise.reject(ElMessage.error("用戶名有誤"))
    else if (response.data.code == 503) return  Promise.reject(ElMessage.error("密碼有誤"))
    else if (response.data.code == 504) return  Promise.reject(ElMessage.error("登錄已過期"))
    else if (response.data.code == 505) return  Promise.reject(ElMessage.error("用戶名占用"))
    } else {
      return response.data.data; /* 返回成功響應數據中的data屬性數據 */
    }
  },
  (error) => {
  NProgress.done()//關閉進度條
    return Promise.reject(error.message);
  }
);

export default service;
