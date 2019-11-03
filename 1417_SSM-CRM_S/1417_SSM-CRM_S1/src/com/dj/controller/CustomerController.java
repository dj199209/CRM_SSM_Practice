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
	public String list(String custName, QueryVo vo,Model model)  throws Exception{
		//客户来源
		List<BaseDict> list = customerService.findDictCode("002");
		model.addAttribute("fromType", list);
		//所属行业
		List<BaseDict> industry = customerService.findDictCode("001");
		model.addAttribute("industryType", industry);
		//客户级别
		List<BaseDict> level = customerService.findDictCode("006");
		model.addAttribute("levelType", level);
		//设置编码
		if (vo.getCustName()!=null) {
			vo.setCustName(new String(vo.getCustName().getBytes("iso8859-1"),"utf-8"));
		}
		//回写
		model.addAttribute("custName", vo.getCustName());
		model.addAttribute("custSource", vo.getCustSource());
		model.addAttribute("custIndustry", vo.getCustIndustry());
		model.addAttribute("custLevel", vo.getCustLevel());
//		页码部分
		if (vo.getPage()==null) {
			vo.setPage(1);
		}
		//设置启始跳数
		vo.setStart((vo.getPage()-1) * vo.getSize());
		//list部分
		List<Customer> customerList = customerService.findCustomerVo(vo);
		Integer count = customerService.findCustomerVoCount(vo);
		Page<Customer> page = new Page<>();
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+vo.getStart());
		page.setSize(vo.getSize());//设置每页显示数
		page.setPage(vo.getPage());//设置但前夜数
		page.setTotal(count);//数据总页数
		page.setRows(customerList);//设置当前列表
		model.addAttribute("page", page);
		return "customer";
	}
	@RequestMapping("/editUpdate")
	@ResponseBody
	public Customer editUpdate(Long id) {
		Customer customer = customerService.updateData(id);
		return customer;
	}
	@RequestMapping("/update")
	public String update(Customer customer) {
		customerService.updateCustomerById(customer);
		return"customer";
	}
	@RequestMapping("/delete")
	public String delete(Long id) {
		customerService.deletCustomerById(id);
		return"customer";
	}
}
