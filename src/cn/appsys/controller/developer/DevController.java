package cn.appsys.controller.developer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.developer.DeveloperService;
import cn.appsys.tools.Constants;
@Controller
@RequestMapping("/developer")
public class DevController {
		private Logger logger=Logger.getLogger(DevController.class);
		@Resource
		private DeveloperService developerService;
		@RequestMapping(value="/devlogin")
		public String login(){
			return "devlogin";
		} 
		@RequestMapping(value="/dologin",method=RequestMethod.POST)
		public String doLogin(@RequestParam String devCode,@RequestParam String devPassword,
							HttpSession session,HttpServletRequest request)throws Exception{
			logger.debug("doLogin===========================");
			//调用service方法，进行匹配
			DevUser user=developerService.login(devCode, devPassword);
			if(null !=user){
				session.setAttribute(Constants.DEV_USER_SESSION, user);
				return "redirect:/flatform/developer/main.html";
			}else{
				request.setAttribute("error", "用户名或密码不正确");
				return "devlogin";
			}
		}
		
		@RequestMapping(value="logout")
		public String logout(HttpSession session){
			session.removeAttribute(Constants.DEV_USER_SESSION);
			return "devlogin";
		}
		
		
		
		
}
