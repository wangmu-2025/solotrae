package com.example.exambankadmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    /**
     * 根路径重定向到登录页面
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    /**
     * 添加全局属性到模型
     */
    public void addCommonAttributes(Model model) {
        // API基础URL配置
        model.addAttribute("API_BASE_URL", "/api");
    }
}
