package cn.appsys.service.backend;

import cn.appsys.pojo.Backend;

public interface BackendService {
	/**
	 * 后台用户登录
	 * @param userCode
	 * @param userPassword
	 * @return
	 * @throws Exception
	 */
	public Backend login(String userCode,String userPassword) throws Exception;
}
