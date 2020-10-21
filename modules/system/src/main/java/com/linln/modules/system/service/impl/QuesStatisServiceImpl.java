package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.QuesStatistics;
import com.linln.modules.system.repository.QuesStatisRepository;
import com.linln.modules.system.service.QuesStatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class QuesStatisServiceImpl implements QuesStatisService {

    @Autowired
    QuesStatisRepository quesStatisRepository;

    @Override
    public QuesStatistics save(QuesStatistics quesStatistics) {
        return quesStatisRepository.save(quesStatistics);
    }

    @Override
    public Page<QuesStatistics> fetchQuesStatisAll(String searchText, String quesId, Pageable req) {
        return quesStatisRepository.fetchQuesStatisAll(searchText,quesId,req);
    }

    @Override
    public   Page<Map<String,String>> fetchQuesStatisAll(String searchText, Pageable req) {
        return quesStatisRepository.fetchQuesStatisAll(searchText,req);
    }

    @Override
    public QuesStatistics getOne(String id) {
        return quesStatisRepository.getOne(id);
    }

    @Override
    public QuesStatistics fetchQuesIdAndUserId(String quesId, String userId) {
        return quesStatisRepository.fetchQuesIdAndUserId(quesId,userId);
    }

    @Override
    public List<Map<String,String>> getQuesAll(String userId) {
        return quesStatisRepository.getQuesAll(userId);
    }

    @Override
    public Integer findCountByUserId(String userId) {
        return quesStatisRepository.findCountByUserId(userId);
    }
}