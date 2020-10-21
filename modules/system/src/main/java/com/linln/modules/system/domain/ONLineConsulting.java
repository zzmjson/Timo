package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Data
@Entity
@Table(name = "sys_online")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_online", comment = "")
public class ONLineConsulting extends BaseStringEntity  {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '在线咨询表'")
    private String id;

    @Lob
    @Column(name="consult_title",columnDefinition="TEXT COMMENT '咨询主题'")
    private String consultTitle ;

    @Lob
    @Column(name="consult_content",columnDefinition="LONGTEXT COMMENT '咨询详细描述'")
    private String consultContent;

    @Column(name="user_id",columnDefinition="VARCHAR(50) COMMENT '咨询用户'")
    private String userId;

    @Column(name="user_name",columnDefinition="VARCHAR(50) COMMENT '咨询用户名称'")
    private String userName;

    @Column(name="answer_id",columnDefinition="VARCHAR(50) COMMENT '咨询回复用户id'")
    private String answerId;

    @Column(name="answer_name",columnDefinition="VARCHAR(50) COMMENT '咨询用户名称'")
    private String answerName;

    @Lob
    @Column(name="con_type",columnDefinition="VARCHAR(200) COMMENT '咨询类型'")
    private String conType;

    @Column(name="con_state",columnDefinition="VARCHAR(50) COMMENT '咨询状态：1、未查看，2、未回复，3.暂无新信息，4、问题已关闭'")
    private String conState;


}
