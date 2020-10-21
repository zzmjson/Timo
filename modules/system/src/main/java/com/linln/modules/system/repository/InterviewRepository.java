package com.linln.modules.system.repository;

import com.linln.modules.system.domain.Interview;
import com.linln.modules.system.domain.Scale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14extends JpaRepository<Scale, Integer>
 */
public interface InterviewRepository extends JpaRepository<Interview,String> {

    @Query(value = "SELECT * FROM sys_interview WHERE report_id=?1",nativeQuery = true)
    List<Interview> findByReportId(String reportId);








}
