
package com.chufang.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class CookieUtil {
	
	public final static String VCODE = "chufang_vcode";
	
	public static String getCookieValue(HttpServletRequest req, String key) {
		Cookie[] cookies = req.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie ck : cookies) {
			if (key.equals(ck.getName())) {
				return ck.getValue();
			}
		}
		return null;
	}
	
	 public static Cookie makeCookie(String key, String value, boolean secure, String domain) {
        Cookie ck = new Cookie(key, value);
        ck.setPath("/chufang");
        if (StringUtils.isNotEmpty(domain)) {
        	ck.setDomain(domain);
        }
        ck.setSecure(secure);
        ck.setMaxAge(-1); 
        return ck;
    }
	 
	 public static Cookie makeCookie(String key, String value, boolean secure, String domain, int expire) {
	        Cookie ck = new Cookie(key, value);
	        ck.setPath("/chufang");
	        if (StringUtils.isNotEmpty(domain)) {
	        	ck.setDomain(domain);
	        }
	        ck.setSecure(secure);
	        ck.setMaxAge(expire); 
	        return ck;
	    }
	
	public static void clearCookie(HttpServletResponse response, String key) {
		Cookie cookie = new Cookie(key, "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
