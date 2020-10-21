package com.linln.admin.system.validator;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class CaseSetValid implements Serializable {
    @NotEmpty(message = "个案设置内容不能为空")
    private String content="";

}
