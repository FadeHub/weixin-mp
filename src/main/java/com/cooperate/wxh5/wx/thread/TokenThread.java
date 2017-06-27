package com.cooperate.wxh5.wx.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooperate.wxh5.wx.entity.accesstoken.Token;
import com.cooperate.wxh5.wx.service.TokenService;
import com.cooperate.wxh5.wx.service.WeiXinTokenService;
import com.cooperate.wxh5.wx.util.SpringApplicationContextHolder;
import com.cooperate.wxh5.wx.util.SpringContextUtil;

public class TokenThread implements Runnable{
	private static Logger log = LoggerFactory.getLogger(TokenThread.class);
	
	
	public  static String appid ="";
	
	public  static String secret = "";
	
	
	@Override
	public void run() {
		while(true){
			try {
				WeiXinTokenService weiXinTokenService = (WeiXinTokenService)SpringApplicationContextHolder.getBean("weiXinTokenService");
				TokenService tokenService = (TokenService)SpringApplicationContextHolder.getBean("tokenService");
				Token token = weiXinTokenService.getToken();
				if(token!=null){
					 tokenService.insert(token);
					  log.info("获取access_token成功，有效时长{}秒 token:{}", token.getExpiresIn(), token.getAccessToken());
	                    // 休眠7000秒
	                  Thread.sleep((Integer.parseInt(token.getExpiresIn()) - 200)*1000);
				}else{
					// 如果access_token为null，60秒后再获取
                    Thread.sleep(1000*30);
				}
				
			} catch (Exception e) {
				try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e1) {
                    log.error("{}", e1);
                }
                log.error("{}", e);
			}
		}
	}

}
