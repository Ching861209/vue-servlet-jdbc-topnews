import request from "../utils/request/"

// portal/findAllTypes
//獲取分類列表
export const getfindAllTypes = () => {
  return request.get("portal/findAllTypes");
};
// 分頁帶條件查詢所有新聞
export const getfindNewsPageInfo = (info) => {
  return request.post("portal/findNewsPage",info);
};
// 查看新聞詳情
export const getshowHeadlineDetail = (id) => {
    return request({
        method: "post",
        url: "portal/showHeadlineDetail",
        headers: {
        "Content-Type": "application/x-www-form-urlencoded;charset=utf-8",
        },
         data:`hid=${id}`
  });
};

//刪除的回調
// headline/removeByHid
export const removeByHid = (id) => {
    return request({
         method: "post",
            url: "headline/removeByHid",
            headers: {
            "Content-Type": "application/x-www-form-urlencoded;charset=utf-8",
            },
            data:`hid=${id}`
  })
};

//登入的接口
export const getLogin = (info) => {
  return request.post("user/login",info);
};
//獲取用戶信息的接口
export const getUserInfo = (info) => {
  return request.get("user/getUserInfo");
};

//註冊校驗的接口  user/checkUserName
export const registerValidateApi = (username) => {
    return request({
         method: "post",
            url: "user/checkUserName",
            headers: {
            "Content-Type": "application/x-www-form-urlencoded;charset=utf-8",
            },
            data:`username=${username}`
  })
};

// 註冊的接口
export const registerApi = (userInfo) => {
  return request.post("user/regist",userInfo)
}
//判斷用戶登入過期的接口
export const isUserOverdue = () => {
  return request.get("user/checkLogin")
}

// 修改頭條回顯的接口
export const getFindHeadlineByHid = (id) => {
    return request({
        method: "post",
        url: "headline/findHeadlineByHid",
        headers: {
        "Content-Type": "application/x-www-form-urlencoded;charset=utf-8",
        },
         data:`hid=${id}`
  });
};

//點擊保存修改的回調
// headline/update
export const saveOrAddNews = (news) => {
  return request.post("headline/update",news)
}

// headline/publish
export const issueNews = (news) => {
  return request.post("headline/publish",news)
}

