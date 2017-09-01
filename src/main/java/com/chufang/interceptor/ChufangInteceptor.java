package com.chufang.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.chufang.annotation.NoAuth;
import com.chufang.consts.ChufangConsts;
import com.chufang.consts.ErrorCode;

import net.sf.json.JSONObject;

@Component
public class ChufangInteceptor  extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			if (((HandlerMethod) handler).getMethodAnnotation(NoAuth.class) == null) {
				Object userInfo = request.getSession().getAttribute(ChufangConsts.USER_INFO_KEY);
				if (userInfo == null) {
					outputError(response, ErrorCode.FORCE_LOGOUT);
					return false;
				}
				request.setAttribute(ChufangConsts.USER_INFO_KEY, userInfo.toString());
			}
		}
		return true;
	}
		
	private void outputError(HttpServletResponse response, int rst) {
		JSONObject obj = new JSONObject();
		obj.put(ChufangConsts.RC, rst);
		response.setContentType("application/json");
		try {
			response.getOutputStream().write(obj.toString().getBytes());
		} catch (IOException e) {
		}
	}
}
