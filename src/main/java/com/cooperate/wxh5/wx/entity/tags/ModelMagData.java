package com.cooperate.wxh5.wx.entity.tags;

/**
 * 
* @ClassName: ModelMagData 
* @Description: TODO
* @author zhrt
* @date 2017年6月26日 上午11:11:58
* @Version 1.0
 */
public class ModelMagData {

	private String value;
	
	private String color;

	public ModelMagData() {
	}

	public ModelMagData(String value, String color) {
		super();
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
}
