package org.example.velogproject.service;

import lombok.AllArgsConstructor;
import org.example.velogproject.domain.Post;
import org.example.velogproject.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
