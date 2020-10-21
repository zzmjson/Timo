package com.linln.modules.system.service;

import com.linln.modules.system.domain.AsseScale;
import com.linln.modules.system.domain.Scale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface AsseScaleService {



    /**
     * 保存量表
     * @param scale
     * @return
     */
    AsseScale save(AsseScale scale);



}
