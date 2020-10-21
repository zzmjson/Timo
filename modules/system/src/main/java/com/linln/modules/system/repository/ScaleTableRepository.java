package com.linln.modules.system.repository;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.ScaleType;
import com.linln.modules.system.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface ScaleTableRepository extends JpaRepository<ScaleType, String> {

    /**
     * 多条件查询用户列表
     * @return
     */

    @Query(value = " select * from sys_scale_type where  CONCAT(name,id,'')  like %?1%  order by create_Date_Time  desc ", nativeQuery = true)
    Page<ScaleType> fetchScaleTypeBySearch(String searchText,  Pageable request);
    @Query(value = "select  * from sys_scale_type where id=?1 ",nativeQuery = true)
    ScaleType fetchOne (String id);


    @Modifying
    @Transactional
    @Query(value = " delete from  sys_scale_type where id=?1 ",nativeQuery = true)
    int updateStatus (String id);




}
