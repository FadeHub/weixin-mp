package com.cooperate.wxh5.wx.entity.message.event;

import com.cooperate.wxh5.wx.entity.message.resp.BaseMessage;
import com.cooperate.wxh5.wx.entity.message.resp.Image;

/**
 * 图片消息
 * @author zhrt
 *
 */
public class ImageMessage extends BaseMessage{

	private Image Image;

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}
	
}
