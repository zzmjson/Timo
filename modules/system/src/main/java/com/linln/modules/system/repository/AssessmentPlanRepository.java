package com.linln.modules.system.repository;

import com.linln.modules.system.domain.AssessmentPlan;
import com.linln.modules.system.domain.Scale;
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
public interface AssessmentPlanRepository extends JpaRepository<AssessmentPlan,String> {


    @Query(value = " SELECT * FROM sys_asse_plan where if(?2='0',1=1,state=?2) and if(?1='',1=1,title like %?1%) and state!=4",nativeQuery = true)
    Page<AssessmentPlan> fetchScaleBySearch(String searchText,int state , Pageable request);
    @Modifying
    @Transactional
    @Query(value = "UPDATE sys_asse_plan  SET state = ?2 WHERE id = ?1 ",nativeQuery = true)
    void updateState(String id,int state);
    @Query(value = "select * from sys_asse_plan where id=?1",nativeQuery = true)
    AssessmentPlan findOneById(String id);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sys_asse_scale  WHERE asse_id = ?1 AND scale_id = ?2",nativeQuery = true)
    Integer deleteScale(String assId,String scaleId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO sys_asse_scale (asse_id, scale_id) VALUES (?1,?2)",nativeQuery = true)
//    @Query(value = "select count(*) from sys_asse_plan",nativeQuery = true)
    Integer addScale(String assId,String scaleId);

    /**
     * 根据查询权限查询测评量表
     * @param nickname
     * @param deptName
     * @return
     */
    @Query(value = "SELECT * FROM sys_asse_plan WHERE state=1 and ((per_list  LIKE %?1% OR per_list LIKE %?2%) OR per_list IS NULL)",nativeQuery = true)
    public List<AssessmentPlan> findAssessmentByDeptOrUser(String nickname,String deptName);





}
