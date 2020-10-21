package com.linln.modules.system.service;

import com.linln.modules.system.domain.ReseTimeSolt;
import com.linln.modules.system.domain.Scale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface ReseTimeSoltService {

    ReseTimeSolt save(ReseTimeSolt reseTimeSolt);
    public List<ReseTimeSolt> findReseByuser(String uid);
    public List<ReseTimeSolt> findReseByDate(String date);

}
