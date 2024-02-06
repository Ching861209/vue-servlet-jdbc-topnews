package com.atguigu.headline.controller;

import com.alibaba.druid.util.ResultSetConsumer;
import com.atguigu.headline.common.Result;
import com.atguigu.headline.common.ResultCodeEnum;
import com.atguigu.headline.pojo.NewsUser;
import com.atguigu.headline.service.NewsUserService;
import com.atguigu.headline.service.impl.NewsUserServiceImpl;
import com.atguigu.headline.util.JwtHelper;
import com.atguigu.headline.util.MD5Util;
import com.atguigu.headline.util.WebUtil;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/user/*")
public class NewsUserController extends BaseController{
    private NewsUserService userService = new NewsUserServiceImpl();

    /**
     * 前端發請求校驗是否失去登入狀態
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void checkLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("token");
        Result result = Result.build(null,ResultCodeEnum.NOTLOGIN);
        if(null !=token){
            if(!JwtHelper.isExpiration(token)){
                result=Result.ok(null);
            }
        }
        WebUtil.writeJson(resp,result);
    }

    protected void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //接收json訊息
        NewsUser registUser = WebUtil.readJson(req, NewsUser.class);
        //調用服務層將用戶訊息存入數據
        Integer rows = userService.registUser(registUser);
        //根據存入是否成功處理響應值
        Result result = Result.ok(null);
        if(rows == 0){
            result = Result.build(null,ResultCodeEnum.USERNAME_USED);
        }
        WebUtil.writeJson(resp,result);
    }

    protected void checkUserName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //獲取用戶帳號
        String username = req.getParameter("username");
        //根據用戶帳號查詢用戶訊息,找到的話返回505代表被占用,找不到返回200
        NewsUser newsUser = userService.findByUsername(username);
        Result result = Result.ok(null);
        if(null != newsUser){
            result = Result.build(null,ResultCodeEnum.USERNAME_USED);
        }
        WebUtil.writeJson(resp,result);
    }

    protected void getUserInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //獲取請求中的token
        String token = req.getHeader("token");
        Result result = Result.build(null,ResultCodeEnum.NOTLOGIN);
        if(null != token && (!"".equals(token))){
            if (!JwtHelper.isExpiration(token)) {
                Integer userId = JwtHelper.getUserId(token).intValue();
                NewsUser newsUser = userService.findByUid(userId);
                if(null != newsUser){
                    //校驗token 通過校驗,查詢用戶訊息並放入Result/未通過,響應Result code 504
                    Map data = new HashMap();
                    newsUser.setUserPwd("");
                    data.put("loginUser",newsUser);
                    result = Result.ok(data);
                }
            }
        }
        WebUtil.writeJson(resp,result);
    }

    protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //接收用戶名和密碼
        NewsUser paramUser = WebUtil.readJson(req, NewsUser.class);
        //調用服務層方法,實現登入
        NewsUser loginUser = userService.findByUsername(paramUser.getUsername());
        Result result = null;
        if(null != loginUser){
            if(MD5Util.encrypt(paramUser.getUserPwd()).equalsIgnoreCase(loginUser.getUserPwd())){
                Integer uid = loginUser.getUid();
                String token = JwtHelper.createToken(uid.longValue());
                Map data = new HashMap();
                data.put("token",token);
                result=Result.ok(data);
            }else{
                result=Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
            }

        }else{
            result = Result.build(null, ResultCodeEnum.USERNAME_ERROR);

        }
        //向客戶端響應登入驗證訊息
        WebUtil.writeJson(resp,result);
    }
}
