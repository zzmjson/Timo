package com.linln.modules.system.service;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.ScaleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface ScaleService {

    Page<Scale> fetchScaleBySearch(String searchText, Pageable request);


    /**
     * 保存量表
     * @param scale
     * @return
     */
    Scale save(Scale scale);

    Scale getOne(String scaleId);
    /**
     * 查询唯一编号
     * @param scaleId
     * @return
     */
    Scale findByOnlyNoId(String scaleId);
    /**
     * 根据测评计划id查询所属的量表列表
     * @param asseId
     * @return
     */
    List<Scale> findScaleByAsseId(String asseId);

    /**
     * 查询已发布的测评量表
     * @return
     */
    Page<List<Map<String,String>>> findScales(String assId, Pageable request);

    /**
     * 前台根据测评计划查询量表
     * @param asseId
     * @return
     */
    List<Map<String,String>> findListByAss(String asseId,Long userId);

    /**
     * 查询量表有因子分组的量表列表
     * @param arr
     * @return
     */
    List<Scale> findByIsGroup(Object[] arr);

}
