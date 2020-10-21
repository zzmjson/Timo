package com.linln.modules.system.repository;

import com.linln.modules.system.domain.QuesStatistics;
import com.linln.modules.system.domain.Questionnaire;
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
public interface QuesStatisRepository extends JpaRepository<QuesStatistics,String> {

    @Query(value = " select * from sys_statistics where ques_id=?1 and user_id=?2  ",nativeQuery = true)
    QuesStatistics fetchQuesIdAndUserId(String quesId, String userId);

    @Query(value = " select * from sys_statistics where ques_id=?2 and  CONCAT(id,'')  like %?1%  order by create_Date_Time  desc ",nativeQuery = true)
    Page<QuesStatistics> fetchQuesStatisAll(String searchText, String quesId, Pageable req);
    @Query(value = "  SELECT ss.id, ss.ques_id,ss.create_date_time,ss.submit_time,ss.user_id,ss.user_name,sq.title " +
            "        FROM sys_statistics  AS ss , sys_ques AS sq WHERE sq.id=ss.ques_id   AND CONCAT(ss.id,'')  LIKE %?1%  ORDER BY ss.create_Date_Time  DESC     ",
            countQuery = "SELECT ss.id, ss.ques_id,ss.create_date_time,ss.submit_time,ss.user_id,ss.user_name,sq.title " +
                    "    FROM sys_statistics  AS ss , sys_ques AS sq WHERE sq.id=ss.ques_id   AND CONCAT(ss.id,'')  LIKE %?1%  ORDER BY ss.create_Date_Time  DESC",nativeQuery = true)
    Page<Map<String,String>> fetchQuesStatisAll(String searchText,Pageable req);

    @Query(value = "     SELECT ss.id,ss.create_date_time,ss.submit_time,sq.title FROM  sys_statistics AS ss, `sys_ques` AS sq  WHERE  sq.id=ss.ques_id AND   ss.user_id=?1  order by  ss.create_date_time desc ",nativeQuery = true)
    List<Map<String,String>> getQuesAll(String userId);
    @Query(value = "SELECT COUNT(id) FROM sys_statistics WHERE user_id=?1",nativeQuery = true)
    int findCountByUserId(String userId);




}
