package com.budgetbuddy.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        addSessionAttributes(session, model);
        return "dashboard";
    }

    @GetMapping("/transactions")
    public String transactions(HttpSession session, Model model) {
        addSessionAttributes(session, model);
        return "transactions";
    }

    @GetMapping("/report")
    public String report(HttpSession session, Model model) {
        addSessionAttributes(session, model);
        return "report";
    }

    @GetMapping("/categories")
    public String categories(HttpSession session, Model model) {
        addSessionAttributes(session, model);
        return "categories";
    }

    private void addSessionAttributes(HttpSession session, Model model) {
        model.addAttribute("userId",   session.getAttribute("userId"));
        model.addAttribute("username", session.getAttribute("username"));
    }
}
