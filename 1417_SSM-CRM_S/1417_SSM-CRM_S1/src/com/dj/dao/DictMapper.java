package com.dj.dao;

import java.util.List;

import com.dj.pojo.BaseDict;

public interface DictMapper {
	public  List<BaseDict> findDictCode (String code);
}
