package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.CaseSet;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.repository.CaseRepository;
import com.linln.modules.system.repository.ScaleRepository;
import com.linln.modules.system.service.CaseService;
import com.linln.modules.system.service.ScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class CaseServiceImpl implements CaseService {

    @Autowired
    CaseRepository caseRepository;


    @Override
    public Page<Map<String,Object>> fetchCaseSetBySearch(String searchText,String type, Pageable request) {
        return caseRepository.fetchCaseSetBySearch(searchText,type,request);
    }

    @Override
    public CaseSet save(CaseSet caseSet) {
        return caseRepository.save(caseSet);
    }

    @Override
    public List<Map<String, String>> findSourse() {
        return caseRepository.findSourse();
    }

    @Override
    public List<Map<String, String>> findLevel() {
        return caseRepository.findLevel();
    }

    @Override
    public CaseSet findById(String id) {
        return caseRepository.getOne(id);
    }

    @Override
    public void delete(String id) {
        caseRepository.deleteById(id);
    }


}