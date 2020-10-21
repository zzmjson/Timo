package com.linln.modules.system.repository;

import com.linln.modules.system.domain.Report;
import com.linln.modules.system.domain.ReportContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author 小懒虫
 * @date 2018/8/14extends JpaRepository<Scale, Integer>
 */
public interface ReportContentRepository extends JpaRepository<ReportContent,String> {

    @Query(value = "SELECT * FROM sys_report_content WHERE report_id=?1",nativeQuery = true)
    List<ReportContent> findByReportId(String reportId);
}
