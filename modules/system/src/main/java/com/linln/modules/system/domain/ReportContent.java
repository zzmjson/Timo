package com.linln.modules.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "sys_report_content")
@EntityListeners(AuditingEntityListener.class)
public class ReportContent extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '个人报告表内容'")
    private String id;

    @Column(name="title",columnDefinition="VARCHAR(255) COMMENT '标题'")
    private String title;

    @Column(name="rid",columnDefinition="VARCHAR(50) COMMENT '自连接id'")
    private String rid;

    @Column(name="tiny_title",columnDefinition="VARCHAR(50) COMMENT '小标题'")
    private String tinyTitle;

    @Lob
    @Column(name="content",columnDefinition="TEXT COMMENT '内容'")
    private String content;

    @Column(name="report_id",columnDefinition="VARCHAR(50) COMMENT '个人报告表id'")
    private String reportId;


}
