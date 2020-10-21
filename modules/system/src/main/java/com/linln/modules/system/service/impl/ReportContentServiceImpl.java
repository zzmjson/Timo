package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.Report;
import com.linln.modules.system.domain.ReportContent;
import com.linln.modules.system.repository.ReportContentRepository;
import com.linln.modules.system.repository.ReportRepository;
import com.linln.modules.system.service.ReportContentService;
import com.linln.modules.system.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class ReportContentServiceImpl implements ReportContentService {

    @Autowired
    private ReportContentRepository reportContentRepository;
    @Override
    public ReportContent save(ReportContent reportContent) {
        return reportContentRepository.save(reportContent);
    }

    @Override
    public List<ReportContent> findByReportId(String reportId) {
        return reportContentRepository.findByReportId(reportId);
    }

    @Override
    public ReportContent getOne(String id) {
        return reportContentRepository.getOne(id);
    }
}