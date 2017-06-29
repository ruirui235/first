package cn.appsys.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.appsys.pojo.Backend;
import cn.appsys.pojo.DevUser;
import cn.appsys.tools.Constants;


public class SysInterceptor extends HandlerInterceptorAdapter {
	private Logger logger=Logger.getLogger(SysInterceptor.class);
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handle){
		logger.debug("SysInterceptor preHandle!");
		HttpSession session=request.getSession();
		Backend backend = (Backend) session.getAttribute(Constants.USER_SESSION);
		DevUser user = (DevUser) session.getAttribute(Constants.DEV_USER_SESSION);
		if(null!=user){
			return true;
		}else if(null!= backend){
					return true;
		}else{
			try {
				response.sendRedirect(request.getContextPath()+"/403.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		
	}

}
