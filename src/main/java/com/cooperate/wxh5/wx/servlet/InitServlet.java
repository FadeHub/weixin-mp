package com.cooperate.wxh5.wx.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooperate.wxh5.wx.thread.TokenThread;

public class InitServlet extends HttpServlet{
	/*private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(InitServlet.class);
    
    @Override
    public void init() throws ServletException {
    	TokenThread.appid = getInitParameter("appid");
        TokenThread.secret = getInitParameter("secret");
        log.info("weixin api appid:{}", TokenThread.appid);
        log.info("weixin api appsecret:{}", TokenThread.secret);

        // 未配置appid、appsecret时给出提示
        if ("".equals(TokenThread.appid) || "".equals(TokenThread.secret)) {
            log.error("appid and appsecret configuration error, please check carefully.");
        } else {
            // 启动定时获取access_token的线程
            new Thread(new TokenThread()).start();
        }
    }*/
}
