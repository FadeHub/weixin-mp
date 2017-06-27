package com.cooperate.wxh5.wx.entity.message.resp;

/**
 * 语音消息
 * @author zhrt
 *
 */
public class VoiceMessage extends BaseMessage{

	private Voice Voice;

	public Voice getVoice() {
		return Voice;
	}

	public void setVoice(Voice voice) {
		Voice = voice;
	}
	
	
}
