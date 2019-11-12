package com.dj.service;

import java.util.List;

import com.dj.pojo.BaseDict;
import com.dj.pojo.Customer;
import com.dj.pojo.QueryVo;

public interface CustomerService {
	public List<BaseDict> findDictCode(String code);
	
	public List<Customer> findCustomerByVo(QueryVo vo);
	public Integer findCustomerByVoCount(QueryVo vo);
	public Customer updateDateById(Long id);
	public void updateCustomer(Customer customer);
	public void deleteCsutomerById(Long id);
}
