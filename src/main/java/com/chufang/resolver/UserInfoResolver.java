package com.chufang.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.chufang.annotation.UserInfo;
import com.chufang.consts.ChufangConsts;
import com.chufang.entity.ext.UserDetail;

import net.sf.json.JSONObject;
public class UserInfoResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(UserInfo.class) == null ? false : true;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object obj = webRequest.getAttribute(ChufangConsts.USER_INFO_KEY, RequestAttributes.SCOPE_REQUEST);
		if (obj != null) {
			JSONObject tmpInfo = JSONObject.fromObject(obj);
			return new UserDetail(tmpInfo.getLong(ChufangConsts.USER_ID), 
					tmpInfo.getString(ChufangConsts.USER_ROLE), tmpInfo.getInt(ChufangConsts.ADMIN),
					tmpInfo.getInt(ChufangConsts.YIGUAN_ID), tmpInfo.getString(ChufangConsts.LONGON_USER_NAME));
		}
		return null;
	}

}
