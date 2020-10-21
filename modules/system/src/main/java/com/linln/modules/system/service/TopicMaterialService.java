package com.linln.modules.system.service;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.TopicMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.listener.Topic;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface TopicMaterialService {



    /**
     * 保存量表
     * @param topicMaterial
     * @return
     */
    TopicMaterial save(TopicMaterial topicMaterial);


    List<TopicMaterial> findByScaleIdAndAppraisalTopicId(String scaleId,String no);

    void deleteTopic(String scaleId,String topicId);

    List<Map<String,List<Map<String,Object>>>> getAllSale(String saleId,String superior);


    TopicMaterial getOne(String id);





}
