package com.atguigu.headline.service;

import com.atguigu.headline.pojo.NewsUser;

public interface NewsUserService {
    /**
     * 根據用戶登入的用戶名查找資料庫中是否有用戶註冊資料
     * @param username 用戶輸入的帳戶名
     * @return 找到返回NewsUser對象,找不到的話則返回null
     */
    NewsUser findByUsername(String username);

    /**
     * 根據token獲得userId值並查找資料庫中是否有匹配的資料
     * @param userId token中的userId
     * @return 找到返回NewsUser對象,找不到的話則返回null
     */
    NewsUser findByUid(Integer userId);

    /**
     *
     * @param registUser
     * @return
     */
    Integer registUser(NewsUser registUser);
}
