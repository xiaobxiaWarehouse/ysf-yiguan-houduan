package com.chufang.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.chufang.annotation.ID;
import com.chufang.annotation.Ignore;
import com.chufang.annotation.Table;
import com.chufang.enumtype.ConditionType;
import com.chufang.parameter.Condition;
import com.chufang.rowmaper.CustomerMapper;

@Component
public class BaseRepository<T> extends PageRespository {
	
	@Resource
	protected JdbcTemplate jdbcTemplate;

	private final static Logger logger = LoggerFactory.getLogger(BaseRepository.class);
	
	protected Map<String, Object> getUpdateParam(String[] fields, T t) {
		Map<String, Object> rst = new HashMap<String, Object>();
		Class<?> cls = t.getClass();
		Method method = null;
		try {
			for (String field : fields) {
				method = cls.getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1).toLowerCase());
				rst.put(field, method.invoke(t));
			}
		} catch (Exception e) {
			logger.error("fail to geneate update params.cls = " + cls.getName(), e);
			return null;
		}
		return rst;
	}
	
	protected Map<String, Object> getUpdateParam(String[] fields, Object[] values) {
		Map<String, Object> rst = new HashMap<String, Object>();
		if (fields.length != values.length) {
			return null;
		}
		for (int i = 0; i < fields.length; i++ ) {
			rst.put(fields[i], values[i]);
		}
		return rst;
	}
	
	protected List<T> queryObjByCondition(Condition condition, Class<T> cls) {
		Table table = cls.getAnnotation(Table.class);
		if (table != null) {
			StringBuilder sql = new StringBuilder();
			List<Object> values = new ArrayList<>();
			sql.append("select * from " + table.value()).append(" ");
			if (condition != null) {
				sql.append(buildWhereCondition(condition, values));
			}
			return this.jdbcTemplate.query(sql.toString(),values.toArray(), new CustomerMapper<T>(cls));
		} else {
			logger.error("no table or params. table=" + cls.getName() + ",params = " + condition);
			return null;
		}
	}
	
	protected int deleteByCondition(Condition condition, Class<T> cls) {
		Table table = cls.getAnnotation(Table.class);
		if (table != null && condition != null) {
			StringBuilder sql = new StringBuilder();
			List<Object> values = new ArrayList<>();
			sql.append("delete from " + table.value()).append(" ");
	 
			if (condition != null) {
				sql.append(buildWhereCondition(condition, values));
			}
			return this.jdbcTemplate.update(sql.toString(),values.toArray());
		} else {
			logger.error("no table or condition. table=" + cls.getName() + ",condition = " + condition);
			return -1;
		}
	}
	
	protected int updateByCondition(Condition condition, Map<String, Object> params, Class<T> cls) {
		Table table = cls.getAnnotation(Table.class);
		if (table != null && params != null) {
			StringBuilder sql = new StringBuilder();
			List<Object> values = new ArrayList<>();
			sql.append("update " + table.value()).append(" set ");
			
			sql.append(buildSetSql(params, values));
			
			if (params != null) {
				sql.append(buildWhereCondition(condition, values));
			}
			return this.jdbcTemplate.update(sql.toString(),values.toArray());
		} else {
			logger.error("no table or params. table=" + cls.getName() + ",params = " + params);
			return -1;
		}
	}
	
	protected long insertObjectAndGetAutoIncreaseId(final T obj, final List<String> definedFields) {
		final List<Field> fields = new ArrayList<Field>();
		final String sql = getInsertSql(obj, fields, definedFields);

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				int index = 1;
				for (Field field : fields) {
					try {
						ps.setObject(index, obj.getClass().getMethod("get" + upperFirstChar(field.getName())).invoke(obj));
						index++;
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				}
				return ps;
			}
		}, keyHolder);

		return keyHolder.getKey().longValue();
	}
	
	protected void insertObject(final T obj, final List<String> definedFields) {
		final List<Field> fields = new ArrayList<Field>();
		final String sql = getInsertSql(obj, fields, definedFields);

		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				int index = 1;
				for (Field field : fields) {
					try {
						ps.setObject(index, obj.getClass().getMethod("get" + upperFirstChar(field.getName())).invoke(obj));
						index++;
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				}
				return ps;
			}
		});
	}

	private String getInsertSql(Object obj, List<Field> fieldList, List<String> definedFields) {
		String tableName = null;
		Table table = obj.getClass().getAnnotation(Table.class);
		if (table != null) {
			tableName = table.value();
		} else {
			tableName = obj.getClass().toString().substring(obj.getClass().toString().lastIndexOf(".") + 1);
		}

		StringBuffer sql = new StringBuffer();
		StringBuffer prepareSql = new StringBuffer();
		sql.append("insert into " + tableName + " (");
		prepareSql.append(" values (");
		boolean hasFields = false;
		Field[] fields = obj.getClass().getDeclaredFields();
		Field field = null;
		for (int i= 0 ; i < fields.length; i++) {
			field = fields[i];
			if (definedFields == null || 
					definedFields.contains(field.getName())) {  //只插入需要插入的字段，其他字段使用默认值
 				if (field.getAnnotation(ID.class) == null && field.getAnnotation(Ignore.class) == null) { // 主键自增的不插入
					sql.append(field.getName()).append(",");
					hasFields = true;
					prepareSql.append("?,");
					fieldList.add(field);
				}
			}
		}
		if (hasFields) { // 去掉最后一个,
			sql.deleteCharAt(sql.length() - 1).append(")");
			prepareSql.deleteCharAt(prepareSql.length() - 1);
		}

		sql.append(prepareSql).append(")");
		return sql.toString();
	}
	
	private String buildSetSql(Map<String, Object> params, List<Object> values) {
		StringBuilder sql = new StringBuilder();
		for (Entry<String, Object> param : params.entrySet()) {
			sql.append(param.getKey()).append("=?,");
			values.add(param.getValue());
		}
		return sql.toString().substring(0, sql.length()-1);
	}
	
	private String buildWhereCondition(Condition condition, List<Object> values) {
		StringBuilder sql = new StringBuilder();
		sql.append(" where ");
		Iterator<String> conKeys = condition.getConditions().keySet().iterator();
		String key = null;
		Map<String, Object> params = null;
		String andOr = "and";
		while (conKeys.hasNext()) {
			key = conKeys.next();
			params = condition.getConditions().get(key);
			if (key.equals(ConditionType.OR.getType())) {
				sql.append("(");
				andOr = "or";
			} else {
				andOr = "and";
			}
			if (key.equals(ConditionType.IN.getType())) {
				for (Entry<String, Object> param : params.entrySet()) {
					sql.append(param.getKey()).append(" " + key + " (");
					if (param.getValue() instanceof int[]) {
						int[] valList = (int[])param.getValue();
						for (int val : valList) {
							sql.append("?,");
							values.add(val);
						}
					}
					sql.append("-99999) and ");
				}
			} else {
				for (Entry<String, Object> param : params.entrySet()) {
					sql.append(param.getKey()).append(" " + key + " ? " + andOr + " ");
					if (key.equals(ConditionType.LIKE.getType())) {
						values.add("%" + param.getValue() + "%");
					} else {
						values.add(param.getValue());
					}
				}
			}
			if (andOr.equals("or")) {
				sql.append(" 1=1)");
			}
		}
		sql.append("1=1");
		return sql.toString();
	}
	
	private String upperFirstChar(String str) {
		return str.substring(0,1).toUpperCase() + str.substring(1);
	}
}
