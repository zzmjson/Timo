package com.linln.modules.system.repository;

import com.linln.modules.system.domain.Interview;
import com.linln.modules.system.domain.InterviewContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14extends JpaRepository<Scale, Integer>
 */
public interface InterviewContentRepository extends JpaRepository<InterviewContent,String> {

    @Query(value = "SELECT * FROM sys_int_content WHERE interview_id=?1",nativeQuery = true)
    List<InterviewContent> findListByInterviewId(String interviewId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sys_int_content WHERE interview_id=?1",nativeQuery = true)
    void deleteByInterviewId(String interviewId);




}
