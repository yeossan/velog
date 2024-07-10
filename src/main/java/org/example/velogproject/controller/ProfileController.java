package org.example.velogproject.controller;

import jakarta.servlet.http.HttpSession;
import org.example.velogproject.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Handle case where user is not found in session
            return "redirect:/login"; // Example redirect to login page
        }
        model.addAttribute("session.user", user);
        return "profile";
    }
}
