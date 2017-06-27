package com.cooperate.wxh5.wx.pagination.model;

import java.io.Serializable;

public class PageInfo implements Serializable {
	
    private static final long serialVersionUID = 587754556498974978L;
    
    // 页数
    private Integer pageNo = 1;

    // 每页记录数
    private Integer pageSize = 20;
    
    // 起始记录数
    private Integer offset=0;
    
    public PageInfo() {
    }
    
    public PageInfo(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
    
	public Integer getPageNo() {
		return pageNo;
	}
	
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public Integer getOffset() {
		if (this.pageNo < 1) {
			this.pageNo = 1;
		}
		if (this.pageSize < 1) {
			this.pageSize = 10;
		}
		this.offset = (pageNo-1) * getPageSize();
		return offset;
	}
	
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
}