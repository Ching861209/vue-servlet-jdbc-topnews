<template>
  <div class="headerContainer">
    <!-- 頭部左側區域 -->
    <div class="left">
      <ul>
        <li @click="HighlightHandler(index,)"  v-for="(item,index) in findAllTypeList" :key="item.tid">
          <a :class="{ active: item.isHighlight }" href="javascript:;">{{item.tname}}</a>
        </li>
      </ul>
    </div>
    <!-- 頭部右側區域 -->
    <div class="right">
      <div class="rightInput" style="margin-right: 50px;">
        <el-input v-model="keywords" placeholder="搜索最新新聞"></el-input>
        <!-- <el-button   type="primary">搜索</el-button> -->
      </div>

  
      <!-- 用戶登入以後的展示 -->
      <div class="btn-dropdown">
      <!-- 用戶沒有登入的時候的展示 -->
     
      <div v-if="nickName" style="display: flex; justify-content: center; align-items: center;">
             <el-dropdown>
          <el-button type="primary">
          您好:{{ nickName }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handlerNews">發布新聞</el-dropdown-item>
              <el-dropdown-item>個人中心</el-dropdown-item>
              <el-dropdown-item>瀏覽記錄</el-dropdown-item>
              <el-dropdown-item @click="Logout">退出登入</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
       <div v-else class="containerButton">
          <el-button size="small" style="background: #212529; color: #aea7a2" @click="toLogin">登入</el-button>
          <el-button size="small" style="background: #ffc107; color: #684802" @click="toRegister">註冊</el-button>
        </div>
      
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'Header'
})
</script>

<script setup>
import { getfindAllTypes, isUserOverdue } from '../api/index'
import { ref, onMounted , getCurrentInstance ,watch, onUpdated} from "vue"
import { useRouter } from 'vue-router'
import { ArrowDown } from '@element-plus/icons-vue'
import { removeToken } from '../utils/token-utils' 
import pinia from '../stores/index';
import { useUserInfoStore } from '../stores/userInfo'
const userInfoStore = useUserInfoStore(pinia)
const nickName = ref("")
// 獲取到 全局事件總線
const { Bus } = getCurrentInstance().appContext.config.globalProperties
const router = useRouter()
const keywords = ref("") // 收集搜索最新新聞參數
//監視搜索參數的變化 ,當搜索參數變化的時候給HeadlineNews組件傳遞數據
watch(keywords, (newVal) => {
  Bus.emit('keyword', newVal)
})
const findAllTypeList = ref([])//所有新聞分類
const toLogin = () => {
router.push({ name: "Login" });
}
//點擊去注冊頁面
const toRegister = () => {
  router.push({ name: "Register" });
}
const getList = async () => {
  let result = await getfindAllTypes()
  // 遍歷數據添加高亮標識
  result.forEach((item) => {
    item.tid = item.tid
    item.tname = item.tname
    item.isHighlight = false
  })
  // 添加新聞數據
  result.unshift({
    isHighlight: true,
    tid: 0,
    tname: "包新聞"
  })
  findAllTypeList.value = result
}
// 頁面掛載的生命周期回調
onUpdated(() => {
  nickName.value = userInfoStore.nickName
})
onMounted(() => {
  getList()
})

//點擊切換高亮的回調(排他思想)
const HighlightHandler = (index) => {
  findAllTypeList.value.forEach((item) => {
    item.isHighlight = false
  })
  // 切換高亮的時候把tid傳給HeadlineNews組件
  findAllTypeList.value[index].isHighlight = true
  Bus.emit('tid', findAllTypeList.value[index].tid)
}

// 點擊退出登入的回調
const Logout = () => {
  removeToken()
  userInfoStore.initUserInfo()
  nickName.value = ""
  router.go({ name: "HeadlineNews" });
}

//點擊發布新聞的回調
const handlerNews = async () => {
  //發送請求判斷用戶是否token過期
  await isUserOverdue()
  router.push({ name: "addOrModifyNews" });
}
</script>

<style>
.el-dropdown {
  vertical-align: top;
  width: 100px;
}

.el-dropdown+.el-dropdown {
  margin-left: 15px;
}

.el-icon-arrow-down {
  font-size: 12px;
}
</style>

<style lang="less" scoped>
.headerContainer {
  width: 100%;
  height: 60px;
  background: #212529;
  display: flex;
  justify-content: space-around;
  .left {
    ul {
      display: flex;
      li {
        list-style: none;
        margin-left: 20px;
        a:-webkit-any-link {
          text-decoration: none;
          color: #59646b;
          &.active {
            color: #c0adab;
          }
        }
      }
    }
  }
  .right {
    .containerButton {
      display: flex;
      align-items: center;
    }
    line-height: 60px;
    display: flex;
    flex-wrap: nowrap;
    .rightInput {
      display: flex;
       align-items: center;
      :deep(.el-input__inner) {
        height: 30px;
        width: 150px;
      }
    }
    .btn-dropdown{
      display: flex;
      align-items: center;
    }
    :deep(.el-button) {
      margin: 0 0 0 10px;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
}


.example-showcase .el-dropdown + .el-dropdown {
  margin-left: 15px;
}
.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>


















