package com.dj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dj.pojo.BaseDict;
import com.dj.pojo.Customer;
import com.dj.pojo.QueryVo;
import com.dj.service.CustomerService;

import cn.itcast.utils.Page;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@RequestMapping("/list")
	public String list(QueryVo vo,Model model) throws Exception {
		//客户来源
		List<BaseDict> customerSource = customerService.findDictCode("002");
		model.addAttribute("fromType", customerSource);
		//所属行业
		List<BaseDict> customerIndustry = customerService.findDictCode("001");
		model.addAttribute("industryType", customerIndustry);
		//客户级别
		List<BaseDict> customerLevel = customerService.findDictCode("006");
		model.addAttribute("levelType", customerLevel);
		//回写至前台
		if (vo.getCustName()!=null) {
			vo.setCustName(new String(vo.getCustName().getBytes("iso-8859-1"),"utf-8"));
		}
		model.addAttribute("custName", vo.getCustName());		
		model.addAttribute("custSource", vo.getCustSource());		
		model.addAttribute("custIndustry", vo.getCustIndustry());		
		model.addAttribute("custLevel", vo.getCustLevel());		
		//设置传入页码数量
		if (vo.getPage() == null) {
			vo.setPage(1);
		}
		//设置初始跳数
		vo.setStart((vo.getPage()-1) * vo.getSize());
		//把值传给数据库
		List<Customer> list = customerService.findCustomerByVo(vo);
		Integer count = customerService.findCustomerByVoCount(vo);
		
		//
		Page<Customer> page = new Page<>();
		page.setSize(vo.getSize());
		page.setPage(vo.getPage());
		page.setTotal(count);
		page.setRows(list);
		model.addAttribute("page", page);
		return "customer";
	}
	@RequestMapping("/edit")
	@ResponseBody
	public Customer edit(Long id) {
		Customer customer = customerService.updateDateById(id);
		return customer;
	}
	
	@RequestMapping("/update")
	public String update(Customer customer) {
		customerService.updateCustomer(customer);
		return "customer";
	}
	@RequestMapping("/delete")
	public String delete(Long id) {
		customerService.deleteCsutomerById(id);
		return "customer";
	}
}
