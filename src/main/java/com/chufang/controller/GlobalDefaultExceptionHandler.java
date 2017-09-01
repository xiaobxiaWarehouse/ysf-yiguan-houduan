package com.chufang.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chufang.consts.ChufangConsts;

@ControllerAdvice
class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";
  
    private final static Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);
    
    @InitBinder  
    public void initBinder(WebDataBinder binder) {  

    }  
    
    @ExceptionHandler(value = Exception.class)
    public @ResponseBody JSONObject defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
//        // 如果异常使用了ResponseStatus注解，那么重新抛出该异常，Spring框架会处理该异常。 
//        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
//            throw e;
    	e.printStackTrace();
        logger.error("exception (" + e.toString() + ")");
        JSONObject rst = new JSONObject();
        rst.put(ChufangConsts.RC, ChufangConsts.FAIL);
        rst.put(ChufangConsts.ERR_MSG, "系统异常，请稍后再试");
        return rst;
    }
}