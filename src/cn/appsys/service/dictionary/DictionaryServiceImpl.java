package cn.appsys.service.dictionary;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.dictionary.DictionaryMapper;
import cn.appsys.pojo.Dictionary;
@Service
public class DictionaryServiceImpl implements DictionaryService {
	@Resource
	private DictionaryMapper dictionaryMapper;
	public List<Dictionary> getAllByTypeCode(String typeCode) throws Exception {
		List<Dictionary> dictionaryList=null;
		dictionaryList=dictionaryMapper.getAllByTypeCode(typeCode);
		return dictionaryList;
	}

}
