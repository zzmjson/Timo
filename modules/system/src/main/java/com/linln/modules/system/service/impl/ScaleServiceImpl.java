package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.repository.ScaleRepository;
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
public class ScaleServiceImpl implements  ScaleService {


    @Autowired
    ScaleRepository scaleRepository;
    @Override
    public Page<Scale> fetchScaleBySearch(String searchText, Pageable request) {

        return scaleRepository.fetchScaleBySearch(searchText,request);
    }

    @Override
    public Scale save(Scale scale) {
        return scaleRepository.save(scale);
    }

    @Override
    public Scale getOne(String scaleId) {
        return scaleRepository.getOne(scaleId);
    }

    @Override
    public Scale findByOnlyNoId(String onlyNoId) {
        return scaleRepository.findByOnlyNoId(onlyNoId);
    }

    /**
     * 根据测评计划id查询所属的量表
     * @param asseId
     * @return
     */
    @Override
    public List<Scale> findScaleByAsseId(String asseId) {
        return scaleRepository.findScaleByAsseId(asseId);
    }

    @Override
    public Page<List<Map<String,String>>> findScales(String assId, Pageable request) {
        return scaleRepository.findScales(assId,request);
    }

    @Override
    public List<Map<String, String>> findListByAss(String asseId,Long userId) {
        return scaleRepository.findListByAss(asseId,userId);
    }

    @Override
    public List<Scale> findByIsGroup(Object[] arr) {
        return scaleRepository.findByIsGroup(arr);
    }


}