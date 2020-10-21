package com.linln.modules.system.repository;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.UserPmq;
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
public interface AppraisalRepository extends JpaRepository<UserPmq,String> {

    @Query(value = " select * from sys_user_pmq  where  CONCAT('')  like %?1% order by create_Date_Time  desc  ",
           countQuery = "select * from sys_user_pmq  where  CONCAT('')  like %?1% order by create_Date_Time  desc",
           nativeQuery = true)
    Page<UserPmq> fetchAppraisalBySearch(String searchText, Pageable request);


    @Query(value = "    SELECT sct.no,sct.id AS tocId,sct.dateil,stm.id AS melId,stm.content ,sct.type ,stm.scale_id AS scaleId ,stm.score,stm.result   " +
            "  FROM `sys_aal_material` AS stm LEFT JOIN `sys_appraisal` AS sct ON stm.topic_id=sct.id WHERE sct.scale_id=stm.scale_id AND stm.scale_id=?1 AND sct.superior=?2 AND sct.upmq_Id=?3  ORDER BY sct.no+0  ",nativeQuery = true)
    List<Map<String,Object>> getAllSale(String saleId,String superior,String assId);


    @Query(value = " select * from sys_user_pmq where scale_id=?1 and user_id=?2 and asses_id=?3  limit 1 ",nativeQuery = true)
    UserPmq fetchScaleIdAndUserId(String scaleId, String userId,String assId);

    @Query(value = "SELECT * FROM sys_user_pmq WHERE user_id=?1",nativeQuery = true)
    List<UserPmq> fetchUserPmqByUser(String userId);

    @Query(value = "SELECT COUNT(id) FROM sys_user_pmq WHERE user_id=?1",nativeQuery = true)
    int findCountByUser(String userId);
   @Query(value = " SELECT us.id,us.nickname,us.email,if(us.sex=1,'男','女') as sex,us.birth_Date as birthDate ,us.age,us.picture,us.phone,us.remark,us.create_date,us.address,us.nation,us.education,us.serial_No,  " +
            " ss.info,ss.instruction,ss.alias,ss.id AS ssId,ss.only_no,ss.scale_type_name,ss.title, sup.submit_Time as submitTime , " +
            "        sup.id AS supId,sup.consequence,sup.only_no AS supOnlyNo, sup.scoring ,sup.sum_socre,sup.suggest ,sup.comment  " +
            "FROM `sys_user` AS us , `sys_scale` AS ss ,`sys_user_pmq`  AS sup  WHERE us.id=sup.user_id AND sup.scale_id=ss.id  AND sup.id=?1   ",nativeQuery = true)
    Map<String ,Object> getUserPmqBy(String supId);

   @Query(value = "SELECT * FROM sys_user_pmq WHERE asses_id=?1",nativeQuery = true)
   List<UserPmq> findByAssId(String id);

    @Query(value = "SELECT * FROM sys_user_pmq WHERE user_id=?1 and asses_id=?2",nativeQuery = true)
    List<UserPmq> findByUserIdAndAssesId(String userId,String assesId);
    @Query(value = "SELECT * FROM sys_user_pmq WHERE user_id=?1 and asses_id=?2",nativeQuery = true)
    Page<UserPmq> findPageByUserIdAndAssesId(String userId,String assesId,Pageable request);

    @Query(value = " SELECT sct.no,sct.id AS tocId,sct.dateil,stm.id AS melId,stm.content ,sct.type ,stm.scale_id AS scaleId ,stm.score,stm.result " +
            "            FROM `sys_aal_material` AS stm LEFT JOIN `sys_appraisal` AS sct ON stm.topic_id=sct.id WHERE sct.scale_id=stm.scale_id " +
            "             AND stm.scale_id=?1 ORDER BY  sct.no+0 ASC",nativeQuery = true)
    List<Map<String,Object>> getAllByscaleId(String saleId);

    /**
     * 因子统计计数
     * @param userId
     * @param scaleId
     * @param serial
     * @return
     */
    @Query(value = "SELECT aml.`serial`,aml.info,gp.stat_type AS `type`,COUNT(aml.id) AS score FROM sys_aal_material AS aml JOIN sys_group AS gp " +
            "ON aml.group_id=gp.id WHERE aml.user_id=?1" +
            "AND aml.scale_id=?2 AND aml.result='是' AND aml.score=1 AND aml.`serial`=?3 GROUP BY aml.`serial`,aml.info,gp.stat_type",nativeQuery = true)
    Map<String,String> findStatByCount(String userId,String scaleId,int serial);

    /**
     * 因子统计 求平均
     * @param userId
     * @param scaleId
     * @param serial
     * @return
     */
    @Query(value = "SELECT aml.`serial`,aml.info,gp.stat_type AS `type`,AVG(aml.score) AS score FROM sys_aal_material AS aml JOIN sys_group AS gp " +
            "ON aml.group_id=gp.id WHERE aml.user_id=?1 " +
            "AND aml.scale_id=?2 AND aml.result='是' AND aml.`serial`=?3 GROUP BY aml.`serial`,aml.info,gp.stat_type",nativeQuery = true)
    Map<String,String> findStatByAVG(String userId,String scaleId,int serial);

    /**
     * 因子统计 求和
     * @param userId
     * @param scaleId
     * @param serial
     * @return
     */
    @Query(value = "SELECT aml.`serial`,aml.info,gp.stat_type AS `type`,SUM(aml.score) AS score FROM sys_aal_material AS aml JOIN sys_group AS gp " +
            "ON aml.group_id=gp.id WHERE aml.user_id=?1 " +
            "AND aml.scale_id=?2 AND aml.result='是' AND aml.`serial`=?3 GROUP BY aml.`serial`,aml.info,gp.stat_type",nativeQuery = true)
    Map<String,String> findStatBySUM(String userId,String scaleId,int serial);

}
