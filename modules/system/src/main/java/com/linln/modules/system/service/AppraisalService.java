package com.linln.modules.system.service;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.UserPmq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface AppraisalService {

    Page<UserPmq> fetchAppraisalBySearch(String searchText, Pageable request);

    UserPmq save(UserPmq userPmq);

    UserPmq fetchOne(String id);

    /**
     * 获取用户答题的信息
     * @param scaleId  量表id
     * @param superior 判断题目类型（问卷调查，量表）
     * @param assId  用户测评记录id /用户问卷调查记录表id
     * @return
     */
    List<List<Map<String,Object>>>  getAllSale(String scaleId,String superior,String assId);

    /**
     * 查询用户测评记录里是否存在答题记录
     * @param scaleId  量表id
     * @param userId 用户id
     * @param assId  测评计划id
     * @return
     */
    UserPmq fetchScaleIdAndUserId(String scaleId,String userId,String assId);

    List<UserPmq> fetchUserPmqByUser(String userId);
    Map<String ,Object> getUserPmqBys(String supId);

    int findCountByUser(String userId);

    /**
     * 根据测评计划查询答题记录列表
     * @param id
     * @return
     */
    List<Map<String,String>> findByAssId(String id);

    Page<UserPmq> findPageByUserIdAndAssesId(String userId,String assesId,Pageable request);

    /**
     * 根据量表id查询答题统计信息
     * @param saleId
     * @return
     */
    List<List<Map<String, Object>>> getAllByscaleId(String saleId);

    /**
     * 因子统计，计数
     * @param userId
     * @param scaleId
     * @param serial
     * @return
     */
    Map<String,String> findStatByCount(String userId,String scaleId,int serial);

    /**
     * 因子统计 求平均
     * @param userId
     * @param scaleId
     * @param serial
     * @return
     */
    Map<String,String> findStatByAVG(String userId,String scaleId,int serial);

    /**
     * 因子统计，求和
     * @param userId
     * @param scaleId
     * @param serial
     * @return
     */
    Map<String,String> findStatBySUM(String userId,String scaleId,int serial);
}
