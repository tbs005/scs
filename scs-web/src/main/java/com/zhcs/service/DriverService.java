package com.zhcs.service;

import com.zhcs.entity.DriverEntity;

import java.util.List;
import java.util.Map;

//*****************************************************************************
/**
 * <p>Title:DriverService</p>
 * <p>Description: 司机管理</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: 深圳市智慧城市管家信息科技有限公司 </p>
 * @author 刘晓东 - Alter
 * @version v1.0 2017年2月23日
 */
//*****************************************************************************
public interface DriverService {
	
	DriverEntity queryObject(Long id);
	
	List<DriverEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DriverEntity driver);
	
	void update(DriverEntity driver);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	List<Map<String, Object>> queryFullList(Map<String, Object> map);
	
	
	
	
	List<Map<String, Object>> queryFullList1(Map<String, Object> map);
}
