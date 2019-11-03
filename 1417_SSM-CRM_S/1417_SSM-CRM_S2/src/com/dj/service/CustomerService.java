package com.dj.service;

import java.util.List;

import com.dj.pojo.BaseDict;

public interface CustomerService {
	public List<BaseDict> findDictCode(String code);
}
