package cn.appsys.controller.backend;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.appsys.pojo.Backend;
import cn.appsys.service.backend.BackendService;
import cn.appsys.tools.Constants;
@Controller
@RequestMapping("/back")
public class LoginController {
		private Logger logger=Logger.getLogger(LoginController.class);
		@Resource
		private BackendService backendService;
		@RequestMapping(value="/login")
		public String login(){
			return "login";
		} 
		@RequestMapping(value="/dologin",method=RequestMethod.POST)
		public String doLogin(@RequestParam String userCode,@RequestParam String userPassword,
							HttpSession session,HttpServletRequest request)throws Exception{
			logger.debug("doLogin===========================");
			//调用service方法，进行匹配
			Backend backend=backendService.login(userCode,userPassword);
			if(null !=backend){
				session.setAttribute(Constants.USER_SESSION, backend);
				return "redirect:/manager/backend/main.html";
			}else{
				request.setAttribute("error", "用户名或密码不正确");
				return "login";
			}
		}
		
		@RequestMapping(value="logout.html")
		public String logout(HttpSession session){
			session.removeAttribute(Constants.USER_SESSION);
			return "login";
		}
		
		
		
		
}
