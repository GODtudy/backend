package com.example.godtudy.domain.post.repository;

import com.example.godtudy.domain.post.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
