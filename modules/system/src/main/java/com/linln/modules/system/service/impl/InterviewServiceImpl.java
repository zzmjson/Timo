package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.Interview;
import com.linln.modules.system.domain.InterviewContent;
import com.linln.modules.system.domain.ReserveInfo;
import com.linln.modules.system.repository.InterviewRepository;
import com.linln.modules.system.repository.ReserveInfoRepository;
import com.linln.modules.system.service.InterviewCoetentService;
import com.linln.modules.system.service.InterviewService;
import com.linln.modules.system.service.ReserveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class InterviewServiceImpl implements InterviewService {

    @Autowired
    private InterviewRepository interviewRepository;
    @Autowired
    private InterviewCoetentService interviewCoetentService;

    @Override
    public Interview save(Interview interview) {
        return interviewRepository.save(interview);
    }

    @Override
    public List<Interview> findByReportId(String reportId) {
        return interviewRepository.findByReportId(reportId);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        interviewRepository.deleteById(id);
        interviewCoetentService.deleteByInterviewId(id);
    }
}
