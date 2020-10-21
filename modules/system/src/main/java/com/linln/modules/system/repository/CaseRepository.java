package com.linln.modules.system.repository;

import com.linln.modules.system.domain.CaseSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


/**
 * @author 小懒虫
 * @date 2018/8/14extends JpaRepository<Scale, Integer>
 */
public interface CaseRepository extends JpaRepository<CaseSet,String> {


    @Query(value = "   select scs.id,scs.content,ssc.type from sys_case_set as scs,sys_case_set as ssc   where scs.pid=ssc.id and if(?2='',1=1,ssc.id=?2) and  scs.content  is not null and  CONCAT('')  like %?1%  order by scs.create_Date_Time  desc      ",nativeQuery = true)
    Page<Map<String,Object>> fetchCaseSetBySearch(String searchText,String type, Pageable request);


    @Query(value = "SELECT  scs.id,scs.content FROM sys_case_set as scs WHERE pid ='a1'",nativeQuery = true)
    List<Map<String,String>> findSourse();

    @Query(value = "SELECT  scs.id,scs.content FROM sys_case_set as scs WHERE pid ='g1'",nativeQuery = true)
    List<Map<String,String>> findLevel();

}
