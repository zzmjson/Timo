package com.linln.modules.system.domain;

import com.linln.common.utils.StatusUtil;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_dialogue")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_dialogue", comment = "")
public class Dialogue extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '咨询问题交流表'")
    private String id;

    @Column(name="online_id",columnDefinition="VARCHAR(50) COMMENT '在线咨询表Id'")
    private String ONLineId;

    @Column(name="user_id",columnDefinition="VARCHAR(50) COMMENT '咨询用户'")
    private String userId;

    @Column(name="user_name",columnDefinition="VARCHAR(50) COMMENT '咨询用户名称'")
    private String userName;

    @Column(name="answer_id",columnDefinition="VARCHAR(50) COMMENT '咨询回复用户id'")
    private String answerId;

    @Column(name="answer_name",columnDefinition="VARCHAR(50) COMMENT '咨询用户名称'")
    private String answerName;

    @Column(name="reply_content",columnDefinition="VARCHAR(500) COMMENT '咨询用户名称'")
    private String replyContent;

}
