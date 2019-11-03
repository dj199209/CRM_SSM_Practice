package com.dj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dj.dao.CustomerMapper;
import com.dj.dao.DictMapper;
import com.dj.pojo.BaseDict;
import com.dj.pojo.Customer;
import com.dj.pojo.QueryVo;
@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private DictMapper dictMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Override
	public List<BaseDict> findDictCode(String code) {
		List<BaseDict> list = dictMapper.findDictCode(code);
		return list;
	}

	@Override
	public List<Customer> findCustomerVo(QueryVo vo) {
		List<Customer> list = customerMapper.findCustomerVo(vo);
		return list;
	}

	@Override
	public Integer findCustomerVoCount(QueryVo vo) {
		Integer count = customerMapper.findCustomerVoCount(vo);
		return count;
	}

	@Override
	public Customer updateData(Long id) {
		Customer customer = customerMapper.updateData(id);
		return customer;
	}


	@Override
	public void updateCustomerById(Customer customer) {
		customerMapper.updateCustomerById(customer);;
	}

	@Override
	public void deletCustomerById(Long id) {
		customerMapper.deletCustomerById(id);
	}

}
