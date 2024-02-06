
export const staticRoutes = [
  {
    path: "/",
    redirect: "/headlinenews",
  },
  {
    // 新聞
    path: "/headlinenews",
    component: () => import("../pages/HeadlineNews/index.vue"),
    name: "HeadlineNews",
  },
  {
    //新聞詳情
    path: "/detail",
    component: () => import("../pages/Detail/index.vue"),
    name: "Detail",
  },
  {
    // 登入
    path: "/login",
    component: () => import("../pages/Login/index.vue"),
    name: "Login",
  },
  {
    //註冊
    path: "/register",
    component: () => import("../pages/Register/index.vue"),
    name: "Register",
  },
   {
    //發布新聞的頁面
    path: "/addormodifynews",
    component: () => import("../pages/addOrModifyNews/index.vue"),
    name: "addOrModifyNews",
  },
];
