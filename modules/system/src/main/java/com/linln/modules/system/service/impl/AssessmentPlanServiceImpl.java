package com.linln.modules.system.service.impl;


import com.linln.modules.system.domain.AssessmentPlan;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.ScaleType;
import com.linln.modules.system.repository.AssessmentPlanRepository;
import com.linln.modules.system.service.AssessmentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class AssessmentPlanServiceImpl implements AssessmentPlanService {
    @Autowired
    private AssessmentPlanRepository assessmentPlanRepository;
    @Override
    public Page<AssessmentPlan> fetchAssessmentPlanBySearch(String searchText,int state, Pageable request) {
        return assessmentPlanRepository.fetchScaleBySearch(searchText,state,request);
    }

    @Override
    public AssessmentPlan save(AssessmentPlan assessmentPlan) {
        assessmentPlanRepository.save(assessmentPlan);
        return null;
    }

    @Override
    public void updateState(String id) {
        int states = 0;
        AssessmentPlan assessmentPlan = assessmentPlanRepository.findOneById(id);
        if (assessmentPlan.getState()==1 || assessmentPlan.getState()==2){
            states = 3;
        }
        if (assessmentPlan.getState()==3){
            Date date = assessmentPlan.getEndTime();
            long time = date.getTime();
            long nowTime = System.currentTimeMillis();
            int days = (int) ((time - nowTime) / (1000 * 60 * 60 * 24));
            if (days>=0){
                states=1;
            }else {
                states=2;
            }
        }
        assessmentPlan.setState(states);
        assessmentPlanRepository.save(assessmentPlan);
//        assessmentPlanRepository.updateState(id,states);
    }

    @Override
    public AssessmentPlan findOneById(String id) {
        return assessmentPlanRepository.findOneById(id);
    }

    /**
     * 删除测评计划下的测评量表
     * @param assId
     * @param scaleId
     */
    @Override
    public boolean deleteScale(String assId, String scaleId) {
        return assessmentPlanRepository.deleteScale(assId,scaleId)>0;
    }

    /**
     * 添加量表到测评列表中
     * @param assId
     * @param scaleId
     * @return
     */
    @Override
    public boolean addScale(String assId, String scaleId) {
        return assessmentPlanRepository.addScale(assId,scaleId)>0;
    }

    @Override
    public List<AssessmentPlan> findAssessmentByDeptOrUser(String nickname, String deptName) {
        return assessmentPlanRepository.findAssessmentByDeptOrUser(nickname,deptName);
    }


}
