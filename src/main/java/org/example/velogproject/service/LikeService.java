package org.example.velogproject.service;

import lombok.AllArgsConstructor;
import org.example.velogproject.domain.Like;
import org.example.velogproject.domain.Post;
import org.example.velogproject.domain.User;
import org.example.velogproject.repository.LikeRepository;
import org.example.velogproject.repository.PostRepository;
import org.example.velogproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        Optional<User> userOpt = userRepository.findById(userId);

        if(postOpt.isPresent() && userOpt.isPresent()) {
            Post post = postOpt.get();
            User user = userOpt.get();

            Optional<Like> likeOpt = likeRepository.findByUserAndPost(user, post);

            if(likeOpt.isPresent()) {
                likeRepository.delete(likeOpt.get());
                return false;
            } else {
                Like like = new Like();
                like.setPost(post);
                like.setUser(user);
                likeRepository.save(like);
                return true;
            }
        } else {
            throw  new RuntimeException("Post or user not found");
        }
    }

    public long getLikeCount(Long postId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        return postOpt.map(likeRepository::countByPost).orElse(0L);
    }
}
