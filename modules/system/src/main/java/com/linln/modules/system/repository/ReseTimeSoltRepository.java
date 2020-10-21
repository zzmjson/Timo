package com.linln.modules.system.repository;

import com.linln.modules.system.domain.ReseTimeSolt;
import com.linln.modules.system.domain.UserPmq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14extends JpaRepository<Scale, Integer>
 */
public interface ReseTimeSoltRepository extends JpaRepository<ReseTimeSolt,String> {


    @Query(value = "SELECT * FROM sys_rese_timesolt WHERE counselor_id=?1",nativeQuery = true)
    public List<ReseTimeSolt> findReseByuser(String uid);

    @Query(value = "SELECT * FROM sys_rese_timesolt WHERE ?1>=star_date AND ?1<=end_date AND state=1",nativeQuery = true)
    public List<ReseTimeSolt> findReseByDate(String date);


}
