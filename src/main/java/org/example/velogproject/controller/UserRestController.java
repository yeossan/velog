package org.example.velogproject.controller;

import jakarta.validation.Valid;
import org.example.velogproject.exception.DuplicateEmailException;
import org.example.velogproject.exception.DuplicateUsernameException;
import org.example.velogproject.model.UserDto;
import org.example.velogproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/check-username")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("username") String username) {
        boolean exists = userService.isUsernameExists(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email") String email) {
        boolean exists = userService.isEmailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/userreg")
    public String registerUser(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            return "user-registration";
        }

        try {
            userService.registerUser(userDto);
            return "redirect:/welcome";
        } catch (DuplicateUsernameException e) {
            model.addAttribute("error", "Username is already taken");
            return "user-registration";
        } catch (DuplicateEmailException e) {
            model.addAttribute("error", "Email is already taken");
            return "user-registration";
        }
    }
}

