package com.linln.modules.system.service;

import com.linln.modules.system.domain.Interview;
import com.linln.modules.system.domain.InterviewContent;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface InterviewCoetentService {

    /**
     * 保存访谈类容
     * @param interviewContent
     * @return
     */
    InterviewContent save(InterviewContent interviewContent);

    /**
     * 根据访谈记录表id查询访谈类容
     * @param interviewId
     * @return
     */
    List<InterviewContent> findListByInterviewId(String interviewId);

    InterviewContent getOne(String id);


    /**
     * 根据访谈id 删除内容
     * @param interviewId
     */
    void deleteByInterviewId(String interviewId);
}
