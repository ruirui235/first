package cn.appsys.service.appversion;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appversion.AppVersionMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
@Service
public class AppVersionServiceImpl implements AppVersionService{
	@Resource
	private AppVersionMapper appVersionMapper;
	public boolean getAppVersionAdd(AppVersion appVersion) throws Exception {
		boolean flag = false;
		
		try {
			//开启JDBC事务管理
			int updateRows = appVersionMapper.getAppVersionAdd(appVersion);
	
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
	public List<AppVersion> getAppVersionByAppId(Integer id) throws Exception {
		List<AppVersion>  appVersion = null;
		appVersion=appVersionMapper.getAppVersionByAppId(id);
		return appVersion;
	}
	public AppVersion selectAppVersion(Integer appId, String versionNo)
			throws Exception {
		AppVersion selectAppVersion=null;
		selectAppVersion=appVersionMapper.selectAppVersion(appId, versionNo);
		return selectAppVersion;
	}
	
	public boolean updeteAppVersionApp(AppVersion appVersion) throws Exception {
		int updetaVersion=appVersionMapper.updeteAppVersionApp(appVersion);
		if(updetaVersion>0){
			
			return true;
		}
		return false;
	}
	public AppVersion getAppVersionById(Integer id)
			throws Exception {
			AppVersion selectAppVersion=null;
			selectAppVersion=appVersionMapper.getAppVersionById(id);
		return selectAppVersion;
	}
	public boolean deleteFile(Integer id) throws Exception {
		boolean flag = true;
		//先删除该条记录的上传附件
		AppVersion appVersion= appVersionMapper.getAppVersionById(id);
		if( appVersion.getApkLocPath() != null && !appVersion.getApkLocPath().equals("")){
			//删除服务器上个人证件照
			File file = new File( appVersion.getDownloadLink());
			if(file.exists()){
				flag = file.delete();
			}
		}
		if(flag){
			if(appVersionMapper.deleteFile(appVersion) < 1)
				flag = false;
		}
		return flag;
	}
}
