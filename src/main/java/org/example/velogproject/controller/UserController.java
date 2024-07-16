package org.example.velogproject.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.velogproject.domain.Post;
import org.example.velogproject.domain.User;
import org.example.velogproject.exception.DuplicateEmailException;
import org.example.velogproject.exception.DuplicateUsernameException;
import org.example.velogproject.service.PostService;
import org.example.velogproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/main")
    public String main(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "main";
    }

    @GetMapping("/userreg")
    public ModelAndView showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return new ModelAndView("userreg", "user", new User());
    }

    @PostMapping("/userreg")
    @Transactional
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "회원가입에 실패했습니다. 다시 시도해주세요.");
            return "userreg";
        }

        try {
            userService.save(user);
            model.addAttribute("registerSuccess", true);
            return "redirect:/registration-success";
        } catch (DuplicateUsernameException e) {
            bindingResult.rejectValue("username", "error.user", e.getMessage());
            return "userreg";
        } catch (DuplicateEmailException e) {
            bindingResult.rejectValue("email", "error.user", e.getMessage());
            return "userreg";
        }
    }

    @GetMapping("/registration-success")
    public String showRegistrationSuccessPage() {
        return "registration-success";
    }

    @GetMapping("/registration-failure")
    public String showRegistrationFailurePage(Model model, String errorMessage) {
        model.addAttribute("errorMessage", errorMessage);
        return "registration-failure";
    }
}
