package com.linln.modules.system.service;


import com.linln.modules.system.domain.QuesStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface QuesStatisService {

    QuesStatistics save(QuesStatistics quesStatistics);  //保存用户回答问卷调查记录

    Page<QuesStatistics> fetchQuesStatisAll(String searchText,String quesId,Pageable req);  //  当前问卷id,返回参与该问卷调查的记录，分页

    Page<Map<String,String>> fetchQuesStatisAll(String searchText,Pageable req);  //   返回所有参与该问卷调查的记录，分页

    QuesStatistics getOne(String id);  //根据id查询一条问卷调查记录

    QuesStatistics fetchQuesIdAndUserId(String quesId,String userId);//使用问卷调查表id和用户id,判断用户是否参与当前问卷调查
    //获取用已完成的问卷调查的记录
    List<Map<String,String>> getQuesAll(String userId);

    /**
     * 查询用户答题记录次数
     * @param userId
     * @return
     */
    Integer findCountByUserId(String userId);
}
