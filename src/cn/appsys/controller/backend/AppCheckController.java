package cn.appsys.controller.backend;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.appsys.dao.backend.BackendMapper;
import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DevUser;
import cn.appsys.pojo.Dictionary;
import cn.appsys.service.appcategory.AppcategoryService;
import cn.appsys.service.appinfo.AppInfoService;
import cn.appsys.service.appversion.AppVersionService;
import cn.appsys.service.developer.DeveloperService;
import cn.appsys.service.dictionary.DictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;


@Controller
@RequestMapping("/manager/backend")
public class AppCheckController {
	private Logger logger=Logger.getLogger(AppCheckController.class);
	@Resource
	private BackendMapper backMapper;
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private DeveloperService developerService;
	@Resource
	private AppcategoryService appCategoryService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private AppVersionService appVersionService;
	@RequestMapping(value="/main.html")
	public String main(){
		return "/backend/main";
	}
	@RequestMapping(value=("/login.html"))
	public String login(){
		return "login";
	}
	@RequestMapping(value = ("/applist"))
	public String getAppInfoCheckList(
			Model model,
			@RequestParam(value = "querySoftwareName", required = false) String querySoftwareName,
			@RequestParam(value = "queryFlatformId", required = false) Integer queryFlatformId,
			@RequestParam(value = "categoryLevel1", required = false) Integer categoryLevel1,
			@RequestParam(value = "categoryLevel2", required = false) Integer categoryLevel2,
			@RequestParam(value = "categoryLevel3", required = false) Integer categoryLevel3,
			@RequestParam(value = "pageIndex", required = false) String pageIndex,
			HttpSession session) throws Exception {
		List<AppInfo> appInfoCheckList=null;
		List<Dictionary> platformList = null;
		List<AppCategory> categoryLevel1List = null;
		List<AppCategory> categoryLevel2List = null;
		List<AppCategory> categoryLevel3List = null;
		// 设置页面容量
		Integer pageSize = Constants.pageSize;
		// 当前页码
		int currentPageNo1 = 1;
		if (!"".equals(pageIndex) && pageIndex != null) {
			try {
				currentPageNo1 = Integer.parseInt(pageIndex);
			} catch (Exception e) {
				return "403.jsp";
			}

		}
		// 总数量（表）
		int totalCount = appInfoService.getAppInfoCountCheck(querySoftwareName,
				 queryFlatformId, categoryLevel1, categoryLevel2,
				categoryLevel3);

		// 总页数
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo1);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		// 控制首页和尾页
		if (currentPageNo1 < 1) {
			currentPageNo1 = 1;
		} else if (currentPageNo1 > totalPageCount) {
			currentPageNo1 = totalPageCount;
		}
		int dataIndex = (currentPageNo1 - 1) * pageSize;
		appInfoCheckList = appInfoService.getAppInfoCheckList(querySoftwareName, queryFlatformId,
								categoryLevel1, categoryLevel2, categoryLevel3, dataIndex, pageSize);
		platformList = dictionaryService.getAllByTypeCode("APP_FLATFORM");
		categoryLevel1List = getChildCategory(null);
		if (categoryLevel1 != null) {
			categoryLevel2List = getChildCategory(categoryLevel1);
		}
		if (categoryLevel2 != null) {
			categoryLevel3List = getChildCategory(categoryLevel2);
		}
		model.addAttribute("pages", pages);
		model.addAttribute("appInfoCheckList", appInfoCheckList);
		model.addAttribute("flatformList", platformList);
		model.addAttribute("querySoftwareName", querySoftwareName);
		System.out.println("*****************"+querySoftwareName);
		model.addAttribute("queryFlatformId", queryFlatformId);
		model.addAttribute("categoryLevel1", categoryLevel1);
		model.addAttribute("categoryLevel2", categoryLevel2);
		model.addAttribute("categoryLevel3", categoryLevel3);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("categoryLevel2List", categoryLevel2List);
		model.addAttribute("categoryLevel3List", categoryLevel3List);
		return "/backend/applist";
	}
	
	public List<AppCategory> getChildCategory(Integer parentId) {
		List<AppCategory> childList = null;
		try {
			childList = appCategoryService
					.getAppCategoryListByParentId(parentId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return childList;

	}
	@RequestMapping("/categorylevellist.json")
	@ResponseBody
	public List<AppCategory> getNextCategory(Integer pid) {
		List<AppCategory> childList = null;
		try {
			childList = getChildCategory(pid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return childList;

	}
	
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public String updeteAppVersionById(Integer aid, Integer vid, Model model) {
		AppInfo appInfo = null;
		AppVersion appVersion = null;
		try {
			appVersion = appVersionService.getAppVersionById(vid);
			appInfo = appInfoService.getAppInfoById(aid);
			model.addAttribute("appVersion", appVersion);
			model.addAttribute("appInfo", appInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "backend/appcheck";

	}
	@RequestMapping(value="/checksave",method = RequestMethod.POST)
	public String checksave(Integer status,AppInfo appInfo) throws Exception{
		if(status==2){
			appInfo.setStatus(2);
		}else{
			appInfo.setStatus(3);
		}
		if(appInfoService.updateAppInfoByApp(appInfo)){
			return "redirect:/manager/backend/applist";
		}
		return "backend/appcheck";

	}
}
