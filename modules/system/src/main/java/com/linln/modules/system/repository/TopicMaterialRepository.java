package com.linln.modules.system.repository;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.TopicMaterial;
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
public interface TopicMaterialRepository extends JpaRepository<TopicMaterial,String> {

     @Query(value = "select * from sys_toc_mel where scale_id=?1 and topic_id=?2   ",nativeQuery = true)
     List<TopicMaterial> findByScaleIdAndTopicId(String scaleId,String topicId);

     @Transactional
     @Modifying
     @Query(value = " delete from  sys_toc_mel where  scale_id=?1 and `topic_id`=?2  ",nativeQuery = true)
     void   deleteTopic(String scaleId, String topicId);

     @Query(value = " SELECT sct.no,sct.id AS tocId,sct.dateil,stm.id AS melId,stm.content ,sct.type ,stm.scale_id as scaleId,stm.score  " +
             " FROM sys_toc_mel AS stm LEFT JOIN  sys_con_toc AS sct ON sct.no=stm.no WHERE sct.scale_id=stm.scale_id  and sct.superior=?2 AND stm.scale_id=?1  " +
              " ORDER BY  sct.no+0  ",nativeQuery = true)
     List<Map<String,Object>> getAllSale( String saleId,String superior);



}
