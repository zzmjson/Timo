package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.Summary;
import com.linln.modules.system.repository.ScaleRepository;
import com.linln.modules.system.repository.SummaryRepository;
import com.linln.modules.system.service.ScaleService;
import com.linln.modules.system.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class SummaryServiceImpl implements SummaryService {


    @Autowired
    SummaryRepository summaryRepository;

    @Override
    public Summary save(Summary summary) {
        return summaryRepository.save(summary);
    }

    @Override
    public void deleteSummarys(String scaleId) {
        summaryRepository.deleteSummarys(scaleId);
    }

    @Override
    public List<Summary> findByScaleIdGradeOrderByAsc(String scaleId) {
        return summaryRepository.findByScaleIdGradeOrderByAsc(scaleId);
    }

    @Override
    public List<Summary> findByScaleId(String scaleId) {
        return summaryRepository.findByScaleId(scaleId);
    }


}