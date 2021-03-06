package com.linln.admin.system.validator;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class QuesValid implements Serializable {


    @NotEmpty(message = "问卷标题不能为空")
    private String title;

}
