package org.example.velogproject.repository;

import org.example.velogproject.domain.Like;
import org.example.velogproject.domain.Post;
import org.example.velogproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    long countByPost(Post post);
}
