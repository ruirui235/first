package cn.appsys.service.appinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoService {
	public List<AppInfo> getAppInfoList(String softwareName,
			                            Integer status,
			                            Integer FlatformId,
			                            Integer categoryLevel1,
										Integer categoryLevel2,
										Integer categoryLevel3,
										Integer devId,
										Integer currentPageNo,
										Integer pageSize)throws Exception;

	public int getAppInfoCount(String softwareName, Integer status,Integer FlatformId,Integer categoryLevel1,
								Integer categoryLevel2,Integer categoryLevel3,Integer devId)throws Exception;
	public boolean appInfoAdd(AppInfo appInfo)throws Exception;
	public AppInfo selectAPKNameExist(String APKName)throws Exception;
	public AppInfo updateAppInfoById(String id)throws Exception;
	public boolean updateAppInfoByApp(AppInfo appInfo)throws Exception;
	public AppInfo getAppInfoById(Integer id)throws Exception;
	public boolean deleteAppInfoById(Integer delId)throws Exception;
	public boolean deleteFile(Integer id)throws Exception;
	public int getAppInfoCountCheck(String softwareName,
									Integer flatformId,
									Integer categoryLevel1,
									 Integer categoryLevel2,
									Integer categoryLevel3)throws Exception;
	public List<AppInfo> getAppInfoCheckList(String softwareName,
											Integer flatformId,
											 Integer categoryLevel1,
											 Integer categoryLevel2,
											Integer categoryLevel3,
											Integer currentPageNo,
											Integer pageSize)throws Exception;
}
