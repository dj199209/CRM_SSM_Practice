package com.dj.service;

import java.util.List;

import com.dj.pojo.BaseDict;
import com.dj.pojo.Customer;
import com.dj.pojo.QueryVo;

public interface CustomerService {
	public List<BaseDict> findDictCode(String code);

	public List<Customer> findCustomerVo(QueryVo vo);
	public Integer findCustomerVoCount(QueryVo vo);
	
	
	//设置修改业
	public Customer updateData(Long id) ;
	//保存修改
	public void updateCustomerById(Customer customer);
	//删除
	public void deletCustomerById(Long id);
}
