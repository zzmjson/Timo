package com.linln.modules.system.repository;

import com.linln.modules.system.domain.CaseSet;
import com.linln.modules.system.domain.Report;
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
public interface ReportRepository extends JpaRepository<Report,String> {

    @Query(value = "SELECT * FROM sys_report WHERE create_user=?1 " +
            "AND if(?2='',1=1,only like %?2%)" +
            "AND IF(?3='',1=1,visitor_name like %?3%)" +
            "AND IF(?4='0',1=1,case_id=?4)" +
            "AND IF(?5='0',1=1,rank_id=?5) order by create_date_time desc ",nativeQuery = true)
    Page<Report> findByCreateUser(String userId,String only,String visitorName,String caseId,String rankId,Pageable req);

    @Query(value = "SELECT * FROM sys_report WHERE " +
            "if(?1='',1=1,only like %?1%)" +
            "AND IF(?2='',1=1,visitor_name like %?2%)" +
            "AND IF(?3='0',1=1,case_id=?3)" +
            "AND IF(?4='0',1=1,rank_id=?4) order by create_date_time desc",nativeQuery = true)
    Page<Report> findAllBySearch(String only,String visitorName,String caseId,String rankId,Pageable req);

    @Modifying
    @Transactional
    @Query(value = "UPDATE sys_report  SET state = 2 WHERE id = ?1",nativeQuery = true)
    void updateState(String id);

    @Query(value = "SELECT COUNT(*) FROM sys_report WHERE case_id=?1 OR rank_id=?1",nativeQuery = true)
    int findCountByCaseIdOrRankId(String id);
}
