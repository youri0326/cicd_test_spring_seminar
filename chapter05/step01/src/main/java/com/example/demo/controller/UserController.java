package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.UserService;
import com.example.demo.entity.User;


@Controller
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        var users = service.findAll();

        model.addAttribute("users", users);

        return "index";
    }

    // 詳細画面
    @GetMapping("/user/{id}")
    public String showUserDetail(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = service.findUser(id);
            model.addAttribute("user", user);
            return "detail";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }    
    // 登録画面の表示
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // 登録処理
    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            User user = service.registerUser(name, email);
            redirectAttributes.addFlashAttribute("message", "登録に成功しました！ ID: " + user.getId());
            return "redirect:/";
            
        } catch (IllegalArgumentException e) {
            // バリデーションエラー時は登録画面に戻す
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }


}