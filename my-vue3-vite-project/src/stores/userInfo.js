
import { defineStore } from 'pinia';
import { getToken, removeToken, setToken } from '../utils/token-utils';
import { getLogin,getUserInfo } from '../api/index';


/**
 * 用戶信息
 * @methods setUserInfos 設置用戶信息
 */
export const useUserInfoStore = defineStore('userInfo', {

	state: () => ({
    token: getToken(),
    nickName: '',
    uid: '',
  }),

	actions: {
    // 登入的異步action
    async login (loginForm) {
       // 發送登入的請求
      const result = await getLogin(loginForm)
      // 請求成功後, 取出token保存  pinia和local中
      const token = result.token
      
      this.token = token
      setToken(token)
    },
    async getInfo () {
      const result = await getUserInfo()
      this.nickName = result.loginUser.nickName
      this.uid = result.loginUser.uid
    },
    initUserInfo(){
      removeToken()
      this.nickName = ""
      this.uid = ""
      console.log('1111111111');
      
    }

  },
  

});