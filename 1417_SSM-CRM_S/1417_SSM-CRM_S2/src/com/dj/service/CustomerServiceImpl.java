package com.dj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dj.dao.BaseDictDao;
import com.dj.dao.CustomerDao;
import com.dj.pojo.BaseDict;
import com.dj.pojo.Customer;
import com.dj.pojo.QueryVo;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private BaseDictDao baseDictDao;
	@Autowired
	private CustomerDao customerDao;
	@Override
	public List<BaseDict> findDictCode(String code) {
		List<BaseDict> list = baseDictDao.findDictCode(code);
		return list;
	}
	@Override
	public List<Customer> findCustomerByVo(QueryVo vo) {
		List<Customer> list = customerDao.findCustomerByVo(vo);
		return list;
		
	}
	@Override
	public Integer findCustomerByVoCount(QueryVo vo) {
		Integer count = customerDao.findCustomerByVoCount(vo);
		return count;
	}
	@Override
	public Customer findCustomrById(Long id) {
		Customer customer = customerDao.findCustomrById(id);
		return customer;
	}
	@Override
	public void updateCustomer(Customer customer) {
		customerDao.updateCustomer(customer);
		
	}
	@Override
	public void delectCustomerById(Long id) {
		customerDao.delectCustomerById(id);
	}
}
