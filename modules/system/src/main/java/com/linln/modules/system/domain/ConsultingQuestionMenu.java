package com.linln.modules.system.domain;

import com.linln.common.utils.StatusUtil;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_con_que_menu")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_con_que_menu", comment = "")
public class ConsultingQuestionMenu extends BaseStringEntity {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50) comment '咨询功能菜单表'")
    private String id;
    @Column(name = "name", columnDefinition = "VARCHAR(50) COMMENT '名称'")
    private String name;
    @Column(name = "consulting_id", columnDefinition = "VARCHAR(50) COMMENT '咨询基类Id'")
    private String consultingId;
    @Column(name = "user_id", columnDefinition = "VARCHAR(50) COMMENT '咨询师Id'")
    private String userId;
    @Column(name = "state", columnDefinition = "int COMMENT '状态'")
    private int state;


}