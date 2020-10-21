package com.linln.modules.system.service;


import com.linln.modules.system.domain.CaseSet;
import com.linln.modules.system.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface ReportService {

    /**
     * 保存个案信息
     * @param report
     * @return
     */
    Report save(Report report);

    /**
     * 根据咨询师id查询个案列表
     * @param userId
     * @return
     */
    Page<Report> findRepsotsByCreateUser(String userId,String only,String visitorName,String caseId,String rankId,Pageable req);

    /**
     * 根据id查询个案信息
     * @param id
     * @return
     */
    Report getOne(String id);

    /**
     * 修改个案信息状态
     * @param id
     * @return
     */
    void updateState(String id);

    /**
     * 查询个案级别或者来源下面是否有数据
     * @param id
     * @return
     */
    boolean isHasDataByCaseIdOrRankid(String id);

    /**
     * 管理员多条件查询个案信息
     * @param only
     * @param visitorName
     * @param caseId
     * @param rankId
     * @param req
     * @return
     */
    Page<Report> findAllBySearch(String only,String visitorName,String caseId,String rankId,Pageable req);

}
