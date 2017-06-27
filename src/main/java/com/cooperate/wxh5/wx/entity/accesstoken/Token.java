/*
 * Automatically generated
 */

package com.cooperate.wxh5.wx.entity.accesstoken;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Token implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "Token";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_ACCESS_TOKEN = "accessToken";
	public static final String ALIAS_EXPIRES_IN = "expiresIn";
	public static final String ALIAS_CREATE_TIME = "createTime";
	
	
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * id       db_column: id 
     */	
	
	private java.lang.String id;
    /**
     * accessToken       db_column: access_token 
     */	
	
	private java.lang.String accessToken;
    /**
     * expiresIn       db_column: expires_in 
     */	
	
	private java.lang.String expiresIn;
    /**
     * createTime       db_column: createTime 
     */	
	
	private java.util.Date createTime;
	//columns END

	public Token(){
	}

	public Token(
		java.lang.String id
	){
		this.id = id;
	}

	public void setId(java.lang.String value) {
		this.id = value;
	}
	
	public java.lang.String getId() {
		return this.id;
	}
	public void setAccessToken(java.lang.String value) {
		this.accessToken = value;
	}
	
	public java.lang.String getAccessToken() {
		return this.accessToken;
	}
	public void setExpiresIn(java.lang.String value) {
		this.expiresIn = value;
	}
	
	public java.lang.String getExpiresIn() {
		return this.expiresIn;
	}
	
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("AccessToken",getAccessToken())
			.append("ExpiresIn",getExpiresIn())
			.append("CreateTime",getCreateTime())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Token == false) return false;
		if(this == obj) return true;
		Token other = (Token)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

