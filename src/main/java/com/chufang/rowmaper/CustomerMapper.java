package com.chufang.rowmaper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class CustomerMapper<T> implements RowMapper<T> {
	
	protected Class<?> entityClazz;
	protected List<String> fields = new ArrayList<String>();  //存储非转换得字段数据库字段。 默认规则即数据库字段和POJO字段名称一致
	protected Map<String, String> transferFieldsMapper = new HashMap<String, String>(); //存储列明需要转化的。 value代表数据库得字段。key代表POJO得字段
	protected List<Method> setMethodList = new ArrayList<Method>();
	
	private String fieldName = null;
	
	public CustomerMapper(Class<T> entityClazz) {
		//初始化数据库列和POJO得关系
		this.entityClazz =entityClazz;
		Field[] fs = entityClazz.getDeclaredFields();
		Annotation tmpA = null;
		for (Field field : fs) {
			if(field.isAnnotationPresent(Column.class)){//是否使用MyAnno注解
				tmpA = field.getAnnotation(Column.class);
				transferFieldsMapper.put(field.getName(),((Column)tmpA).name());
			} else {
				fields.add(field.getName());
			}
		}
		
		Method[] methods = entityClazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("set")) {
				setMethodList.add(method);
			}
		}
	}
	
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		try {
			return setValues(entityClazz.newInstance(), rs, rowNum);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public T setValues(Object t, ResultSet rs, int i) {
		try {
			String columnName = null, parameterType = null;
			for (Method method : setMethodList) {
				fieldName = method.getName().substring("set".length());
				fieldName = fieldName.substring(0,1).toLowerCase() + fieldName.substring(1); //首字母小写转换
				if (transferFieldsMapper.containsKey(fieldName)) {
					columnName = transferFieldsMapper.get(fieldName);
				} else {
					columnName = fieldName.substring(0,1).toLowerCase() + fieldName.substring(1);
				}
				if (isExistColumn(rs, columnName)) {
					parameterType = method.getGenericParameterTypes()[0].toString().toLowerCase();
					//基本类型，如果有新得类型则添加。。。
					if (parameterType.endsWith("string")) {
						method.invoke(t, rs.getString(columnName));
					} else if (parameterType.endsWith("long")) {
						method.invoke(t, rs.getLong(columnName));
					} else if (parameterType.endsWith("int")) {
						method.invoke(t, rs.getInt(columnName));
					} else if (parameterType.endsWith("date")) {
						if (rs.getTimestamp(columnName) != null) {
							method.invoke(t, new Date(rs.getTimestamp(columnName).getTime()));
						}
					} else if (parameterType.endsWith("double")) {
						method.invoke(t, rs.getDouble(columnName));
					} else if (parameterType.endsWith("float")) {
						method.invoke(t, rs.getFloat(columnName));
					}
				}
			}
			return (T) t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private boolean isExistColumn(ResultSet rs, String column) {
		try {
			if (rs.findColumn(column) > 0) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

}
