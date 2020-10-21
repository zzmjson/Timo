package com.linln.modules.system.repository;
import com.linln.modules.system.domain.ScaleAsseRese;
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
public interface ScaleAsseReseRepository extends JpaRepository<ScaleAsseRese,String> {

    @Query(value = " SELECT * FROM sys_scale_asse_rese  ",countQuery = "SELECT * FROM sys_scale_asse_rese",nativeQuery = true)
    Page<ScaleAsseRese> findScaleAsseReses(Pageable req);

}
