package com.chufang.repository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.chufang.entity.Page;


public class PageRespository {
	
	public final static String COUNT_COLUMN_ALIAS = "ROW_COUNT_";
	
	@Resource
	protected JdbcTemplate jdbcTemplate;
	
	public PageRespository() {
		
	}
	
	public PageRespository(JdbcTemplate template) {
		this.jdbcTemplate = template;
	}
	
	public <T> Page<T> query(String sql, Page<T> page, RowMapper<T> rowMapper) {
		if (page == null) {
			page = new Page<T>();
		}

		int totalCount = jdbcTemplate.queryForObject(getCountSql(sql), Integer.class);
		page.setTotalCount(totalCount);

		List<T> data = jdbcTemplate.query(
				getPaggingSql(sql, page.getPageNo(), page.getPageSize()),
				rowMapper);
		page.setData(data);

		return page;
	}

	public <T> Page<T> query(String sql, Object[] para, Page<T> page,
			RowMapper<T> rowMapper) {
		if (page == null) {
			page = new Page<T>();
		}

		int totalCount = jdbcTemplate.queryForObject(getCountSql(sql), para, Integer.class);
		page.setTotalCount(totalCount);

		List<T> data = jdbcTemplate.query(getPaggingSql(sql, page.getPageNo(), page.getPageSize()), para,rowMapper);
		page.setData(data);
		return page;
	}
	
	protected String getCountSql(String querySql) {
		return "select count(1) " + COUNT_COLUMN_ALIAS + " from (" + querySql
				+ ") countview";
	}
	
	private String getPaggingSql(String querySql, int pageNo, int pageSize) {

		int myPageNo = (pageNo > 0 ? pageNo : 1);
		int myPageSize = (pageSize > 0 ? pageSize : Page.DEFAULT_PAGE_SIZE);
		long begin = 0l + (myPageNo - 1) * myPageSize;
		//long end = 0l + begin + myPageSize;
		long end = myPageSize;

		querySql = querySql.trim();
		boolean isForUpdate = false;
		if (querySql.toLowerCase().endsWith(" for update")) {
			querySql = querySql.substring(0, querySql.length() - 11);
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer(querySql.length() + 100);
		pagingSelect
				.append("SELECT * FROM ( ");
		pagingSelect.append(querySql);
		pagingSelect.append(" ) row_ limit ").append(begin).append(",").append(end);

		if (isForUpdate) {
			pagingSelect.append(" for update");
		}

		return pagingSelect.toString();
	}
	
	protected <T> T getSingleData(List<T> list) {
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
