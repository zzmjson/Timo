package com.linln.modules.system.service.impl;

import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.RedisUtil;
import com.linln.modules.system.domain.Dept;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.ScaleType;
import com.linln.modules.system.domain.User;
import com.linln.modules.system.repository.ScaleTableRepository;
import com.linln.modules.system.repository.UserRepository;
import com.linln.modules.system.service.DeptService;
import com.linln.modules.system.service.ScaleTableService;
import com.linln.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class ScaleTableServiceImpl implements ScaleTableService {


    @Autowired
    ScaleTableRepository scaleTableRepository;


    @Override
    public Page<ScaleType> fetchScaleTypeBySearch(String searchText, Pageable request) {
        return scaleTableRepository.fetchScaleTypeBySearch(searchText,request);
    }

    @Override
    public ScaleType save(ScaleType scaleType) {
        return scaleTableRepository.save(scaleType);
    }

    @Override
    public ScaleType fetchOne(String id) {
        return scaleTableRepository.fetchOne(id);
    }

    @Override
    public Boolean updateStatus( String id) {
        return scaleTableRepository.updateStatus( id) > 0;
    }

    @Override
    public List<ScaleType> getBayAll() {
        return scaleTableRepository.findAll();
    }


}