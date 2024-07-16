package org.example.velogproject.controller;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.example.velogproject.domain.Image;
import org.example.velogproject.domain.Post;
import org.example.velogproject.domain.User;
import org.example.velogproject.model.PostDto;
import org.example.velogproject.service.ImageService;
import org.example.velogproject.service.PostService;
import org.example.velogproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    private final PostService postService;
    private final ImageService imageService;
    private final UserService userService;

    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    public PostController(PostService postService, ImageService imageService, UserService userService) {
        this.postService = postService;
        this.imageService = imageService;
        this.userService = userService;
    }

    @GetMapping("/write")
    public String showWriteForm(Model model) {
        model.addAttribute("postDto", new PostDto());
        return "write";
    }

    @PostMapping("/write")
    public String savePost(@Valid @ModelAttribute("postDto") PostDto postDto,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("postDto", postDto);
            return "write";
        }

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUser(currentUser);

        Post savedPost = postService.save(post);

        List<Image> images = new ArrayList<>();
        for (MultipartFile imageFile : postDto.getImages()) {
            if (!imageFile.isEmpty()) {
                Image image = new Image();
                image.setImageName(imageFile.getOriginalFilename());
                String imagePath = saveImageToDisk(imageFile);
                image.setImagePath(imagePath);
                image.setPost(savedPost);
                images.add(image);
            }
        }

        if (!images.isEmpty()) {
            imageService.saveAll(images);
            savedPost.getImages().addAll(images);
            postService.save(savedPost);
        }

        return "redirect:/main";
    }

    private String saveImageToDisk(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
}
