package com.linln.modules.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.TopicMaterial;
import com.linln.modules.system.repository.ScaleRepository;
import com.linln.modules.system.repository.TopicMaterialRepository;
import com.linln.modules.system.service.ScaleService;
import com.linln.modules.system.service.TopicMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class TopicMaterIalServiceImpl implements TopicMaterialService {

    @Autowired
    TopicMaterialRepository topicMaterialRepository;

    @Override
    public TopicMaterial save(TopicMaterial topicMaterial) {
        return topicMaterialRepository.save(topicMaterial);
    }

    @Override
    public List<TopicMaterial> findByScaleIdAndAppraisalTopicId(String scaleId,String no) {
        return topicMaterialRepository.findByScaleIdAndTopicId(scaleId,no );
    }

    @Override
    public void deleteTopic(String scaleId, String topicId) {
        topicMaterialRepository.deleteTopic(scaleId,topicId);
    }

    @Override
    public   List<Map<String,List<Map<String,Object>>>> getAllSale(String saleId,String superior) {
        List<Map<String,List<Map<String,Object>>>>   packaging=new ArrayList<>();
        List<Map<String,Object>>    getTopicAll=topicMaterialRepository.getAllSale(saleId,superior);
        String noAll="";
        for (int i=0;i<getTopicAll.size();i++){
            String no= getTopicAll.get(i).get("no").toString();
            List<Map<String,Object>>   aa=getTopicAll.stream().filter(item->item.get("no").toString().equals(no)).collect(Collectors.toList());
            if(noAll.indexOf("["+no+"]")<0){
                noAll+="["+no+"]";
                Map<String,List<Map<String,Object>>>   map=new HashMap<>();
                map.put(getTopicAll.get(i).get("no").toString(),aa);
                packaging.add(map);
            }
        }
        return packaging;
    }

    @Override
    public TopicMaterial getOne(String id) {
        return topicMaterialRepository.getOne(id);
    }


}