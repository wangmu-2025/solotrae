package com.example.exambankadmin.controller;

import com.example.exambankadmin.entity.QuestionBank;
import com.example.exambankadmin.service.QuestionBankService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Autowired
    private QuestionBankService questionBankService;

    /**
     * 仪表盘页面
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "仪表盘");
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("content", "admin/dashboard");
        addCommonAttributes(model);
        return "layout/main";
    }

    /**
     * 用户管理页面
     */
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("title", "用户管理");
        model.addAttribute("activePage", "users");
        model.addAttribute("content", "admin/users");
        addCommonAttributes(model);
        return "layout/main";
    }

    /**
     * 题目管理页面
     */
    @GetMapping("/questions")
    public String questions(Model model) {
        model.addAttribute("title", "题目管理");
        model.addAttribute("activePage", "questions");
        model.addAttribute("content", "admin/questions");
        addCommonAttributes(model);
        return "layout/main";
    }

    /**
     * 题库管理页面 - 显示题库列表
     */
    @GetMapping("/question-banks")
    public String questionBanks(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "category", required = false) String category) {
        model.addAttribute("title", "题库管理");
        model.addAttribute("activePage", "question-banks");
        model.addAttribute("content", "admin/question-banks");
        model.addAttribute("questionBanks", questionBankService.searchQuestionBanks(keyword, category));
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        addCommonAttributes(model);
        return "layout/main";
    }

    /**
     * 系统设置页面
     */
    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("title", "系统设置");
        model.addAttribute("activePage", "settings");
        model.addAttribute("content", "admin/settings");
        addCommonAttributes(model);
        return "layout/main";
    }

    /**
     * 添加题库页面
     */
    @GetMapping("/question-banks/add")
    public String addQuestionBank(Model model) {
        model.addAttribute("title", "添加题库");
        model.addAttribute("activePage", "question-banks");
        model.addAttribute("content", "admin/question-bank-form");
        model.addAttribute("questionBank", new QuestionBank());
        addCommonAttributes(model);
        return "layout/main";
    }

    /**
     * 保存题库
     */
    @PostMapping("/question-banks/save")
    public String saveQuestionBank(@Valid @ModelAttribute("questionBank") QuestionBank questionBank,
                                  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "添加题库");
            model.addAttribute("activePage", "question-banks");
            model.addAttribute("content", "admin/question-bank-form");
            addCommonAttributes(model);
            return "layout/main";
        }
        
        questionBankService.createQuestionBank(questionBank);
        return "redirect:/admin/question-banks";
    }

    /**
     * 编辑题库页面
     */
    @GetMapping("/question-banks/edit/{id}")
    public String editQuestionBank(@PathVariable Long id, Model model) {
        QuestionBank questionBank = questionBankService.getQuestionBankById(id)
                .orElseThrow(() -> new RuntimeException("QuestionBank not found with id: " + id));
        
        model.addAttribute("title", "编辑题库");
        model.addAttribute("activePage", "question-banks");
        model.addAttribute("content", "admin/question-bank-form");
        model.addAttribute("questionBank", questionBank);
        addCommonAttributes(model);
        return "layout/main";
    }

    /**
     * 更新题库
     */
    @PostMapping("/question-banks/update/{id}")
    public String updateQuestionBank(@PathVariable Long id, @Valid @ModelAttribute("questionBank") QuestionBank questionBank,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "编辑题库");
            model.addAttribute("activePage", "question-banks");
            model.addAttribute("content", "admin/question-bank-form");
            addCommonAttributes(model);
            return "layout/main";
        }
        
        questionBankService.updateQuestionBank(id, questionBank);
        return "redirect:/admin/question-banks";
    }

    /**
     * 删除题库
     */
    @GetMapping("/question-banks/delete/{id}")
    public String deleteQuestionBank(@PathVariable Long id) {
        questionBankService.deleteQuestionBank(id);
        return "redirect:/admin/question-banks";
    }

    /**
     * 添加公共属性到模型
     */
    @Override
    public void addCommonAttributes(Model model) {
        // 调用父类方法添加API_BASE_URL
        super.addCommonAttributes(model);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }
}
