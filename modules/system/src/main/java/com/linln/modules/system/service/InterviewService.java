package com.linln.modules.system.service;

import com.linln.modules.system.domain.Interview;
import com.linln.modules.system.domain.ReserveInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface InterviewService {

    /**
     * 保存访谈记录
     * @param interview
     * @return
     */
    Interview save(Interview interview);

    /**
     * 根据个案信息id查询访问记录
     * @param reportId
     * @return
     */
    List<Interview> findByReportId(String reportId);

    /**
     * 根据id删除访谈记录表
     * @param id
     */
    void deleteById(String id);
}
