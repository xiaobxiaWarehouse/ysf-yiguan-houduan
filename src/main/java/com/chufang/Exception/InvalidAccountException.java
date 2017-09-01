package com.chufang.Exception;

import com.chufang.consts.ErrorCode;

public class InvalidAccountException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int getErrorCode() {
		return ErrorCode.INVAILD_ACCOUNT;
	}
	public String getErrorMsg() {
		return "账号错误";
	}
	public InvalidAccountException() {
		
	}
}
