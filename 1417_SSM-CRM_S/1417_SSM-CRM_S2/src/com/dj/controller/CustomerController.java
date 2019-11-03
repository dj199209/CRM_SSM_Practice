package com.dj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dj.pojo.BaseDict;
import com.dj.pojo.QueryVo;
import com.dj.service.CustomerService;

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
		if (vo.getCustName()!="") {
			vo.setCustName(new String(vo.getCustName().getBytes("iso-8859-1"),"utf-8"));
		}
		model.addAttribute("custName", vo.getCustName());		
		model.addAttribute("custSource", vo.getCustSource());		
		model.addAttribute("custIndustry", vo.getCustIndustry());		
		model.addAttribute("custLevel", vo.getCustLevel());		
		
		return "customer";
	}
}
