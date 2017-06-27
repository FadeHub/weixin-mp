package com.cooperate.wxh5.wx.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooperate.wxh5.wx.entity.accesstoken.AccessToken;
import com.cooperate.wxh5.wx.entity.menu.Button;
import com.cooperate.wxh5.wx.entity.menu.CommonButton;
import com.cooperate.wxh5.wx.entity.menu.ComplexButton;
import com.cooperate.wxh5.wx.entity.menu.Menu;
import com.cooperate.wxh5.wx.util.WeixinUtil;

public class MenuController {

	 private static Logger log = LoggerFactory.getLogger(MenuController.class);

	    public static void main(String[] args) {
	        // 第三方用户唯一凭证
	        String appId = "wxc10075b97c589304";
	        // 第三方用户唯一凭证密钥
	        String appSecret = "72c7940589d71634912352b060dbbb37";

	        // 调用接口获取access_token
	        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

	        if (null != at) {
	            // 调用接口创建菜单
	            int result = WeixinUtil.createMenu(getMenu(), at.getToken());

	            // 判断菜单创建结果
	            if (0 == result)
	                log.info("菜单创建成功！");
	            else
	                log.info("菜单创建失败，错误码：" + result);
	        }
	    }

	    /**
	     * 组装菜单数据
	     * 
	     * @return
	     */
	    private static Menu getMenu() {
	        CommonButton btn11 = new CommonButton();
	        btn11.setName("天气预报");
	        btn11.setType("view");
	        btn11.setUrl("https://www.baidu.com/");
	        btn11.setKey("11");

	        CommonButton btn12 = new CommonButton();
	        btn12.setName("公交查询");
	        btn12.setType("click");
	        btn12.setKey("12");

	        CommonButton btn13 = new CommonButton();
	        btn13.setName("周边搜索");
	        btn13.setType("click");
	        btn13.setKey("13");

	        CommonButton btn14 = new CommonButton();
	        btn14.setName("历史上的今天");
	        btn14.setType("click");
	        btn14.setKey("14");

	        CommonButton btn21 = new CommonButton();
	        btn21.setName("歌曲点播");
	        btn21.setType("click");
	        btn21.setKey("21");

	        CommonButton btn22 = new CommonButton();
	        btn22.setName("经典游戏");
	        btn22.setType("click");
	        btn22.setKey("22");

	        CommonButton btn23 = new CommonButton();
	        btn23.setName("美女电台");
	        btn23.setType("click");
	        btn23.setKey("23");

	        CommonButton btn24 = new CommonButton();
	        btn24.setName("人脸识别");
	        btn24.setType("click");
	        btn24.setKey("24");

	        CommonButton btn25 = new CommonButton();
	        btn25.setName("聊天唠嗑");
	        btn25.setType("click");
	        btn25.setKey("25");

	        CommonButton btn31 = new CommonButton();
	        btn31.setName("邀请好友");
	        btn31.setType("view");
	        btn31.setUrl("https://sit.lianzhonghuzhu.com/wxh5/inviteFriends.html");
	        btn31.setKey("31");

	        CommonButton btn32 = new CommonButton();
	        btn32.setName("我要登录");
	        btn32.setType("view");
	        btn32.setKey("32");
	        btn32.setUrl("https://sit.lianzhonghuzhu.com/wxh5/web_login.html");

	       /* CommonButton btn33 = new CommonButton();
	        btn33.setName("幽默笑话");
	        btn33.setType("click");
	        btn33.setKey("33");*/

	        
	        /**
	         * 微信：  mainBtn1,mainBtn2,mainBtn3底部的三个一级菜单。
	         */
	        
	        ComplexButton mainBtn1 = new ComplexButton();
	        mainBtn1.setName("了解连众");
	        mainBtn1.setUrl("https://sit.lianzhonghuzhu.com/wxh5/bothDifference.html");
	        mainBtn1.setType("view");
	        //一级下有4个子菜单
	       // mainBtn1.setSub_button(new CommonButton[] { btn11, btn12, btn13, btn14 });

	        
	        ComplexButton mainBtn2 = new ComplexButton();
	        mainBtn2.setName("加入互助");
	        mainBtn2.setUrl("https://sit.lianzhonghuzhu.com/wxh5/");
	        mainBtn2.setType("view");
	        //mainBtn2.setSub_button(new CommonButton[] { btn21, btn22, btn23, btn24, btn25 });

	        
	        ComplexButton mainBtn3 = new ComplexButton();
	        mainBtn3.setName("我的互助");
	        mainBtn3.setSub_button(new CommonButton[] { btn31, btn32 });

	        
	        /**
	         * 封装整个菜单
	         */
	        Menu menu = new Menu();
	        menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });

	        return menu;
	    }
}
