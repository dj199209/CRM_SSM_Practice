package com.dj.service;

import java.util.List;

import com.dj.pojo.BaseDict;
import com.dj.pojo.Customer;
import com.dj.pojo.QueryVo;

public interface CustomerService {
	public List<BaseDict> findDictCode(String code);
	//显示列表
	public List<Customer> findCustomerByVo(QueryVo vo);
	public Integer findCustomerByVoCount(QueryVo vo);
	public Customer findCustomrById(Long id);
	public void updateCustomer(Customer customer);
	public void delectCustomerById(Long id);
}
