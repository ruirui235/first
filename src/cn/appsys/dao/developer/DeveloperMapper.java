package cn.appsys.dao.developer;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DevUser;

public interface DeveloperMapper {
		public DevUser getloginDeveloper(@Param("devCode") String devCode) throws Exception;
}
