package com.linln.modules.system.service;

import com.linln.modules.system.domain.ScaleAsseRese;
import com.linln.modules.system.domain.UserPmq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface ScaleAsseReseService {

   ScaleAsseRese save(ScaleAsseRese scaleAsseRese);
   Page<ScaleAsseRese> findScaleAsseReses(Pageable req);
   ScaleAsseRese getOne(String id);
}
