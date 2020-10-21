package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.AppraisalTopic;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.repository.ApprTopicRepository;
import com.linln.modules.system.repository.ScaleRepository;
import com.linln.modules.system.service.ApprTopicService;
import com.linln.modules.system.service.ScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class ApprTopicServiceImpl implements ApprTopicService {

    @Autowired
    ApprTopicRepository apprTopicRepository;

    @Override
    public AppraisalTopic save(AppraisalTopic appraisalTopic) {
        return apprTopicRepository.save(appraisalTopic);
    }

    @Override
    public List<AppraisalTopic> findByScaleIdAndUserId(String scaleId, String userId) {
        return apprTopicRepository.findByScaleIdAndUserId(scaleId,userId);
    }
}