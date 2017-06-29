package cn.appsys.controller.developer;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

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
@RequestMapping("/flatform/developer")
public class AppController {
	private Logger logger = Logger.getLogger(AppController.class);
	@Resource
	private DeveloperService developerService;
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private AppcategoryService appCategoryService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private AppVersionService appVersionService;

	@RequestMapping(value = "/main.html")
	public String main() {
		return "/developer/frame";
	}

	@RequestMapping(value = ("/appinfolist"))
	public String getAppInfoList(
			Model model,
			@RequestParam(value = "querySoftwareName", required = false) String querySoftwareName,
			@RequestParam(value = "queryStatus", required = false) Integer queryStatus,
			@RequestParam(value = "queryFlatformId", required = false) Integer queryFlatformId,
			@RequestParam(value = "categoryLevel1", required = false) Integer categoryLevel1,
			@RequestParam(value = "categoryLevel2", required = false) Integer categoryLevel2,
			@RequestParam(value = "categoryLevel3", required = false) Integer categoryLevel3,
			@RequestParam(value = "pageIndex", required = false) String pageIndex,
			HttpSession session) throws Exception {
		List<AppInfo> appinfoList = null;
		List<Dictionary> statusList = null;
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
		int devId = ((DevUser) session.getAttribute(Constants.DEV_USER_SESSION))
				.getId();
		// 总数量（表）
		int totalCount = appInfoService.getAppInfoCount(querySoftwareName,
				queryStatus, queryFlatformId, categoryLevel1, categoryLevel2,
				categoryLevel3, devId);

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
		appinfoList = appInfoService.getAppInfoList(querySoftwareName,
				queryStatus, queryFlatformId, categoryLevel1, categoryLevel2,
				categoryLevel3, devId, dataIndex, pageSize);
		statusList = dictionaryService.getAllByTypeCode("APP_STATUS");
		platformList = dictionaryService.getAllByTypeCode("APP_FLATFORM");
		categoryLevel1List = getChildCategory(null);
		if (categoryLevel1 != null) {
			categoryLevel2List = getChildCategory(categoryLevel1);
		}
		if (categoryLevel2 != null) {
			categoryLevel3List = getChildCategory(categoryLevel2);
		}
		model.addAttribute("pages", pages);
		model.addAttribute("appinfoList", appinfoList);
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatformList", platformList);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("queryFlatformId", queryFlatformId);
		model.addAttribute("categoryLevel1", categoryLevel1);
		model.addAttribute("categoryLevel2", categoryLevel2);
		model.addAttribute("categoryLevel3", categoryLevel3);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("categoryLevel2List", categoryLevel2List);
		model.addAttribute("categoryLevel3List", categoryLevel3List);
		return "/developer/appinfolist";
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

	@RequestMapping(value = "/appinfoadd", method = RequestMethod.GET)
	public String appInfoAdd(@ModelAttribute("appInfo") AppInfo appInfo) {
		return "developer/appinfoadd";
	}

	@RequestMapping(value = "/appinfoaddsave", method = RequestMethod.POST)
	public String appInfoAddSava(
			AppInfo appInfo,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "a_logoPicPath", required = false) MultipartFile attach)
			throws Exception {
		String logoPicPath = null;
		String logoPath = null;
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			logger.info("uploadfiles path ============================" + path);
			String oldFileName = attach.getOriginalFilename();
			logger.info("uploadfiles oldFileName ============================"
					+ oldFileName);
			String prefix = FilenameUtils.getExtension(oldFileName);
			logger.info("uploadfiles prefix ============================"
					+ prefix);
			int filesize = 500000;
			logger.info("uploadfiles size ============================"
					+ attach.getSize());
			if (attach.getSize() > filesize) {
				request.setAttribute("uploadFileError", "* 上传大小不得超过 500KB");
				return "developer/appInfoadd";
			} else if (prefix.equalsIgnoreCase("jpg")
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg")
					|| prefix.equalsIgnoreCase("peng")) {
				String fileName = System.currentTimeMillis()
						+ RandomUtils.nextInt(1000000) + "_Personal.jpg";
				logger.debug("new fileName============================="
						+ fileName);
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", "*上传失败");
					return "developer/appInfoadd";
				}
				logoPath = path + File.separator + fileName;
				logoPicPath = "/AppInfoSystem/statics/uploadfiles/" + fileName;
			} else {
				request.setAttribute("uploadFileError", "*上传图片格式不正确");
				return "developer/appInfoadd";
			}
		}
		appInfo.setCreatedBy(((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId());
		int devId = ((DevUser) session.getAttribute(Constants.DEV_USER_SESSION))
				.getId();
		System.out.println("*******" + devId);
		appInfo.setCreationDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoPath);
		appInfo.setDevId(devId);
		appInfo.setModifyDate(new Date());
		if (appInfoService.appInfoAdd(appInfo)) {
			return "redirect:/flatform/developer/appinfolist";
		}
		return "developer/appInfoadd";
	}

	@RequestMapping(value = "/apkexist.json", method = RequestMethod.GET)
	@ResponseBody
	public Object APKNameIsExit(@RequestParam String APKName) throws Exception {
		logger.debug("APKNameIsExit APKName:" + APKName);
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if (StringUtils.isNullOrEmpty(APKName)) {
			resultMap.put("APKName", "empty");
		} else {
			AppInfo appInfo = appInfoService.selectAPKNameExist(APKName);
			if (null != appInfo) {
				resultMap.put("APKName", "exist");
			} else {
				resultMap.put("APKName", "noexist");
			}

		}
		return JSONArray.toJSONString(resultMap);
	}

	public List<Dictionary> getFeilDictionary(String typeCode) {
		List<Dictionary> dictionaryList = null;
		try {
			dictionaryList = dictionaryService.getAllByTypeCode(typeCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dictionaryList;
	}

	@RequestMapping("/datadictionarylist.json")
	@ResponseBody
	public List<Dictionary> getNextDictionary(String tcode) {
		List<Dictionary> dictionaryList = null;
		try {
			dictionaryList = getFeilDictionary(tcode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dictionaryList;
	}

	@RequestMapping(value = "/appinfomodify", method = RequestMethod.GET)
	public String updateAppInfoById(Integer id, Model model) {
		try {
			AppInfo appInfo = appInfoService.getAppInfoById(id);
			model.addAttribute("appInfo", appInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/appinfomodify";

	}

	@RequestMapping(value = ("/appinfomodifysave"), method = RequestMethod.POST)
	public String appModify(
			AppInfo appInfo,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "a_logoPicPath", required = false) MultipartFile attach) {
		String logoPicPath = null;
		String logoPath = null;
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			logger.info("uploadfiles path ============================" + path);
			String oldFileName = attach.getOriginalFilename();
			logger.info("uploadfiles oldFileName ============================"
					+ oldFileName);
			String prefix = FilenameUtils.getExtension(oldFileName);
			logger.info("uploadfiles prefix ============================"
					+ prefix);
			int filesize = 500000;
			logger.info("uploadfiles size ============================"
					+ attach.getSize());
			if (attach.getSize() > filesize) {
				request.setAttribute("uploadFileError", "* 上传大小不得超过 500KB");
				return "developer/appinfomodify";
			} else if (prefix.equalsIgnoreCase("jpg")
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg")
					|| prefix.equalsIgnoreCase("peng")) {
				String fileName = System.currentTimeMillis()
						+ RandomUtils.nextInt(1000000) + "_Personal.jpg";
				logger.debug("new fileName============================="
						+ fileName);
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", "*上传失败");
					return "developer/appinfomodify";
				}
				logoPath = path + File.separator + fileName;
				logoPicPath = "/AppInfoSystem/statics/uploadfiles/" + fileName;
			} else {
				request.setAttribute("uploadFileError", "*上传图片格式不正确");
				return "developer/appinfomodify";
			}
		}
		try {
			appInfo.setModifyBy(((DevUser) session
					.getAttribute(Constants.DEV_USER_SESSION)).getId());
			int devId = ((DevUser) session
					.getAttribute(Constants.DEV_USER_SESSION)).getId();
			appInfo.setLogoPicPath(logoPicPath);
			appInfo.setLogoLocPath(logoPath);
			appInfo.setDevId(devId);
			appInfo.setModifyDate(new Date());
			if (appInfoService.updateAppInfoByApp(appInfo)) {
				return "redirect:/flatform/developer/appinfolist";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/appinfomodify";

	}

	@RequestMapping(value = "/appinfoview/{id}", method = RequestMethod.GET)
	public String view(@PathVariable String id, Model model,
			HttpServletRequest request) {
		logger.debug("view id===================== " + id);
		AppInfo appInfo = new AppInfo();
		List<AppVersion> appVersionList = null;
		try {
			appVersionList = appVersionService.getAppVersionByAppId(Integer
					.parseInt(id));
			appInfo = appInfoService.getAppInfoById(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("appVersionList", appVersionList);
		model.addAttribute("appInfo", appInfo);
		return "developer/appinfoview";
	}

	@RequestMapping(value = "delapp.json", method = RequestMethod.GET)
	@ResponseBody
	public Object deleteAppInfo(@RequestParam String id) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if (StringUtils.isNullOrEmpty(id)) {
			resultMap.put("delResult", "notexist");
		} else {
			try {
				if (appInfoService.deleteAppInfoById(Integer.parseInt(id)))
					resultMap.put("delResult", "true");
				else
					resultMap.put("delResult", "false");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}

	@RequestMapping(value = "/appversionadd", method = RequestMethod.GET)
	public String getAppVersionAdd(@RequestParam Integer id, Model model)
			throws Exception {
		List<AppVersion> appVersionList = null;
		appVersionList = appVersionService.getAppVersionByAppId(id);
		AppVersion appVersion = new AppVersion();
		AppInfo appinfo = appInfoService.getAppInfoById(id);
		appVersion.setAppId(appinfo.getId());
		/* appVersion.setAppId(id); */
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appVersionList", appVersionList);
		return "developer/appversionadd";
	}

	@RequestMapping(value = "/appversionaddsave", method = RequestMethod.POST)
	public String getAppVersionAddsave(
			AppVersion appVersion,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "a_downloadLink", required = false) MultipartFile attach)
			throws Exception {
		String apkFileName = null;
		String apklogoPath = null;
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			logger.info("uploadfiles path ============================" + path);
			String oldFileName = attach.getOriginalFilename();
			logger.info("uploadfiles oldFileName ============================"
					+ oldFileName);
			String prefix = FilenameUtils.getExtension(oldFileName);
			logger.info("uploadfiles prefix ============================"
					+ prefix);
			int filesize = 500000000;
			logger.info("uploadfiles size ============================"
					+ attach.getSize());
			if (attach.getSize() > filesize) {
				request.setAttribute("uploadFileError", "* 上传大小不得超过 500KB");
				return "developer/appversionadd";
			} else if (prefix.equalsIgnoreCase("apk")) {
				AppInfo appinfo = appInfoService.getAppInfoById(appVersion
						.getAppId());
				String fileName = appinfo.getAPKName() + "-"
						+ appVersion.getVersionNo() + prefix;
				appVersion.setApkFileName(fileName);
				logger.debug("new fileName============================="
						+ fileName);
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", "*上传失败");
					return "developer/appversionadd";
				}
				apklogoPath = path + File.separator + fileName;
				apkFileName = "/AppInfoSystem/statics/uploadfiles/" + fileName;
			} else {
				request.setAttribute("uploadFileError", "*上传图片格式不正确");
				return "developer/appversionadd";
			}
		}
		appVersion.setCreatedBy(((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setApkLocPath(apklogoPath);
		appVersion.setDownloadLink(apkFileName);
		appVersion.setModifyDate(new Date());
		if (appVersionService.getAppVersionAdd(appVersion)) {
			AppVersion appVsersion1 = appVersionService.selectAppVersion(
					appVersion.getAppId(), appVersion.getVersionNo());
			AppInfo appInfo = appInfoService.getAppInfoById(appVersion
					.getAppId());
			appInfo.setVersionId(appVsersion1.getId());
			appInfoService.updateAppInfoByApp(appInfo);
			return "redirect:/flatform/developer/appinfolist";
		}
		return "developer/appversionadd";
	}

	@RequestMapping(value = "/appversionmodify", method = RequestMethod.GET)
	public String updeteAppVersionById(Integer vid, Integer aid, Model model) {
		List<AppVersion> appVersionList = null;
		AppVersion appVersion = null;
		try {
			appVersion = appVersionService.getAppVersionById(vid);
			appVersionList = appVersionService.getAppVersionByAppId(aid);
			model.addAttribute("appVersion", appVersion);
			model.addAttribute("appVersionList", appVersionList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/appversionmodify";

	}

	@RequestMapping(value = "/appversionmodifysave", method = RequestMethod.POST)
	public String versionModifysave(
			AppVersion appVersion,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "attach", required = false) MultipartFile attach)
			throws Exception {
		String apkFileName = null;
		String apklogoPath = null;
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			logger.info("uploadfiles path ============================" + path);
			String oldFileName = attach.getOriginalFilename();
			logger.info("uploadfiles oldFileName ============================"
					+ oldFileName);
			String prefix = FilenameUtils.getExtension(oldFileName);
			logger.info("uploadfiles prefix ============================"
					+ prefix);
			int filesize = 500000000;
			logger.info("uploadfiles size ============================"
					+ attach.getSize());
			if (attach.getSize() > filesize) {
				request.setAttribute("uploadFileError", "* 上传大小不得超过 500MB");
				return "developer/appversionmodify";
			} else if (prefix.equalsIgnoreCase("apk")) {
				AppInfo appinfo = appInfoService.getAppInfoById(appVersion
						.getAppId());
				String fileName = appinfo.getAPKName() + "-"
						+ appVersion.getVersionNo() + prefix;
				appVersion.setApkFileName(fileName);
				logger.debug("new fileName============================="
						+ fileName);
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", "*上传失败");
					return "developer/appversionmodify";
				}
				apklogoPath = path + File.separator + fileName;
				apkFileName = "/AppInfoSystem/statics/uploadfiles/" + fileName;
			} else {
				request.setAttribute("uploadFileError", "*上传图片格式不正确");
				return "developer/appversionmodify";
			}
		}
		appVersion.setCreatedBy(((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setApkLocPath(apklogoPath);
		appVersion.setDownloadLink(apkFileName);
		appVersion.setModifyDate(new Date());
		if (appVersionService.updeteAppVersionApp(appVersion)) {
			AppVersion appVsersion1 = appVersionService.selectAppVersion(
					appVersion.getAppId(), appVersion.getVersionNo());
			AppInfo appInfo = appInfoService.getAppInfoById(appVersion
					.getAppId());
			appInfo.setVersionId(appVsersion1.getId());
			appInfoService.updateAppInfoByApp(appInfo);
			return "redirect:/flatform/developer/appinfolist";
		}
		return "developer/appversionmodify";

	}

	@RequestMapping(value = "/{appId}/sale.json", method = RequestMethod.PUT)
	@ResponseBody
	public Object saleSwitch(@PathVariable String appId) throws Exception {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		AppInfo appInfo = appInfoService.getAppInfoById(Integer.valueOf(appId));
		AppVersion appVersion = appVersionService.getAppVersionById(appInfo
				.getVersionId());
		if (appInfo.getStatus() == 2 || appInfo.getStatus() == 5) {
			appInfo.setStatus(4);
			appInfo.setOnSaleDate(new Date());
			appVersion.setPublishStatus(2);
			resultMap.put("resultMap", "success");
		} else if (appInfo.getStatus() == 4) {
			appInfo.setStatus(5);
			appInfo.setOnSaleDate(new Date());
			appVersion.setPublishStatus(1);
			resultMap.put("resultMap", "success");
		} else {
			resultMap.put("resultMap", "failed");
		}
		appInfoService.updateAppInfoByApp(appInfo);
		appVersionService.updeteAppVersionApp(appVersion);
		return JSONArray.toJSON(resultMap);

	}

	@RequestMapping(value = "delfile.json", method = RequestMethod.GET)
	@ResponseBody
	public Object delfile(@RequestParam String id, String flag) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if ("logo".equals(flag)) {
			if (StringUtils.isNullOrEmpty(id)) {
				resultMap.put("result", "failed");
			} else {
				try {
					if (appInfoService.deleteFile(Integer.parseInt(id))) {
						resultMap.put("result", "success");
					} else {
						resultMap.put("result", "failed");
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if ("apk".equals(flag)) {
			if (StringUtils.isNullOrEmpty(id)) {
				resultMap.put("result", "failed");
			} else {
				try {
					if (appVersionService.deleteFile(Integer.parseInt(id))) {
						resultMap.put("result", "success");
					} else {
						resultMap.put("result", "failed");
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return JSONArray.toJSONString(resultMap);

	}
}