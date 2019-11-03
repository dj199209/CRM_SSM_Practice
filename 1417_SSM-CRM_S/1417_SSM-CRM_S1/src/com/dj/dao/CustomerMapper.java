package com.dj.dao;

import java.util.List;

import com.dj.pojo.Customer;
import com.dj.pojo.QueryVo;

public interface CustomerMapper {
	public List<Customer> findCustomerVo(QueryVo vo);
	public Integer findCustomerVoCount(QueryVo vo);
	public Customer updateData(Long id) ;
	public List<Customer> update(Customer customer);
	public void updateCustomerById(Customer customer);
	public void deletCustomerById(Long id);
}
