package com.linln.admin.system.validator;

import com.linln.modules.system.domain.Dept;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class AssessmentPlanValid implements Serializable {

    @NotNull(message = "计划标题不能为空")
    private String title;

}
