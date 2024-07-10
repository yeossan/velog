package org.example.velogproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String imageName;
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

}
