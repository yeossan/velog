package org.example.velogproject.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private List<MultipartFile> images = new ArrayList<>();
}
