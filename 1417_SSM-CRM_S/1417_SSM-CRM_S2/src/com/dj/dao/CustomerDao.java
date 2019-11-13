package com.dj.dao;

import java.util.List;

import com.dj.pojo.Customer;
import com.dj.pojo.QueryVo;

public interface CustomerDao {
	//显示列表
	public List<Customer> findCustomerByVo(QueryVo vo);
	public Integer findCustomerByVoCount(QueryVo vo);
	//修改
	public Customer findCustomrById(Long id);
	//设置值
	public void updateCustomer(Customer customer);
	//删除值
	public void delectCustomerById(Long id);
	
}
