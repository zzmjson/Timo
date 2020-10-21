package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.ContentTopic;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.repository.ContentTopicRepository;
import com.linln.modules.system.repository.ScaleRepository;
import com.linln.modules.system.service.ContentTopicService;
import com.linln.modules.system.service.ScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class ContentTopicServiceImpl implements ContentTopicService {


    @Autowired
    ContentTopicRepository contentTopicRepository;

    @Override
    public Page<ContentTopic> fetchContentTopicBySearch(String scaleId,String searchText,String superior, Pageable request) {
        return contentTopicRepository.fetchContentTopicBySearch(scaleId,searchText,superior,request);
    }

    @Override
    public ContentTopic save(ContentTopic contentTopic) {
        return contentTopicRepository.save(contentTopic);
    }

    @Override
    public Integer checkContentTopic(String scaleId,String superior) {
        return contentTopicRepository.checkContentTopic(scaleId,superior);
    }

    @Override
    public ContentTopic getOne(String id) {
        return contentTopicRepository.getOne(id);
    }


    @Override
    public List<Map<String, String>> findTocAndMelByScale(String scaleId) {
       return contentTopicRepository.findTocAndMelByScale(scaleId);
    }

    //筛选数据
    public static List<List<Topic>> checkDate(List<Map<String,String>> list){
        List result = new ArrayList();
        for (Map<String, String> map : list) {
        }
        return null;
    }
    public static void main(String[] args) {
        ContentTopicServiceImpl c = new ContentTopicServiceImpl();
        System.out.println(c.findTocAndMelByScale("158918054030300126"));
    }


}