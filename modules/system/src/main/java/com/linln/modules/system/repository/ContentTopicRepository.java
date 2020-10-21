package com.linln.modules.system.repository;

import com.linln.modules.system.domain.ContentTopic;
import com.linln.modules.system.domain.Scale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14extends JpaRepository<Scale, Integer>
 */
public interface ContentTopicRepository extends JpaRepository<ContentTopic,String> {


    @Query(value = " select * from sys_con_toc  where scale_id=?1 and superior=?3 and  CONCAT('')  like %?2%  order by `no`+0 , create_Date_Time  desc ",
            countQuery = " select * from sys_con_toc  where scale_id=?1 and  superior=?3 and   CONCAT('')  like %?2%  order by `no`, create_Date_Time  desc ",nativeQuery = true)
    Page<ContentTopic> fetchContentTopicBySearch(String scaleId,String searchText,String superior, Pageable request);

    @Query(value = "select count(1) from sys_con_toc where scale_id=?1 and superior=?2  ",nativeQuery = true)
    Integer checkContentTopic(String scaleId,String superior);

    /**
     * 根据量表id查询题目跟选项
     * @param scaleId
     * @return
     */
    @Query(value = "SELECT sct.no,sct.id AS tocId,sct.dateil,stm.id AS melId,stm.content FROM sys_toc_mel AS stm LEFT JOIN  sys_con_toc AS sct ON sct.no=stm.no WHERE sct.scale_id=stm.scale_id AND stm.scale_id=?1 ORDER BY  sct.no+0 ",nativeQuery = true)
    public List<Map<String,String>> findTocAndMelByScale(String scaleId);

}
