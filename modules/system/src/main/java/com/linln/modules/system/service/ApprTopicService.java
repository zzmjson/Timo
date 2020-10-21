package com.linln.modules.system.service;

import com.linln.modules.system.domain.AppraisalTopic;
import com.linln.modules.system.domain.Scale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface ApprTopicService {


    AppraisalTopic save(AppraisalTopic appraisalTopic);
    List<AppraisalTopic> findByScaleIdAndUserId(String scaleId,String userId);

}
