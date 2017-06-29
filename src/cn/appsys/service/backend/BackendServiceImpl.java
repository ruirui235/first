package cn.appsys.service.backend;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.backend.BackendMapper;
import cn.appsys.pojo.Backend;
@Service
public class BackendServiceImpl implements BackendService  {
	@Resource
	private BackendMapper backendMapper;
	public Backend login(String userCode, String userPassword) throws Exception {
		Backend backend=null;
		backend = backendMapper.getIndexBackend(userCode);
		if(null != backend){
			if(!backend.getUserPassword().equals(userPassword)){
				backend=null;
			}
		}
		return backend;
	}

}
