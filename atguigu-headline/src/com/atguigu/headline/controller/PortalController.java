package com.atguigu.headline.controller;

import com.atguigu.headline.common.Result;
import com.atguigu.headline.pojo.NewsType;
import com.atguigu.headline.pojo.vo.HeadlineDetailVo;
import com.atguigu.headline.pojo.vo.HeadlinePageVo;
import com.atguigu.headline.pojo.vo.HeadlineQueryVo;
import com.atguigu.headline.service.NewsHeadlineService;
import com.atguigu.headline.service.NewsTypeService;
import com.atguigu.headline.service.impl.NewsHeadlineServiceImpl;
import com.atguigu.headline.service.impl.NewsTypeServiceImpl;
import com.atguigu.headline.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 門戶控制器
 * 專門放那些不需要登入,不需要做增刪改查得請求
 */
@WebServlet("/portal/*")
public class PortalController extends BaseController{
    private NewsTypeService typeService = new NewsTypeServiceImpl();
    private NewsHeadlineService headlineService = new NewsHeadlineServiceImpl();

    /**
     * 查詢新聞詳情的業務接口實現
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void showHeadlineDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //接收要查詢新聞的hid
        int hid = Integer.parseInt(req.getParameter("hid"));
        //調用服務層完成查詢處理
        HeadlineDetailVo headlineDetailVo = headlineService.findHeadlineDetail(hid);
        Map data = new HashMap();
        data.put("headline",headlineDetailVo);
        WebUtil.writeJson(resp,Result.ok(data));
        //將查到的訊息響應給客戶端
    }

    /**
     * 查詢所有新聞類型
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void findAllTypes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //查詢所有新聞類型,裝入Result響應給客戶端
        List<NewsType> newsTypeList = typeService.findAll();
        WebUtil.writeJson(resp,Result.ok(newsTypeList));
    }

    /**
     * 分頁帶條件查詢新聞
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void findNewsPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //接收請求中的參數
        HeadlineQueryVo headlineQueryVo = WebUtil.readJson(req, HeadlineQueryVo.class);
        //將參數傳遞給服務層 進行分頁查詢
        /**"pageInfo":{
         *          pageData:[
         *          {
         *              "hid":"1",                     // 新闻id
         *              "title":" ...今日頭條 ...",      // 新聞標題
         *              "type":"1",                    // 新聞所屬類別編號
         *              "pageViews":"40",              // 新聞瀏覽量
         *              "pastHours":"3" ,              // 發布時間已經過的小時數
         *              "publisher":"1"
         *
         *          }
         *                  ]
         *          pageNum:1,
         *          pageSize:1,
         *          totalPage1:,
         *          totalSize:1
         */
        Map pageInfo = headlineService.findPage(headlineQueryVo);
        Map data = new HashMap();
        data.put("pageInfo",pageInfo);
        //將分頁查詢的結果轉為JSON傳給客戶端
        WebUtil.writeJson(resp,Result.ok(data));
    }
}
