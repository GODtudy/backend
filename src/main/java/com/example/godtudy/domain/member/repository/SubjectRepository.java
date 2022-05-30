package com.example.godtudy.domain.member.repository;

import com.example.godtudy.domain.member.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findByTitle(String title);
}
