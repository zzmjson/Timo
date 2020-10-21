package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.Interview;
import com.linln.modules.system.domain.InterviewContent;
import com.linln.modules.system.repository.InterviewContentRepository;
import com.linln.modules.system.repository.InterviewRepository;
import com.linln.modules.system.service.InterviewCoetentService;
import com.linln.modules.system.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class InterviewContentServiceImpl implements InterviewCoetentService {

    @Autowired
    private InterviewContentRepository interviewContentRepository;
    @Override
    public InterviewContent save(InterviewContent interviewContent) {
        return interviewContentRepository.save(interviewContent);
    }

    @Override
    public List<InterviewContent> findListByInterviewId(String interviewId) {
        return interviewContentRepository.findListByInterviewId(interviewId);
    }

    @Override
    public InterviewContent getOne(String id) {
        return interviewContentRepository.getOne(id);
    }

    @Override
    public void deleteByInterviewId(String interviewId) {
        interviewContentRepository.deleteByInterviewId(interviewId);
    }
}
