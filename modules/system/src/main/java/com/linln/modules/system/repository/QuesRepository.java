package com.linln.modules.system.repository;

import com.linln.modules.system.domain.Questionnaire;
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
public interface QuesRepository extends JpaRepository<Questionnaire,String> {
    @Query(value = " select * from sys_ques where  CONCAT(id,'')  like %?1%  order by create_Date_Time  desc    ", nativeQuery = true)
    Page<Questionnaire> fetchQuestionnaire(String searchText, Pageable req);

    @Query(value = " SELECT sct.no,sct.id AS tocId,sct.dateil,stm.id AS melId,stm.content ,sct.type ,stm.scale_id AS scaleId ,stm.score   FROM   sys_toc_mel AS stm LEFT JOIN sys_con_toc AS sct ON stm.topic_id= sct.id WHERE  stm.scale_id=?1 AND  sct.scale_id=stm.scale_id AND sct.superior='2'  ",nativeQuery = true)
    List<Map<String,Object>> getAllQues(String saleId);

    @Query(value = "  SELECT  sq.*,(SELECT COUNT(1) FROM sys_statistics WHERE  user_id=?1 AND sq.id=ques_id ) AS stateSt  FROM sys_ques AS sq  WHERE  sq.state='1' AND ((sq.per_list  LIKE %?2% OR sq.per_list LIKE %?3%) OR sq.per_list IS NULL) ORDER BY  create_date_time DESC    ",nativeQuery = true)
    List<Map<String,String>>  getQuesAll(String uid,String nickname,String deptName);

    @Query(value = "SELECT sq.per_list FROM sys_ques AS sq WHERE id = ?1",nativeQuery = true)
    String findPerListById(String id);

}
