package com.cooperate.wxh5.wx.util;

import java.security.GeneralSecurityException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.alibaba.druid.pool.DruidDataSource;

public class EncryptDruidDataSource extends DruidDataSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 将配置文件中的字符串转换成二进制数据后在解密
	 * @param password 加密后的字符串
	 */
	@Override
	 public void setPassword(String password) {
		// key 二进制密钥
		byte[] key = new byte[] { 85, 88, -84, 100, 77, -78, -68, -53, 42, 44, 42, -114, -43, -64, 107, 122 };
		try {
			// 将配置文件中的字符串转换成二进制数据后在解密
			byte[] decryptData = AESCoder.decrypt(Hex.decodeHex(password.toCharArray()), key);
			this.password = new String(decryptData);

		} catch (GeneralSecurityException e2) {
			e2.printStackTrace();
		} catch (DecoderException e2) {
			e2.printStackTrace();
		}
    }
	
//	@Override
//	 public String getPassword() {
//        return password;
//    }

}
