package com.chufang.test.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class GenerateEntity {
	
	private final static String huanhang = "\n";
	
	private final static String tab = "\t";
	
	private final static String project = "chufang";
	
	public static void main(String[] arg) throws Exception{
		Class.forName("org.mariadb.jdbc.Driver");
		GenerateEntity en = new GenerateEntity();
		Connection conn = DriverManager.getConnection("jdbc:mysql://rm-bp1i38g6s0kf370xlo.mysql.rds.aliyuncs.com:3306/ysf_dev?useUnicode=true&characterEncoding=utf-8", "ysf_dev", "Bkysf_dev");
		
		String[] tables = new String[]{"ysf_yg_tiaojicfyp"};
		String[] Vos = new String[]{"YG_TIAOJICFYP"};
//		String entityPath = "C:\\whlworkstation\\myworkspace\\chufang\\src\\main\\java\\com\\chufang\\entity\\";
		String entityPath = "D:\\myworkstation\\chufang\\src\\main\\java\\com\\chufang\\entity\\";
		String pkgName = "com.chufang.entity";
		
		String sql = "SELECT column_name, data_type, column_default,numeric_scale,numeric_precision,extra FROM information_schema.`COLUMNS`  WHERE TABLE_SCHEMA='ysf_dev' AND TABLE_NAME='%s'";
		ResultSet rs = null;
		List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
		for (int i = 0; i < tables.length; i++) {
			rs = conn.createStatement().executeQuery(String.format(sql, tables[i]));
			
			while (rs.next()) {
				columns.add(en.new ColumnInfo(rs.getString("column_name"), rs.getString("data_type"), rs.getString("numeric_scale"),rs.getString("numeric_precision"),rs.getString("extra")));	
			}
			if (rs != null) {
				rs.close();
			}
			
			en.generateFile(tables[i], entityPath + Vos[i] + ".java",columns, pkgName, Vos[i]);
		}
		if (conn != null) {
			conn.close();
		}
	}
	
	private void generateFile(String table, String file, List<ColumnInfo> columns, String pkgName, String className) throws Exception {
		FileOutputStream out = new FileOutputStream(new File(file));
		String packageLine = "package " + pkgName + ";" + huanhang + huanhang;
		String importTable = "import com." + project + ".annotation.Table;"+ huanhang + "import com." + project + ".annotation.ID;" + huanhang;
		String line = null;
		out.write(packageLine.getBytes("utf-8"));
		out.write(importTable.getBytes("utf-8"));
		List<String> dataTypes = new ArrayList<>();
		for (ColumnInfo info : columns) {
			if (!dataTypes.contains(info.dataType)) {
				dataTypes.add(info.dataType);
			}
		}
		List<String> importList = new ArrayList<>();
		for (String dataType : dataTypes) {
			if ((dataType.equals("datetime") || dataType.equals("date")) && !importList.contains("date")) {
				line = "import java.util.Date;" + huanhang + huanhang;
				out.write(line.getBytes("utf-8"));
				importList.add("date");
			}
		}
		out.write(("@Table(\"" + table + "\")" + huanhang).getBytes("utf-8"));
		String classDeclare = "public class " + className + " {" + huanhang;
		out.write(classDeclare.getBytes("utf-8"));
		for (ColumnInfo info : columns) {
			if (info.extra != null && info.extra.equalsIgnoreCase("auto_increment")) {
				line = tab + "@ID" + huanhang;
				out.write(line.getBytes("utf-8"));
			}
			line = tab + "private " + getType(info.dataType, info.numericValue, info.numericPrecision) + " " + info.columnName.toLowerCase() + ";" + huanhang + huanhang;
			out.write(line.getBytes("utf-8"));
		}
		
		for (ColumnInfo info : columns) {
			out.write(generateGetSet(info.dataType, info.columnName, info.numericValue, info.numericPrecision).getBytes("utf-8"));
		}
		
		out.write("}".getBytes());
		out.flush();
		out.close();
	}
	
	private String generateGetSet(String dataType, String columnName, String numericValue, String numericPrecision) {
		StringBuffer sb = new StringBuffer();
		sb.append(tab + "public " + getType(dataType, numericValue, numericPrecision) + " get" + columnName.substring(0,1).toUpperCase() + columnName.substring(1).toLowerCase() + "() {").append(huanhang);
		sb.append(tab + tab + "return this." + columnName.toLowerCase() + ";" + huanhang);
		sb.append(tab + "}" + huanhang + huanhang);
		
		sb.append(tab + "public void set" + columnName.substring(0,1).toUpperCase() + columnName.substring(1).toLowerCase() + "(" + getType(dataType,numericValue, numericPrecision) 
			+ " " + columnName.toLowerCase() + ") {").append(huanhang);
		sb.append(tab + tab + "this." + columnName.toLowerCase() + " = " + columnName.toLowerCase() + ";" + huanhang);
		sb.append(tab + "}" + huanhang);
		
		return sb.toString();
	}
	
	private String getType(String dataType, String numericValue, String numericPrecision) {
		if (dataType.equals("bigint")) {
			return "long";
		} else if (dataType.equals("int")) {
			if (Integer.parseInt(numericPrecision) > 7) {
				return "long";
			}
			return "int";
		} else if (dataType.equals("datetime") || dataType.equals("date")) {
			return "Date";
		} else if (dataType.equals("varchar") || dataType.equals("char") || dataType.equals("text")) {
			return "String";
		} else if (dataType.equals("decimal")){
			if (StringUtils.isEmpty(numericValue) || "0".equals(numericValue)) {
				if (Integer.parseInt(numericPrecision) > 7) {
					return "long";
				} else {
					return "int";
				}
			} else {
				return "double";
			}
		} else {
			return "UNKNOWN";
		}
	}
	
	class ColumnInfo {
		public String columnName;
		public String dataType;
		public String numericValue;
		public String numericPrecision;
		public String extra;
		public ColumnInfo(String cl, String dt, String numericValue, String numericPrecision, String extra) {
			this.columnName = cl;
			this.dataType = dt;
			this.numericValue = numericValue;
			this.extra = extra;
			this.numericPrecision = numericPrecision;
		}
	}
}
