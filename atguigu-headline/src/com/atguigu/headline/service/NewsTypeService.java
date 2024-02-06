package com.atguigu.headline.service;

import com.atguigu.headline.pojo.NewsType;

import java.util.List;

public interface NewsTypeService {
    /**
     * 查詢所有頭條類型的方法
     * @return 以List<NewsType>形式返回多個頭條類型
     */
    List<NewsType> findAll();
}
