package cn.appsys.dao.backend;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.Backend;


public interface BackendMapper {
	/**
	 * 通过userCode获取Backend
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public Backend getIndexBackend(@Param("userCode") String userCode) throws Exception;
}
