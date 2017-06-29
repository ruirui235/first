package cn.appsys.dao.appinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoMapper {
	/**
	 * 信息列表查询
	 * @param softwareName
	 * @param status
	 * @param flatformId
	 * @param categoryLevel1
	 * @param categoryLevel2
	 * @param categoryLevel3
	 * @param devId
	 * @param currentPageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<AppInfo> getAppInfoList(@Param("softwareName") String softwareName,
			                            @Param("status") Integer status,
										@Param("flatformId") Integer flatformId,
										@Param("categoryLevel1") Integer categoryLevel1,
										@Param("categoryLevel2") Integer categoryLevel2,
										@Param("categoryLevel3") Integer categoryLevel3,
										@Param("devId") Integer devId,
										@Param("from")Integer currentPageNo,
										@Param("pageSize") Integer pageSize)throws Exception;
	/**
	 * 分类查询
	 * @param softwareName
	 * @param status
	 * @param flatformId
	 * @param categoryLevel1
	 * @param categoryLevel2
	 * @param categoryLevel3
	 * @param devId
	 * @return
	 * @throws Exception
	 */
	public int getAppInfoCount(@Param("softwareName") String softwareName,
					            @Param("status") Integer status,
								@Param("flatformId") Integer flatformId,
								@Param("categoryLevel1") Integer categoryLevel1,
								@Param("categoryLevel2") Integer categoryLevel2,
								@Param("categoryLevel3") Integer categoryLevel3,
								@Param("devId") Integer devId)throws Exception;
	public int appInfoAdd(AppInfo appInfo)throws Exception;
	public AppInfo selectAPKNameExist(@Param("APKName") String APKName)throws Exception;
	public AppInfo updateAppInfoById(@Param("id")String id)throws Exception;
	public int updateAppInfoByApp(AppInfo appInfo)throws Exception;
	public AppInfo getAppInfoById(@Param("id")Integer id)throws Exception;
	public int deleteAppInfoById(@Param("id")Integer delId)throws Exception;
	public int  deleteFile(AppInfo appInfo)throws Exception;
	public int getAppInfoCountCheck(@Param("softwareName") String softwareName,
									@Param("flatformId") Integer flatformId,
									@Param("categoryLevel1") Integer categoryLevel1,
									@Param("categoryLevel2") Integer categoryLevel2,
									@Param("categoryLevel3") Integer categoryLevel3)throws Exception;
	public List<AppInfo> getAppInfoCheckList(@Param("softwareName") String softwareName,
									@Param("flatformId") Integer flatformId,
									@Param("categoryLevel1") Integer categoryLevel1,
									@Param("categoryLevel2") Integer categoryLevel2,
									@Param("categoryLevel3") Integer categoryLevel3,
									@Param("from")Integer currentPageNo,
									@Param("pageSize") Integer pageSize)throws Exception;
	
}
