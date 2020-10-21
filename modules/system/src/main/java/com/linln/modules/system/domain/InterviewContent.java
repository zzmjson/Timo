package com.linln.modules.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sys_int_Content")
@EntityListeners(AuditingEntityListener.class)
public class InterviewContent extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '访谈记录内容表'")
    private String id;


    @Column(name="interview_Id",columnDefinition="VARCHAR(50) COMMENT '访谈记录表id'")
    private String interviewId;

    @Column(name="type_title",columnDefinition="VARCHAR(50) COMMENT '类型标题'")
    private String typeTitle;

    @Lob
    @Column(name="content",columnDefinition="Text COMMENT '内容'")
    private String content;




}
