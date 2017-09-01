package com.chufang.entity;

import java.util.Collections;
import java.util.List;

public class Page<T> {
	public static final int DEFAULT_PAGE_NO = 1;
    public static final int DEFAULT_PAGE_SIZE = 12;
    
    /** 当前请求页 */
    private int pageNo = DEFAULT_PAGE_NO;
    
    /** 每页显示记录数 */
    private int pageSize = DEFAULT_PAGE_SIZE;
    
    /** 总记录数 */
    private int totalCount = 0;
    
    /** 总页数 */
    private int totalPage = 0;
    
    /** 数据 */
    private List<T> data = Collections.emptyList();
    
    /** 默认构造函数 */
    public Page(){
    }
    
    public Page(int pageNumber){
        this.pageNo = pageNumber;
    }
    
    public Page(int pageIndex, int pageSize){
        this.pageNo = pageIndex;
        this.pageSize = pageSize;
    }
    
    public Page(Page<?> page){
        this.pageNo = page.pageNo;
        this.pageSize = page.pageSize;
        this.totalCount = page.totalCount;
        this.totalPage = page.totalPage;
    }
    
    /** 
     * toString Method
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Page{")
            .append("  pageNo=").append(pageNo)
            .append(", pageSize=").append(pageSize)
            .append(", totalCount=").append(totalCount)
            .append(", totalPage=").append(totalPage)
            .append(", data=").append(data)
            .append("  }\n");
        
        return sb.toString();
    }
    
    public int getPageBegin() {
        int myPageNo = (pageNo > 0 ? pageNo : DEFAULT_PAGE_NO);
        int myPageSize = (pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE);
        int begin = (myPageNo - 1) * myPageSize;
        return begin;
    }
    
    // -------------------------------- 以下为Getter/Setter方法 -------------------------------- //

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo > 0) {
            this.pageNo = pageNo;
        } else {
        	this.pageNo = 1;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        int totalPage = totalCount / pageSize;
        
        if (totalCount % pageSize > 0) {
        	totalPage = totalPage + 1;
        }
        setTotalPage(totalPage);
    }

    public List<T> getData() {
        if (data == null) {
            data = Collections.emptyList();
        }
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
