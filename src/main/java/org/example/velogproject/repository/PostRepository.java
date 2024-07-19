package org.example.velogproject.repository;

import org.example.velogproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.title = :title AND p.user.name = :name")
    List<Post> findByTitleAndUserNickname(@Param("title") String title, @Param("name") String name);
}
