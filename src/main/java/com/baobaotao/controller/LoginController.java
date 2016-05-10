package com.baobaotao.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.baobaotao.domain.LoginCommand;
import com.baobaotao.domain.User;
import com.baobaotao.service.UserService;

@Controller
public class LoginController {
	@Autowired
	private UserService userService;
	@RequestMapping(value = "/login")
	public String loginPage(){
		return "login";
	}
	
	public ModelAndView loginCheck(HttpServletRequest req,LoginCommand loginCommand) {
		boolean isValidUser = userService.hasMatchUser(loginCommand.getUserName(), loginCommand.getPassword());
		if(!isValidUser){
			return new ModelAndView("login","error","账号密码错误");
		}
		User user = userService.findUserByUserName(loginCommand.getUserName());
		user.setLasstVist(new Date());
		user.setLastIp(req.getRemoteAddr());
		userService.LoginSuccess(user);
		req.getSession().setAttribute("user", user);
		return new ModelAndView("main");
	}
	
	@RequestMapping(value="/applogin", method={RequestMethod.POST,RequestMethod.GET},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map appLogin(LoginCommand loginCommand){
		Map<String,Object> map = new HashMap<String, Object>();
		boolean isValidUser = userService.hasMatchUser(loginCommand.getUserName(), loginCommand.getPassword());
		if(!isValidUser){
			map.put("result", 0);
			map.put("message","账号密码错误");
			return map;
		}
		map.put("result", 1);
		map.put("message", "123");
		return map;
	}
	
}
