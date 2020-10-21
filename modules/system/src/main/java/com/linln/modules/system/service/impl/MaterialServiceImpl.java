package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.AppraisalMaterial;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.repository.MaterialRepository;
import com.linln.modules.system.repository.ScaleRepository;
import com.linln.modules.system.service.MaterialService;
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
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    MaterialRepository materialRepository;

    @Override
    public AppraisalMaterial save(AppraisalMaterial appraisalMaterial) {
        return materialRepository.save(appraisalMaterial);
    }

    @Override
    public List<String> findByScaleIdAndTopicId(String scaleId, String topicId) {
        return materialRepository.findByScaleIdAndTopicId(scaleId,topicId);
    }
}