package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
        System.out.println("test");
        // 1. 一度変数に入れる（Listなどの型はserviceの戻り値に合わせてください）
        var users = service.findAll();

        // 2. コンソールに出力
        System.out.println("DEBUG: usersの中身 = " + users);

        // 3. モデルに追加
        model.addAttribute("users", users);

        return "index";
    }

    // 登録画面の表示
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            // Serviceの呼び出し
            User user = service.registerUser(name, email);
            
            // リダイレクト先にデータを渡すための特別なバケツ「RedirectAttributes」
            redirectAttributes.addFlashAttribute("message", "登録に成功しました！ ID: " + user.getId());
            
            // トップ画面（/）へリダイレクト
            return "redirect:/";
            
        } catch (IllegalArgumentException e) {
            // エラーの場合は入力画面にとどまり、メッセージを表示
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
    @GetMapping("/user/{id}")
    public String showUserDetail(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = service.findUser(id);
            model.addAttribute("user", user);
            return "detail"; // detail.html を作成
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }    
}