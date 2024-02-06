<template>
  <el-card class="box-card AddNewsContainer">
    <el-form :rules="newsRules" :model="formData" ref="formRef" size="default">
      <el-form-item label="文章標題" prop="title">
        <el-input v-model="formData.title" placeholder="請輸入標題"></el-input>
      </el-form-item>
      <el-form-item style="margin: 50px 0;" label="文章內容" prop="article">
        <el-input v-model="formData.article" type="textarea" rows="8"></el-input>
      </el-form-item>
      <el-form-item label="文章內容"  prop="type">
        <el-select v-model="formData.type" placeholder="請選擇文章類別">
          <el-option v-for="item in article" :label="item.name" :value="item.type">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <el-form-item>
      <el-button   @click="handlerCancel">取消</el-button>
      <el-button type="primary"  @click="handlerSave">保存</el-button>
    </el-form-item>
  </el-card>
</template>

<script>
import { defineComponent } from 'vue'
import { isUserOverdue } from '../../api/index'
export default defineComponent({
  name: 'AddNews'
})
</script>
<script  setup>
import { getFindHeadlineByHid , saveOrAddNews, issueNews } from "../../api/index"
import { ref, onMounted } from "vue"
import { useRoute } from 'vue-router'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
const router = useRouter() 
const route = useRoute() 


const formRef = ref()
// 校驗規則
const validateType = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('文章標題是必須的'))
  }
}
// 校驗規則
const validateArticle = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('文章內容是必須的'))
  }
}
// 校驗規則
const validateTitle = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('文章類別是必須的'))
}
}
// 校驗規則
const newsRules = {
  title: [{ required: true, trigger: 'blur', validator: validateTitle }],
  article: [{ required: true, trigger: 'blur', validator: validateArticle }],
  type: [{ required: true, validator: validateType }],
}


const formData = ref({
  hid:null,
  title: "",   // 文章標題
  article: "", // 文章內容
  type: ""     // 文章類別
})
//初始化文章類別數據
const article = [
  {
    type: "1",
    name: "新聞"
  },
  {
    type: "2",
    name: "體育"
  },
  {
    type: "3",
    name: "娛樂"
  },
  {
    type: "4",
    name: "科技"
  },
  {
    type: "5",
    name: "其他"
  }
]
// 如果是點擊修改的話 路由就會攜帶hid參數  就要發送請求 獲取數據回顯
const clickModifyEcho = async () => {
  if (!route.query.hid)  return
    let result = await getFindHeadlineByHid(route.query.hid)
    formData.value.title = result.headline.title
  formData.value.article = result.headline.article
     
    formData.value.type = result.headline.type === 1 ? "新聞" : result.headline.type === 2 ? "體育" : result.headline.type === 3 ? "娛樂" : result.headline.type === 4 ? "科技" : "其他" 
}
//頁面掛載生命周期
onMounted(() => {
  clickModifyEcho()
})
//點擊取消的回調
const handlerCancel = () => {
  router.back()
}
//點擊保存的回調
const handlerSave = async () => {
  await formRef.value?.validate()
    //發送請求判斷用戶是否token過期
  await isUserOverdue()
const Obj = {...formData.value}

  //整理請求參數
//  Obj.hid = userInfoStore.uid.toString()  //添加用戶id 讓後端知道誰添加的
 Obj.hid = route.query.hid  //添加用戶id 讓後端知道誰添加的
// 判斷type類型
 if(Obj.type == "新聞" ) Obj.type = "1"
 if(Obj.type == "體育" ) Obj.type = "2"
 if(Obj.type == "娛樂" ) Obj.type = "3"
 if(Obj.type == "科技" ) Obj.type = "4"
 if (Obj.type == "其他") Obj.type = "5"
  //發送請求
  if (route.query.hid) {
    await saveOrAddNews(Obj)
    ElMessage.success("修改成功")
  }
  else {
    await issueNews(formData.value)
    ElMessage.success("添加成功")
    }
    router.push({ name: "HeadlineNews" });
}


</script>

<style lang="less" scoped>
.AddNewsContainer {
  width: 600px;
  margin: 150px auto;
}
</style>
