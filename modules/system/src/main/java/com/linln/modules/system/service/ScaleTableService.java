package com.linln.modules.system.service;

import com.linln.common.enums.StatusEnum;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.ScaleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface ScaleTableService {

    Page<ScaleType> fetchScaleTypeBySearch(String searchText,  Pageable request);



    /**
     * 保存类型
     * @param scaleType
     * @return
     */
    ScaleType save(ScaleType scaleType);

    /**
     * 根据ID查询数据
     * @param id
     *
     */
    ScaleType fetchOne(String id);

    @Transactional
    Boolean updateStatus(   String  id);

    List<ScaleType> getBayAll();

}
