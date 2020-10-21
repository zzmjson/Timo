package com.linln.modules.system.repository;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.Template;
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
public interface TemplateRepository extends JpaRepository<Template,String> {


    @Query(value = "SELECT st.protocol_title,st.protocol_content FROM sys_template AS st WHERE id='1'",nativeQuery = true)
    Map<String,String> getAsseTemp();


}
