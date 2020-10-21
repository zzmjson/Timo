package com.linln.modules.system.service;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.Summary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface SummaryService {
    /**
     * 保存量表结论
     * @param summary
     * @return
     */
    Summary save(Summary summary);

    void  deleteSummarys(String scaleId);

    List<Summary> findByScaleIdGradeOrderByAsc(String scaleId);
    List<Summary> findByScaleId(String scaleId);

}
