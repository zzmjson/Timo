package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.Template;
import com.linln.modules.system.repository.ScaleRepository;
import com.linln.modules.system.repository.TemplateRepository;
import com.linln.modules.system.service.ScaleService;
import com.linln.modules.system.service.TemplateService;
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
public class TemplateServiceImpl implements TemplateService {


    @Autowired
    TemplateRepository templateRepository;


    @Override
    public Template save(Template template) {
        return templateRepository.save(template);
    }

    @Override
    public Template getOne(String id) {
        return templateRepository.getOne(id);
    }

    @Override
    public Map<String, String> getAsseTemp() {
        return templateRepository.getAsseTemp();
    }


}