package cn.appsys.service.appcategory;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.appsys.dao.appcategory.AppCategoryMapper;
import cn.appsys.pojo.AppCategory;
@Service
public class AppcategoryServiceImpl implements AppcategoryService {
	@Resource
	private AppCategoryMapper appCategoryMapper;
	public List<AppCategory> getAppCategoryListByParentId(Integer parentId)
			throws Exception {
		List<AppCategory> getCategoryList=null;
		getCategoryList = appCategoryMapper.getAppCategoryListByParentId(parentId);
		return getCategoryList;
	}
	
	
}
