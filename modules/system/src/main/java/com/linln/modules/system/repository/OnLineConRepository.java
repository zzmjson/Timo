package com.linln.modules.system.repository;

import com.linln.modules.system.domain.ONLineConsulting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OnLineConRepository  extends JpaRepository<ONLineConsulting,Integer> {



    @Query(value = " select * from oNLine where CONCAT(userName,consultTitle,'') like %?1%  ORDER BY createDateTime DESC  ",nativeQuery = true)
    Page<ONLineConsulting> fetchONLineBySearch(String searchText, Pageable request);






}
