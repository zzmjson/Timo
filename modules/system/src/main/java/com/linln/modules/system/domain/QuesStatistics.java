package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sys_statistics")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_statistics", comment = "")
public class QuesStatistics extends BaseStringEntity {

    public QuesStatistics(String id, String userId, String userName, String quesId ) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.quesId = quesId;
        this.calculateTime = new Date();
        this.submitTime = new Date();
        this.answerRequired = "";
        this.getNumber = 0;
    }
    public QuesStatistics() {
    }

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '问卷调查用户答题统计表 '")
    private String id;

    @Column(name="user_id",columnDefinition="VARCHAR(50) COMMENT '答题用户id'")
    private String userId;

    @Column(name="user_name",columnDefinition="VARCHAR(50) COMMENT '答题用户名称'")
    private String userName;

    @Column(name="ques_id",columnDefinition="VARCHAR(50) COMMENT '问卷调查id'")
    private String quesId;

    @Column(name="calculate_Time",columnDefinition="DATETIME COMMENT '计算总分时间'")
    private Date calculateTime;

    @Column(name="submit_Time",columnDefinition="DATETIME COMMENT '提交答案时间'")
    private Date submitTime;

    @Column(name="answer_required",columnDefinition="varchar(255) COMMENT '答题时长'")
    private String answerRequired;


    @Column(name="get_Number",columnDefinition="int COMMENT '是否参与统计(0 否 1 是)'")
    private Integer getNumber=0;




}
