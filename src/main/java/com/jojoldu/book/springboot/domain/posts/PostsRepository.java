package com.jojoldu.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

// Entity class와 기본 Entity Repo는 함께 위치
public interface PostsRepository extends JpaRepository<Posts, Long> {
}
