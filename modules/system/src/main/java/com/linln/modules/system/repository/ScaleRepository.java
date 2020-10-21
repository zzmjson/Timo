package com.linln.modules.system.repository;

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
public interface ScaleRepository extends JpaRepository<Scale,String> {

    @Query(value = " select * from sys_scale  where  CONCAT('')  like %?1% order by create_Date_Time  desc  ",
            countQuery = " select * from sys_scale  where  CONCAT('')  like %?1% order by create_Date_Time  desc  ",nativeQuery = true)
    Page<Scale> fetchScaleBySearch(String searchText, Pageable request);
    @Query(value = "select * from sys_scale  where only_no=?1 ",nativeQuery = true)
    Scale findByOnlyNoId(String onlyNoId);

    /**
     * 根据测评计划id查询所属的量表
     * @param asseId
     * @return
     */
    @Query(value = "SELECT se.* FROM sys_scale se JOIN sys_asse_scale sas ON se.id = sas.scale_id JOIN sys_asse_plan sap ON sas.asse_id=sap.id WHERE sap.id=?1",nativeQuery = true)
    List<Scale> findScaleByAsseId(String asseId);

    /**
     * 查询已发布的量表列表
     * @param request
     * @return
     */
    @Query(value = "SELECT sse.id,sse.title,sse.scale_type_name,(SELECT COUNT(*) FROM sys_con_toc WHERE scale_id=sse.id )AS number ,sse.create_Date_time, sse.create_user,sse.state FROM sys_scale AS sse WHERE  sse.id NOT IN ( SELECT ss.id FROM  sys_scale AS ss ,sys_asse_scale AS sas,sys_asse_plan AS sap  WHERE sap.id=sas.asse_id AND ss.id=scale_id  AND sap.id=?1)",
            countQuery = "SELECT sse.id,sse.title,sse.scale_type_name,(SELECT COUNT(*) FROM sys_con_toc WHERE scale_id=sse.id )AS number ,sse.create_Date_time, sse.create_user,sse.state FROM sys_scale AS sse WHERE  sse.id NOT IN ( SELECT ss.id FROM  sys_scale AS ss ,sys_asse_scale AS sas,sys_asse_plan AS sap  WHERE sap.id=sas.asse_id AND ss.id=scale_id  AND sap.id=?1)",
            nativeQuery = true)
    Page<List<Map<String,String>>> findScales(String assId , Pageable request);

    /**
     * 根据assId查询
     * @param asseId
     * @return
     */
    @Query(value = "       SELECT sce.id,sce.title,sce.duration_time,(SELECT COUNT(1) FROM sys_con_toc WHERE scale_id=sce.id )AS number ,IF((SELECT COUNT(1) " +
            " FROM `sys_user_pmq` WHERE scale_id=sce.id  AND asses_id=?1 AND user_id=?2 )=0,'否','是') AS state   " +
            " FROM sys_scale AS sce JOIN sys_asse_scale AS sas ON sce.id=sas.scale_id JOIN sys_asse_plan AS sap ON sas.asse_id=sap.id WHERE sap.id=?1  ",nativeQuery = true)
    List<Map<String,String>> findListByAss(String asseId,Long userId);

    /**
     * 查询量表有因子分组的量表列表
     * @param arr
     * @return
     */
    @Query(value = "SELECT * FROM sys_scale WHERE is_group=1 AND id NOT IN (?1)",nativeQuery = true)
    List<Scale> findByIsGroup(Object[] arr);





}
