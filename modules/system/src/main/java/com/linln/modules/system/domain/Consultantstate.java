package com.linln.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.StatusUtil;
import com.linln.component.excel.annotation.Excel;
import com.linln.component.excel.enums.ExcelType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
@Entity
@Table(name = "sys_con_state")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_con_state", comment = "")
public class Consultantstate extends BaseStringEntity {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50) comment '咨询师状态表'")
    private String id;
    @Column(name = "chief", columnDefinition = "int COMMENT '是否开启咨询师功能状态 1 开启 0关闭'")
    private int chief;
    @Column(name = "in_List", columnDefinition = "int COMMENT '是否在咨询师列表中 1 在 0 不在'")
    private int inList;
    @Column(name = "online_Consult", columnDefinition = "int COMMENT '是否开启在线咨询功能 1 开启 0 关闭'")
    private int onlineConsult;
    @Column(name = "reser_Consult", columnDefinition = "int COMMENT '是否启用预约咨询功能 1 开启 0 关闭'")
    private int reserConsult;

}
