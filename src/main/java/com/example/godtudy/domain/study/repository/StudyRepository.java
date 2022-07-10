package com.example.godtudy.domain.study.repository;

import com.example.godtudy.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    Study findByUrl(String url);

}
