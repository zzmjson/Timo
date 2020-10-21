package com.linln.modules.system.repository;

import com.linln.modules.system.domain.AppraisalMaterial;
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
public interface MaterialRepository extends JpaRepository<AppraisalMaterial,String> {

    @Query(value = " select score  from sys_aal_material where scale_id=?1 and topic_id=?2 and  result='是' ",nativeQuery = true)
    List<String> findByScaleIdAndTopicId(String scaleId, String topicId);




}
