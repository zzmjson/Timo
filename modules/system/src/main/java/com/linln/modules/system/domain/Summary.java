package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_summary")
@EntityListeners(AuditingEntityListener.class)
public class Summary extends BaseStringEntity {



    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '量表结论'")
    private String id;

    @Column(name="grade",columnDefinition="VARCHAR(50) COMMENT '分数范围'")
    private String grade;
    @Lob
    @Column(name="content_Summary",columnDefinition="Text COMMENT '结论内容'")
    private String contentSummary="";
    @Lob
    @Column(name = "suggest",columnDefinition = "text comment ''")
    private  String suggest="";


    @Column(name="scale_id",columnDefinition="VARCHAR(50) COMMENT '量表id'")
    private String scaleId;



}
