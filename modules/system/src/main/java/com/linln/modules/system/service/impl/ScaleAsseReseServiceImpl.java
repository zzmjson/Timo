package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.ScaleAsseRese;
import com.linln.modules.system.domain.UserPmq;
import com.linln.modules.system.repository.AppraisalRepository;
import com.linln.modules.system.repository.ScaleAsseReseRepository;
import com.linln.modules.system.service.AppraisalService;
import com.linln.modules.system.service.ScaleAsseReseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class ScaleAsseReseServiceImpl implements ScaleAsseReseService {

    @Autowired
    private ScaleAsseReseRepository scaleAsseReseRepository;
    @Override
    public ScaleAsseRese save(ScaleAsseRese scaleAsseRese) {
        return scaleAsseReseRepository.save(scaleAsseRese);
    }

    @Override
    public Page<ScaleAsseRese> findScaleAsseReses(Pageable req) {
        return scaleAsseReseRepository.findScaleAsseReses(req);
    }

    @Override
    public ScaleAsseRese getOne(String id) {
        return scaleAsseReseRepository.getOne(id);
    }
}