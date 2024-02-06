package com.atguigu.headline.service.impl;

import com.atguigu.headline.dao.NewsHeadlineDao;
import com.atguigu.headline.dao.impl.NewsHeadlineDaoImpl;
import com.atguigu.headline.pojo.NewsHeadline;
import com.atguigu.headline.pojo.vo.HeadlineDetailVo;
import com.atguigu.headline.pojo.vo.HeadlinePageVo;
import com.atguigu.headline.pojo.vo.HeadlineQueryVo;
import com.atguigu.headline.service.NewsHeadlineService;
import com.atguigu.headline.service.NewsUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsHeadlineServiceImpl implements NewsHeadlineService {
    private NewsHeadlineDao headlineDao = new NewsHeadlineDaoImpl();
    /**
     *          * pageData:[
     *          *  {
     *          *          "hid":"1",                     // 新闻id
     *          *          "title":" ...今日頭條 ...",      // 新聞標題
     *          *          "type":"1",                    // 新聞所屬類別編號
     *          *          "pageViews":"40",              // 新聞瀏覽量
     *          *          "pastHours":"3" ,              // 發布時間已經過的小時數
     *          *          "publisher":"1"
     *          *  }
     *          * ]
     *          * pageNum:1,
     *          * pageSize:1,
     *          * totalPage1:,
     *          * totalSize:1
     */
    @Override
    public Map findPage(HeadlineQueryVo headlineQueryVo) {
        int pageNum = headlineQueryVo.getPageNum();
        int pageSize = headlineQueryVo.getPageSize();
        List<HeadlinePageVo> pageData = headlineDao.findPageList(headlineQueryVo);
        int totalSize = headlineDao.findPageSize(headlineQueryVo);

        int totalPage = totalSize%pageSize == 0 ? totalSize/pageSize : totalSize/pageSize + 1;
        Map pageInfo = new HashMap();
        pageInfo.put("pageNum",pageNum);
        pageInfo.put("pageSize",pageSize);
        pageInfo.put("totalSize",totalSize);
        pageInfo.put("totalPage",totalPage);
        pageInfo.put("pageData",pageData);
        return pageInfo;
    }

    @Override
    public HeadlineDetailVo findHeadlineDetail(int hid) {
        //修改新聞的瀏覽量 +1
        headlineDao.incrPageViews(hid);
        //查詢新聞的詳情
        return headlineDao.findHeadlineDetail(hid);

    }


    public int addNewsHeadline(NewsHeadline newsHeadline) {
        return headlineDao.addNewsHeadline(newsHeadline);
    }

    @Override
    public NewsHeadline findByHid(Integer hid) {
        return headlineDao.findByHid(hid);
    }

    @Override
    public int update(NewsHeadline newsHeadline) {
        return headlineDao.update(newsHeadline);
    }

    @Override
    public int removeByHid(Integer hid) {
        return headlineDao.removeByHid(hid);
    }
}
