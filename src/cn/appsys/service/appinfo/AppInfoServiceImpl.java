package cn.appsys.service.appinfo;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appinfo.AppInfoMapper;
import cn.appsys.pojo.AppInfo;
@Service
public class AppInfoServiceImpl implements AppInfoService{
	@Resource
	private AppInfoMapper appInfoMapper;
	public List<AppInfo> getAppInfoList(String querySoftwareName, Integer queryStatus,
			Integer queryFlatformId, Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3, Integer devId, Integer currentPageNo,
			Integer pageSize) throws Exception {
			List<AppInfo> appInfoList=null;
			appInfoList=appInfoMapper.getAppInfoList(querySoftwareName, queryStatus, queryFlatformId, categoryLevel1, categoryLevel2, categoryLevel3, devId, currentPageNo, pageSize);
		return appInfoList;
	}
	public int getAppInfoCount(String softwareName, Integer queryStatus,
			Integer queryFlatformId,
			Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3, Integer devId) throws Exception {
		// TODO Auto-generated method stub
		int count=appInfoMapper.getAppInfoCount(softwareName, queryStatus, queryFlatformId, categoryLevel1, categoryLevel2, categoryLevel3, devId);
		return count;
	}
	public boolean appInfoAdd(AppInfo appInfo) throws Exception {
			boolean flag = false;
		
		try {
			//开启JDBC事务管理
			int updateRows = appInfoMapper.appInfoAdd(appInfo);
	
			if(updateRows > 0){
				flag = true;
				System.out.println("add success!");
			}else{
				System.out.println("add failed!");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return flag;
	}
	public AppInfo selectAPKNameExist(String APKName) throws Exception {
		AppInfo appInfo =new AppInfo();
		try {
			appInfo = appInfoMapper.selectAPKNameExist(APKName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return appInfo;
	}
	public AppInfo updateAppInfoById(String id) throws Exception {
		AppInfo appInfo = null;
		appInfo = appInfoMapper.updateAppInfoById(id);
		return appInfo;
	}
	public boolean updateAppInfoByApp(AppInfo appInfo) throws Exception {
		 int upappinfo=appInfoMapper.updateAppInfoByApp(appInfo);
		 if(upappinfo>0){
			 return true;
		 }
		return false;
	}
	public AppInfo getAppInfoById(Integer id) throws Exception {
		AppInfo appInfo=null;
		appInfo=appInfoMapper.getAppInfoById(id);
		return appInfo;
	}
	public boolean deleteAppInfoById(Integer delId) throws Exception {
		boolean flag = true;
		//先删除该条记录的上传附件
		AppInfo  appinfo= appInfoMapper.getAppInfoById(delId);
		if(appinfo.getLogoLocPath() != null && !appinfo.getLogoLocPath().equals("")){
			//删除服务器上个人证件照
			File file = new File(appinfo.getLogoLocPath());
			if(file.exists()){
				flag = file.delete();
			}
		}
		if(flag){
			if(appInfoMapper.deleteAppInfoById(delId) < 1)
				flag = false;
		}
		return flag;
	}
	public boolean deleteFile(Integer id) throws Exception {
		boolean flag = true;
		//先删除该条记录的上传附件
		AppInfo  appinfo= appInfoMapper.getAppInfoById(id);
		if(appinfo.getLogoLocPath() != null && !appinfo.getLogoLocPath().equals("")){
			//删除服务器上个人证件照
			File file = new File(appinfo.getLogoLocPath());
			if(file.exists()){
				flag = file.delete();
			}
		}
		if(flag){
			if(appInfoMapper.deleteFile(appinfo) < 1)
				flag = false;
		}
		return flag;
	}
	public int getAppInfoCountCheck(String softwareName, Integer flatformId,
			Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3) throws Exception {
		 int CountCheck=appInfoMapper.getAppInfoCountCheck(softwareName, flatformId, categoryLevel1, categoryLevel2, categoryLevel3);
		return CountCheck;
	}
	public List<AppInfo> getAppInfoCheckList(String querySoftwareName,
			Integer queryFlatformId, Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3, Integer currentPageNo, Integer pageSize)
			throws Exception {
		List<AppInfo> appInfoCheckList=null;
			appInfoCheckList=appInfoMapper.getAppInfoCheckList(querySoftwareName, queryFlatformId, categoryLevel1, categoryLevel2, categoryLevel3, currentPageNo, pageSize);
		return appInfoCheckList;
	}
	
}
