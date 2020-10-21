package com.linln.modules.system.repository;

import com.linln.modules.system.domain.AsseScale;
import com.linln.modules.system.domain.Scale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14extends JpaRepository<Scale, Integer>
 */
public interface AsseScaleRepository extends JpaRepository<AsseScale,String> {



}
