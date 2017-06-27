package com.cooperate.wxh5.wx.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cooperate.wxh5.wx.exception.CommonRuntimeException;

import net.sf.json.JSONObject;

/**
 * 发送https请求
 * @author jing
 *
 */
@Component
public class WeixinHttpClient {

	private static Logger log = LoggerFactory.getLogger(WeixinHttpClient.class);
	
	private HttpClient httpclient;
	
	public WeixinHttpClient() {
		try {
	        SSLContext ctx = SSLContext.getInstance("TLS");
	        X509TrustManager tm = new X509TrustManager() {

	            @Override
				public void checkClientTrusted(X509Certificate[] chain,
	                    String authType) throws CertificateException
	            {
	                // TODO Auto-generated method stub

	            }

	            @Override
				public void checkServerTrusted(X509Certificate[] chain,
	                    String authType) throws CertificateException
	            {
	                // TODO Auto-generated method stub

	            }

	            @Override
				public X509Certificate[] getAcceptedIssuers()
	            {
	                // TODO Auto-generated method stub
	                return null;
	            }

	        };
	        ctx.init(null, new TrustManager[] { tm }, null);
	        SSLSocketFactory ssf = new SSLSocketFactory(ctx);
	        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        ClientConnectionManager ccm = new DefaultHttpClient().getConnectionManager();
	        SchemeRegistry sr = ccm.getSchemeRegistry();
	        //设置要使用的端口，默认是443
	        sr.register(new Scheme("https", ssf, 443));
	        
	        httpclient = new DefaultHttpClient(ccm, new DefaultHttpClient().getParams());
		} catch (Exception ex) {
	        ex.printStackTrace();
	    }
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
	}
	
	/**
	 * 
	 * @param certPath  私钥信息的证书文件路径
	 * @param mchId 微信支付分配的商户号
	 */
	public WeixinHttpClient(String certPath,String mchId) {
		try {
			  KeyStore keyStore  = KeyStore.getInstance("PKCS12");
		        FileInputStream instream = new FileInputStream(new File(certPath));
		        try {
		            keyStore.load(instream, mchId.toCharArray());
		        } finally {
		            instream.close();
		        }
		        // Trust own CA and all self-signed certs
		        SSLContext sslcontext = SSLContexts.custom()
		                .loadKeyMaterial(keyStore, mchId.toCharArray())
		                .build();
		        // Allow TLSv1 protocol only
		        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
		                sslcontext,
		                new String[] { "TLSv1" },
		                null,
		                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		        httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	public JSONObject post(String url, Map<String, String> paramMap) {
		HttpPost httPost = new HttpPost(url);

		List<NameValuePair> list = new ArrayList<NameValuePair>();

		try {
			if(paramMap!=null && paramMap.size()>0){
				for(Entry<String,String> entry : paramMap.entrySet()){
					list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
				}
			}
			
			HttpEntity httpEntity = new UrlEncodedFormEntity(list,"UTF-8");
			
			httPost.setEntity(httpEntity);
		} catch (UnsupportedEncodingException e) {
			log.error("http post param error.", e);
			throw new CommonRuntimeException(-1,"http post param error."+ e);
		}
		
		String res = exec(httPost);
		JSONObject ret = JSONObject.fromObject(res);
		
		if(ret.containsKey("errcode") && ret.getInt("errcode") != 0) {
			String errmsg = ret.getString("errmsg");
			Integer errCode = ret.getInt("errcode");
			log.warn("responseText:" + res);
			throw new CommonRuntimeException(100,errmsg);
		}
		
		return ret;
		
	}
	
	public JSONObject post(String url, String param) throws CommonRuntimeException {
		HttpPost httPost = new HttpPost(url);


		try {
			StringEntity entity = new StringEntity(param, "utf-8");
			
			httPost.setEntity(entity);
		} catch (Exception e) {
			log.error("http post param error.", e);
			throw new CommonRuntimeException(-1,"http post param error."+e);
		}
		
		String res = exec(httPost);
		JSONObject ret = JSONObject.fromObject(res);
		
		if(ret.containsKey("errcode") && ret.getInt("errcode") != 0) {
			String errmsg = ret.getString("errmsg");
			Integer errCode = ret.getInt("errcode");
			log.warn("responseText:" + res);
			throw new CommonRuntimeException(100,errmsg);
		}
		
		return ret;
		
	}
	
	public String postToString(String url, String param) throws CommonRuntimeException {
		HttpPost httPost = new HttpPost(url);


		try {
			StringEntity entity = new StringEntity(param, "utf-8");
			
			httPost.setEntity(entity);
		} catch (Exception e) {
			log.error("http post param error.", e);
			throw new CommonRuntimeException(-1,"http post param error."+e);
		}
		
		String res = exec(httPost);	
		log.debug("weixinHttpclient postToString result {}",res);
		return res;
		
	}
	
	public JSONObject post(String url, Map<String, File> dataMap, Map<String, String> paramMap) throws CommonRuntimeException {
		HttpPost httPost = new HttpPost(url);
		try {
			
			
			MultipartEntity reqEntity = new MultipartEntity();

			if(paramMap!=null && paramMap.size()>0){
				for(Entry<String,String> entry : paramMap.entrySet()){
					reqEntity.addPart(entry.getKey(),new StringBody(entry.getValue()));
				}
			}
			
			if(dataMap!=null && dataMap.size()>0){
				for(Entry<String,File> entry : dataMap.entrySet()){
					reqEntity.addPart(entry.getKey(), new FileBody(entry.getValue()));
				}
			}
			
			httPost.setEntity(reqEntity);
		} catch (UnsupportedEncodingException e) {
			log.error("http post param error.", e);
			throw new CommonRuntimeException(-1,"http post param error." + e);
		}
		
		String res = exec(httPost);
		log.debug("weixinHttpclient post result {}",res);
		JSONObject ret = JSONObject.fromObject(res);
		
		if(ret.containsKey("errcode") && ret.getInt("errcode") != 0) {
			String errmsg = ret.getString("errmsg");
			Integer errCode = ret.getInt("errcode");
			log.warn("responseText:" + res);
			throw new CommonRuntimeException(100,errmsg);
		}
		
		return ret;
		
	}
	
	public JSONObject get(String url, Map<String, String> paramMap) throws CommonRuntimeException {
		HttpGet httGet = new HttpGet(url);
	
		List<NameValuePair> list = new ArrayList<NameValuePair>();

		if(paramMap!=null && paramMap.size()>0){
			for(Entry<String,String> entry : paramMap.entrySet()){
				list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
			}
		}
		String str;
		try {
			str = EntityUtils.toString(new UrlEncodedFormEntity(list));
			 log.debug("weixinHttpclient get result {}",str);
			httGet.setURI(new URI(httGet.getURI().toString() + "?" + str)); 
		} catch (Exception e) {
			log.error("http get param error.", e);
			throw new CommonRuntimeException(-1,"http get param error." + e);
		}
		 
		/*RequestConfig requestConfig = RequestConfig.custom()  
		        .setConnectTimeout(50000).setConnectionRequestTimeout(100000)  
		        .setSocketTimeout(50000).build();  
		httGet.setConfig(requestConfig);*/  
		String res = exec(httGet);
		log.debug("weixinHttpclient get result {}",res);
		JSONObject ret = JSONObject.fromObject(res);
		
		if(ret.containsKey("errcode") && ret.getInt("errcode") != 0) {
			String errmsg = ret.getString("errmsg");
			Integer errCode = ret.getInt("errcode");
			log.warn("responseText:" + res);
			throw new CommonRuntimeException(100,errmsg);
		}
		
		return ret;
		
	}
	
	public File getforFile(String url, Map<String, String> paramMap, String savePath) throws CommonRuntimeException {
		HttpGet httGet = new HttpGet(url);
	
		List<NameValuePair> list = new ArrayList<NameValuePair>();

		if(paramMap!=null && paramMap.size()>0){
			for(Entry<String,String> entry : paramMap.entrySet()){
				list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
			}
		}
		String str;
		try {
			str = EntityUtils.toString(new UrlEncodedFormEntity(list));
			httGet.setURI(new URI(httGet.getURI().toString() + "?" + str)); 
		} catch (Exception e) {
			log.error("http get param error.", e);
			throw new CommonRuntimeException(100,"http get param error."+ e);
		}
		 

		File file = execForFile(httGet, savePath);
	
		return file;
		
	}
	
	
	private String exec(HttpRequestBase requet) throws CommonRuntimeException {
		HttpResponse response = null;
		try {
			int errorCnt = 0;
			StringBuffer responseStr = new StringBuffer();
			while(errorCnt < 3) {
				response = httpclient.execute(requet);
				
				StatusLine status = response.getStatusLine();
				HttpEntity entity = response.getEntity();
				Header[] headers = response.getHeaders("Content-disposition");
				String contentDisposition = "";
				if(headers != null && headers.length > 0) {
					contentDisposition = headers[0].getValue();					
				}
				if ("".equals(contentDisposition.toLowerCase()) && status.getStatusCode() == 200) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
					
					String line = null;
					 while ((line = reader.readLine()) != null) {
					     log.debug("weixinHttpclient exec result {}",line);
						 responseStr.append(line);
					 }
					 reader.close();
//					responseStr = EntityUtils.toString(entity);
					 EntityUtils.consume(entity);
					break;
				} else {
					errorCnt++;
					EntityUtils.consume(entity);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						log.warn("thread sleep error.", e);
					}
					if(errorCnt == 3) {						
						log.error("Call to the server failed."
								+ status.getReasonPhrase());
						throw new CommonRuntimeException(-1,"Call to the server failed."+status.getReasonPhrase());
					} 
				}
			}
			
			return responseStr.toString().trim();
		} catch (ClientProtocolException e) {
			log.error("Call to the server failed. 2", e);
			throw new CommonRuntimeException(-1,"Call to the server failed." + e);
		} catch (IOException e) {
			log.error("Call to the server failed. 3", e);
			throw new CommonRuntimeException(-1,"Call to the server failed." + e);
		} finally {
			if(response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			httpclient.getConnectionManager().shutdown();
		}
	}
	
	private File execForFile(HttpRequestBase requet, String savePath) throws CommonRuntimeException {
		HttpResponse response = null;
		try {
			int errorCnt = 0;
			StringBuffer responseStr = new StringBuffer();
			while(errorCnt < 3) {
				response = httpclient.execute(requet);
				
				StatusLine status = response.getStatusLine();
				HttpEntity entity = response.getEntity();
				Header[] headers = response.getHeaders("Content-disposition");
				String contentDisposition = "";
				File storeFile = null;
				if(headers != null && headers.length > 0) {
					contentDisposition = headers[0].getValue();
					String fileName = StringUtils.substringBetween(contentDisposition, "\"");
					storeFile = new File(savePath + File.separator + fileName);
				}
				if("".equals(contentDisposition)) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
					
					String line = null;
					 while ((line = reader.readLine()) != null) {
						 responseStr.append(line);
					 }
					 reader.close();
					throw new CommonRuntimeException(-1,"Call to the server failed."+responseStr.toString());
				}
				if (contentDisposition.startsWith("attachment;") && status.getStatusCode() == 200) {
					InputStream in = entity.getContent();
					FileOutputStream output = new FileOutputStream(storeFile);  

					byte[] b = new byte[1024];  
					int len = 0;
					 while ((len = in.read(b)) != -1) {
						 output.write(b,0,len);  
					 }
					 in.close();  
					 output.close();  
					return storeFile;
				} else {
					errorCnt++;
					EntityUtils.consume(entity);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						log.warn("thread sleep error.", e);
					}
					if(errorCnt == 3) {						
						log.error("Call to the server failed."
								+ status.getReasonPhrase());
						throw new CommonRuntimeException(-1,"Call to the server failed."+status.getReasonPhrase());
					} 
				}
			}
			
			return null;
		} catch (ClientProtocolException e) {
			log.error("Call to the server failed. 2", e);
			throw new CommonRuntimeException(-1,"Call to the server failed."+ e);
		} catch (IOException e) {
			log.error("Call to the server failed. 3", e);
			throw new CommonRuntimeException(-1,"Call to the server failed." + e);
		} finally {
			if(response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			httpclient.getConnectionManager().shutdown();
		}
	}
	
	
	public void setTimeout(Integer timeout){
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
	}
}
