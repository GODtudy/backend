package com.example.godtudy.domain.post.repository;

import com.example.godtudy.domain.post.entity.AdminPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminPostRepository extends JpaRepository<AdminPost, Long> {
}
