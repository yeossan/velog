package org.example.velogproject.service;

import lombok.AllArgsConstructor;
import org.example.velogproject.domain.Post;
import org.example.velogproject.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post save(Post post) {
        System.out.println("Saving post: " + post);  // 디버깅 로그 추가
        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
