package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.ReseTimeSolt;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.repository.ReseTimeSoltRepository;
import com.linln.modules.system.repository.ScaleRepository;
import com.linln.modules.system.service.ReseTimeSoltService;
import com.linln.modules.system.service.ScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class ReseTimeSoltServiceImpl implements ReseTimeSoltService {

    @Autowired
    private ReseTimeSoltRepository reseTimeSoltRepository;

    @Override
    public ReseTimeSolt save(ReseTimeSolt reseTimeSolt) {
        return reseTimeSoltRepository.save(reseTimeSolt);
    }

    @Override
    public List<ReseTimeSolt> findReseByuser(String uid) {
        return reseTimeSoltRepository.findReseByuser(uid);
    }

    @Override
    public List<ReseTimeSolt> findReseByDate(String date) {
        return reseTimeSoltRepository.findReseByDate(date);
    }
}