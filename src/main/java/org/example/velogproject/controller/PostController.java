package org.example.velogproject.controller;

import lombok.AllArgsConstructor;
import org.example.velogproject.domain.Image;
import org.example.velogproject.domain.Post;
import org.example.velogproject.domain.User;
import org.example.velogproject.service.ImageService;
import org.example.velogproject.service.PostService;
import org.example.velogproject.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final ImageService imageService;
    private final UserService userService; //

    @GetMapping("/write")
    public String showWriteForm(Model model) {
        model.addAttribute("post", new Post());
        return "write";
    }

    @PostMapping("/write")
    public String savePost(@Valid @ModelAttribute("post") Post post,
                           BindingResult bindingResult,
                           @RequestParam("images") MultipartFile[] images,
                           Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "write";
        }

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        post.setUser(currentUser);

        Post savedPost = postService.save(post);

        for (MultipartFile imageFile : images) {
            Image image = new Image();
            image.setImageName(imageFile.getOriginalFilename());
            String imagePath = saveImageToDisk(imageFile);
            image.setImagePath(imagePath);
            image.setPost(savedPost);
            imageService.save(image);
        }

        return "redirect:/main";
    }

    private String saveImageToDisk(MultipartFile file) {
        // 파일을 디스크에 저장하고 저장 경로를 반환하는 구현
        return "path_to_saved_image";
    }
}
