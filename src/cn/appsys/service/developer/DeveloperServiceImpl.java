package cn.appsys.service.developer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.developer.DeveloperMapper;
import cn.appsys.pojo.DevUser;
@Service
public class DeveloperServiceImpl implements DeveloperService{
	@Resource
	private DeveloperMapper developerMapper;
	public DevUser login(String devCode, String devPassword) throws Exception {
		DevUser user=null;
		user=developerMapper.getloginDeveloper(devCode);
		if(null!=user){
			if(!user.getDevPassword().equals(devPassword)){
				user=null;
			}
		}
		return user;
	}

}
