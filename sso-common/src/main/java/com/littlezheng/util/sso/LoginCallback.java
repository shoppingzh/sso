package com.littlezheng.util.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class LoginCallback {

	/**
	 * ��¼�ɹ��Ļص�
	 * @param req
	 * @param resp
	 */
	public abstract void onSuccess(HttpServletRequest req, HttpServletResponse resp, String username, String token);
	
}
