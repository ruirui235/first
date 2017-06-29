package cn.appsys.service.developer;

import cn.appsys.pojo.DevUser;

public interface DeveloperService {
	/**
	 * 前台用户登录
	 * @param devCode
	 * @param devPassword
	 * @return
	 * @throws Exception
	 */
	public DevUser login(String devCode,String devPassword) throws Exception;
}
