package com.zhcs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import com.zhcs.dao.RedetailDao;
import com.zhcs.entity.RedetailEntity;
import com.zhcs.service.RedetailService;


//*****************************************************************************
/**
 * <p>Title:RedetailServiceImpl</p>
 * <p>Description: 维修明细</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: 深圳市智慧城市管家信息科技有限公司 </p>
 * @author 刘晓东 - Alter
 * @version v1.0 2017年2月23日
 */
//*****************************************************************************

@Service("redetailService")
public class RedetailServiceImpl implements RedetailService {
	@Autowired
	private RedetailDao redetailDao;
	
	@Override
	public RedetailEntity queryObject(Long id){
		return redetailDao.queryObject(id);
	}
	
	@Override
	public List<RedetailEntity> queryList(Map<String, Object> map){
		return redetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return redetailDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(RedetailEntity redetail){
		redetailDao.save(redetail);
	}
	
	@Override
	@Transactional
	public void update(RedetailEntity redetail){
		redetailDao.update(redetail);
	}
	
	@Override
	@Transactional
	public void delete(Long id){
		redetailDao.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		redetailDao.deleteBatch(ids);
	}

	@Override
	public List<RedetailEntity> queryListByReid(Long reid) {
		return redetailDao.queryListByReid(reid);
	}
	
}
