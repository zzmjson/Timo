package com.linln.modules.system.service;


import com.linln.modules.system.domain.CaseSet;
import org.hibernate.sql.Delete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface CaseService {


    /**
     * 分页查询个案级别及来源
     * @param searchText
     * @param type
     * @param request
     * @return
     */
    Page<Map<String,Object>> fetchCaseSetBySearch(String searchText,String type, Pageable request);

    /**
     * 保存个案数据字典
     * @param caseSet
     * @return
     */
    CaseSet save(CaseSet caseSet);

    /**
     * 查询个案来源列表
     * @return
     */
    List<Map<String,String>> findSourse();

    /**
     * 查询个案处理级别
     * @return
     */
    List<Map<String,String>> findLevel();

    /**
     * 根据id查询
     * @return
     */
    CaseSet findById(String id);

    /**
     * 根据id删除字典
     * @param id
     */
    void delete(String id);

}
