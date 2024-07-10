package org.example.velogproject.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.websocket.OnMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //글의 id

    @NotBlank(message = "Title is required")
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Transient
    private transient MultipartFile[] imageFiles;
}
