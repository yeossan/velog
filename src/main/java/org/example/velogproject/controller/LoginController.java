package org.example.velogproject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.velogproject.domain.User;
import org.example.velogproject.model.LoginForm;
import org.example.velogproject.service.LoginService;
import org.example.velogproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm, HttpSession session) {
        User user = userService.login(loginForm.getUsername(), loginForm.getPassword());
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/main";
        } else {
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main";
    }
}
