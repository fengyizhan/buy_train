package com.tm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tm.service.TrainOrderService;




@Controller
@RequestMapping("/")
public class RootController {

	@Autowired
	private TrainOrderService trainOrderService;
	/**
	 * 跳转到主界面
	 */
	@RequestMapping("")
	public String toIndex(HttpServletRequest request,HttpServletResponse response){
		return "redirect:/manage";
	}
	/**
	 * 登陆验证
	 */
	@RequestMapping("/loginSubmit")
	public String loginSubmit(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		if("monitor".equals(username)&&"monitor".equals(password))
		{
			request.getSession(true).setAttribute("currentUser",username);
			return "redirect:/manage";
		}else
		{
			redirectAttributes.addFlashAttribute("message", "用户名或密码不正确！");
			return "redirect:/login";
		}
	}
	/**
	 * 跳转到登陆面
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request,HttpServletResponse response){
		return "/login";
	}
}
