package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_template")
@EntityListeners(AuditingEntityListener.class)
public class Template extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '模板表'")
    private String id;

    @Lob
    @Column(name="protocol_title",columnDefinition="VARCHAR(500) COMMENT '协议标题'")
    private String protocolTitle;

    @Lob
    @Column(name="protocol_content",columnDefinition="text COMMENT '协议内容'")
    private String protocolContent;

    @Lob
    @Column(name="summarize",columnDefinition="text COMMENT '报告结语'")
    private String summarize="";

    @Column(name="state",columnDefinition="VARCHAR(50) COMMENT '是否显示结语 1 是 0 否 '")
    private String state="1";




}
