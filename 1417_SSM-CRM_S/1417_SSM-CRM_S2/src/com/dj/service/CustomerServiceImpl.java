package com.dj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dj.dao.BaseDictDao;
import com.dj.pojo.BaseDict;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private BaseDictDao baseDictDao;
	@Override
	public List<BaseDict> findDictCode(String code) {
		List<BaseDict> list = baseDictDao.findDictCode(code);
		return list;
	}
}
