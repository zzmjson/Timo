package com.linln.modules.system.repository;

import com.linln.common.constant.StatusConst;
import com.linln.modules.system.domain.Dept;
import com.linln.modules.system.domain.Group;
import com.linln.modules.system.domain.Scale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 小懒虫
 * @date 2018/12/02
 */
public interface GroupRepository extends JpaRepository<Group , String> {

    /**
     * 查询拥有因子分组的量表列表
     * @param request
     * @return
     */
    @Query(value = "SELECT sc.id AS id,sc.title FROM sys_group AS gp JOIN sys_scale AS sc ON gp.scale_id=sc.id GROUP BY gp.scale_id",
            countQuery = "SELECT sc.id AS id,sc.title FROM sys_group AS gp JOIN sys_scale AS sc ON gp.scale_id=sc.id GROUP BY gp.scale_id",
            nativeQuery = true)
    Page<Map<String,String>> findScaleAll(PageRequest request);

    /**
     * 根据量表id查询因子分组详情
     * @param scaleId
     * @return
     */
    @Query(value = "SELECT * FROM sys_group WHERE scale_id=?1 ORDER BY `serial`+0 ",nativeQuery = true)
    List<Group> findByScaleId(String scaleId);

    /**
     * 查询已拥有因子分组的量表id
     * @return
     */
    @Query(value = "SELECT gp.scale_id FROM sys_group AS gp GROUP BY gp.scale_id",nativeQuery = true)
    List<String> findScaleId();

    /**
     * 根据序号与量表编号查询因子分组信息
     * @param scaleId
     * @param serial
     * @return
     */
    @Query(value = "SELECT * FROM sys_group WHERE scale_id=?1 AND `serial`=?2",nativeQuery = true)
    Group findByScaleIdAndSerial(String scaleId,String serial);
}

