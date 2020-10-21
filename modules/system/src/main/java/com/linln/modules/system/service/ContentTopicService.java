package com.linln.modules.system.service;

import com.linln.modules.system.domain.ContentTopic;
import com.linln.modules.system.domain.Scale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface ContentTopicService {

    Page<ContentTopic> fetchContentTopicBySearch(String scaleId,String searchText,String  superior, Pageable request);


    /**
     * 保存量表
     * @param scale
     * @return
     */
    ContentTopic save(ContentTopic scale);


    Integer checkContentTopic(String scaleId,String superior);


    ContentTopic getOne(String id);

    /**
     * 根据量表查询题目跟选项
     * @param scaleId
     * @return
     */
    public List<Map<String,String>> findTocAndMelByScale(String scaleId);


}
