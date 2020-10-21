package com.linln.modules.system.service;

import com.linln.common.enums.StatusEnum;
import com.linln.modules.system.domain.Dept;
import com.linln.modules.system.domain.Group;
import com.linln.modules.system.domain.Scale;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 小懒虫
 * @date 2018/12/02
 */
public interface GroupService {

    /**
     * 查询拥有因子分组的量表列表
     * @param request
     * @return
     */
    Page<Map<String,String>> findScaleAll(PageRequest request);

    /**
     * 根据量表id查询因子分组详情
     * @param scaleId
     * @return
     */
    List<Group> findByScaleId(String scaleId);

    Group fingById(String id);

    Group save(Group group);

    /**
     * 查询已拥有因子分组的量表id
     * @return
     */
    List<String> findScaleId();

    /**
     * 查询编号是否已存在
     * @param scaleId
     * @param serial
     * @return
     */
    boolean haveGroup(String scaleId,String serial);
}

