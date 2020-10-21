package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.CaseSet;
import com.linln.modules.system.domain.Report;
import com.linln.modules.system.repository.CaseRepository;
import com.linln.modules.system.repository.ReportRepository;
import com.linln.modules.system.service.CaseService;
import com.linln.modules.system.service.ReportService;
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
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Page<Report> findRepsotsByCreateUser(String userId,String only,String visitorName,String caseId,String rankId,Pageable req) {
        return reportRepository.findByCreateUser(userId,only,visitorName,caseId,rankId,req);
    }

    @Override
    public Report getOne(String id) {
        return reportRepository.getOne(id);
    }

    @Override
    public void updateState(String id) {
        reportRepository.updateState(id);
    }

    @Override
    public boolean isHasDataByCaseIdOrRankid(String id) {
        int count = reportRepository.findCountByCaseIdOrRankId(id);
        if (count>0){
            return false;
        }
        return true;

    }

    @Override
    public Page<Report> findAllBySearch(String only, String visitorName, String caseId, String rankId, Pageable req) {
        return reportRepository.findAllBySearch(only,visitorName,caseId,rankId,req);
    }
}