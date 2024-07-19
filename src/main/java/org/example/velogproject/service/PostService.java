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
        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findByTitleAndUserNickname(String title, String name) {
        List<Post> posts = postRepository.findByTitleAndUserNickname(title, name);
        if (posts.isEmpty()) {
            throw new RuntimeException("Post not found");
        }
        if (posts.size() > 1) {
            throw new RuntimeException("Multiple posts found");
        }
        return posts.getFirst(); // 첫 번째 결과 반환
    }
}
