package com.linln.modules.system.repository;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.Summary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14extends JpaRepository<Scale, Integer>
 */
public interface SummaryRepository extends JpaRepository<Summary,String> {


    @Transactional
    @Modifying
    @Query(value = " delete from  sys_summary where  scale_id=?1 ",nativeQuery = true)
    void   deleteSummarys(String scaleId);


    @Query(value = "select * from sys_summary where scale_id=?1 order by grade ",nativeQuery = true)
    List<Summary> findByScaleIdGradeOrderByAsc(String scaleId);

     @Query(value = "select * from sys_summary where scale_id=?1 order by grade ",nativeQuery = true)
    List<Summary> findByScaleId(String scaleId);




}
