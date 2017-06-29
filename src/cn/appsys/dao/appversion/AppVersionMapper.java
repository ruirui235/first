package cn.appsys.dao.appversion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;

public interface AppVersionMapper {
		public int getAppVersionAdd(AppVersion appVersion)throws Exception;
		public List<AppVersion>  getAppVersionByAppId(@Param("appId") Integer appId)throws Exception;
		public AppVersion selectAppVersion(@Param("appId") Integer appId,@Param("versionNo") String versionNo)throws Exception;
		public int updeteAppVersionApp(AppVersion appVersion)throws Exception;
		public AppVersion getAppVersionById(@Param("id")Integer id)throws Exception;
		public int  deleteFile(AppVersion appVersion)throws Exception;
}
