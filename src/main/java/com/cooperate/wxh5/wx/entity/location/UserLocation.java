package com.cooperate.wxh5.wx.entity.location;

/**
 * 
* @ClassName: UserLocation 
* @Description: TODO
* @author zhrt
* @date 2017年6月27日 上午10:45:49
* @Version 1.0
 */
public class UserLocation {

	private String ToUserName;
	
	private String FromUserName;
	
	private String CreateTime;
	
	private String MsgType;
	
	private String Event;
	
	private String Latitude;
	
	private String Longitude;
	
	private String Precision;

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getPrecision() {
		return Precision;
	}

	public void setPrecision(String precision) {
		Precision = precision;
	}
	
	
}
