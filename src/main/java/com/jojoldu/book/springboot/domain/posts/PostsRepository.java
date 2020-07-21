package com.jojoldu.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// Entity class와 기본 Entity Repo는 함께 위치
public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
