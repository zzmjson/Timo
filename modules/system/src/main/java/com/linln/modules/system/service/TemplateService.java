package com.linln.modules.system.service;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface TemplateService {



    Template save(Template template);

    Template getOne(String id);

    Map<String,String> getAsseTemp();


}
