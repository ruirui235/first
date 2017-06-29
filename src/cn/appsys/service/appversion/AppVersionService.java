package cn.appsys.service.appversion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionService {
	public boolean getAppVersionAdd(AppVersion appVersion)throws Exception;
	public List<AppVersion>  getAppVersionByAppId(Integer appId)throws Exception;
	public AppVersion selectAppVersion(Integer appId,String versionNo)throws Exception;
	public boolean updeteAppVersionApp(AppVersion appVersion)throws Exception;
	public AppVersion getAppVersionById(Integer id)throws Exception;
	public boolean deleteFile(Integer id)throws Exception;
}
