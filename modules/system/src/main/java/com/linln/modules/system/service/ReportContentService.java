package com.linln.modules.system.service;


import com.linln.modules.system.domain.Report;
import com.linln.modules.system.domain.ReportContent;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface ReportContentService {

    /**
     * 保存个案内容信息
     * @param reportContent
     * @return
     */
    ReportContent save(ReportContent reportContent);

    /**
     * 根据个人报告表查询报告信息列表
     * @param reportId
     * @return
     */
    List<ReportContent> findByReportId(String reportId);

    /**
     * 根据id查询信息
     * @param id
     * @return
     */
    ReportContent getOne(String id);


}
