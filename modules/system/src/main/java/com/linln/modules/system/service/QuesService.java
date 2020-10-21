package com.linln.modules.system.service;


import com.linln.modules.system.domain.Questionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface QuesService {

    Questionnaire save(Questionnaire questionnaire);  //保存问卷调查数据
    Page<Questionnaire> fetchQuestionnaire(String searchText,Pageable req);  //获取文件调查数据，分页
    List<List<Map<String,Object>>> getAllQues(String id);  //问卷调查表的id获取对应的模板题目
    Questionnaire getOne(String id);  //根据id查询一条问卷调查数据
    List<Map<String,String>>  getQuesAll(String userId,String nickname,String deptName); //获取已发布的问卷调查数据

    /**
     * 根据问卷id查询问卷的用户权限
     * @param id
     * @return
     */
    List findPerListById(String id);


}
