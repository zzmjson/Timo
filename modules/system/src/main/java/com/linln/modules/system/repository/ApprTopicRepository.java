package com.linln.modules.system.repository;

import com.linln.modules.system.domain.AppraisalTopic;
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
public interface ApprTopicRepository extends JpaRepository<AppraisalTopic,String> {

    @Query(value = " select * from sys_appraisal where scale_id=?1 and user_Id=?2  ",nativeQuery = true)
    List<AppraisalTopic> findByScaleIdAndUserId(String scaleId, String userId);

}
