package com.chufang.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chufang.annotation.NoAuth;

@Controller
public class TestController {
	@RequestMapping(value = "/emoji", method = RequestMethod.POST)
	@NoAuth
	public @ResponseBody String emoji(HttpServletRequest request, String content) {
		emojiTest(content);
		return "OK";
	}
	
	public void emojiTest(String content) {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
//			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/shiwubao?useUnicode=true&characterEncoding=utf-8");
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/shiwubao?useUnicode=true", "shiwubao","shiwubao");
			PreparedStatement st = connection.prepareStatement("insert into test(name) values (?)");
			st.setString(1, content);
			st.execute();
			st.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
}
