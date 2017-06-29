package cn.appsys.service.dictionary;

import java.util.List;


import cn.appsys.pojo.Dictionary;

public interface DictionaryService {
	public List<Dictionary> getAllByTypeCode(String typeCode)throws Exception;
}
