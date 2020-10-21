package com.linln.modules.system.service;

import com.linln.modules.system.domain.AssessmentPlan;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.ScaleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface AssessmentPlanService {

    Page<AssessmentPlan> fetchAssessmentPlanBySearch(String searchText,int state, Pageable request);



    /**
     * 保存类型
     * @param assessmentPlan
     * @return
     */
    AssessmentPlan save(AssessmentPlan assessmentPlan);

    /**
     * 更新测评计划状态
     * @param id
     */
    void updateState(String id);

    /**
     * 根据id获取测评计划数据
     * @param id
     * @return
     */
    AssessmentPlan findOneById(String id);

    /**
     * 删除测评计划下的测评量表
     * @param assId
     * @param scaleId
     */
    boolean deleteScale(String assId,String scaleId);

    /**
     * 添加量表到测评计划中
     * @param assId
     * @param scaleId
     * @return
     */
    boolean addScale(String assId,String scaleId);

    /**
     * 根据用户或部门拥有的量表查询权限查询测评计划
     * @param nickname
     * @param deptName
     * @return
     */
    public List<AssessmentPlan> findAssessmentByDeptOrUser(String nickname,String deptName);

}
