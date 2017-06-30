package com.cooperate.wxh5.wx.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooperate.wxh5.wx.service.CoreService;
import com.cooperate.wxh5.wx.util.StringCodeUtils;
import com.google.gson.Gson;


@Controller
@RequestMapping("/wx")
public class AuthController extends BaseController{
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	
	@Resource
	private CoreService coreService;
	
	@RequestMapping(value = "/auth",method = RequestMethod.GET)
	public void authWx(HttpServletRequest request,HttpServletResponse response){
		try {
			StringBuffer sb = new StringBuffer();  
			InputStream is = request.getInputStream();  
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");  
			BufferedReader br = new BufferedReader(isr);  
			String s = "";  
			while ((s = br.readLine()) != null) {  
			    sb.append(s);  
			}  
     //xml即为接收到微信端发送过来的xml数据  
			String xml = sb.toString(); 
			/** 判断是否是微信接入激活验证，只有首次接入验证时才会收到echostr参数，此时需要把它直接返回 */  
			String signature =  request.getParameter("signature");
			String timestamp =  request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String echostr =  request.getParameter("echostr");
			
			if(StringUtils.isEmpty(echostr) && request.getInputStream() != null){
				/* //正常的微信处理流程  
				Map<String, String> xmlMap = this.parseXml(xml);
				 log.error("weixin event result xmlMap",xmlMap.toString());
				Message message = messageFactory.createMessage(xmlMap);
				if(message == null){//目前只处理关注事件
					return;
				}
				
				ApplicationContext application = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
				IMessageProcessor processor = (IMessageProcessor) application.getBean(message.getProcessorId());
				log.info("weixin event return xml ",message.toString());
				String process = processor.process(message);
				this.sendResponse(response,process);*/
			}else{
			    
			    //this.sendResponse(response, echostr);
			    
				String token = "lianzhonghuzhu";//全局token
				String[] key = new String[3];
				key[0] = token;
				key[1] = timestamp;
				key[2] = nonce;
				Arrays.sort(key);
				//**确认此次GET请求来自微信服务器
				 
				String sha1Str = StringCodeUtils.sha1(key[0] + key[1] + key[2]);
				
				if(signature.equals(sha1Str)) {
					this.sendResponse(response, echostr);
				} else {
					 log.info("weixin event result xml ",xml);
					this.sendResponse(response, "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	@ResponseBody
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public void wechatServicePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8");
		String responseMessage = coreService.processRequest(request);
        log.info("responseMessage ",new Gson().toJson(responseMessage));
        response.getWriter().write(responseMessage);
    }
	
	private void sendResponse(HttpServletResponse response,String echostr){
		try {  
            String fullContentType = "text/plain" + ";charset=utf-8";
    		response.setContentType(fullContentType);
    		PrintWriter writer = response.getWriter();
        	 writer.write(echostr);
        	 writer.flush();  
        	 writer.close();
          
           
        } catch (Exception e) {  
            e.printStackTrace();  
        }
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> parseXml(String xmlStr){
		 // 将解析结果存储在HashMap中
	        Map<String, String> map = new HashMap<String, String>();
	 
	        // 读取输入流
	        SAXReader reader = new SAXReader();
	        Document document;
			try {
				document = reader.read(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
			} catch (UnsupportedEncodingException | DocumentException e) {
				throw new RuntimeException(e);
			}
	        // 得到xml根元素
	        Element root = document.getRootElement();
	        // 得到根元素的所有子节点
	        List<Element> elementList = root.elements();
	 
	        // 遍历所有子节点
	        for (Element e : elementList){
	        	 map.put(e.getName(), e.getText());
	        }
	        return map;
	           
	 }
}
