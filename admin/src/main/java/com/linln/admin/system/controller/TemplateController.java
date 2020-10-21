package com.linln.admin.system.controller;


import com.linln.admin.system.detailsRealize.SummaryRealize;
import com.linln.admin.system.validator.ScaleValid;
import com.linln.common.utils.RandomUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.ScaleType;
import com.linln.modules.system.domain.Summary;
import com.linln.modules.system.domain.Template;
import com.linln.modules.system.service.ScaleService;
import com.linln.modules.system.service.ScaleTableService;
import com.linln.modules.system.service.SummaryService;
import com.linln.modules.system.service.TemplateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//import com.linln.modules.system.service.ScaleService;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/template")
@RequiresPermissions("system:template:index")
public class TemplateController {

    @Autowired
    TemplateService templateService;

    @GetMapping("/index")
//    @RequiresPermissions("system:template:index")
    public String index(  Model model,  HttpServletRequest request) {
        Template template=templateService.getOne("1");
        model.addAttribute(template);
        return "/system/template/index";
    }



    @GetMapping("/index1")
    @RequiresPermissions("system:template:index1")
    public String index1(  Model model,  HttpServletRequest request) {
        Template template=templateService.getOne("1");
        model.addAttribute(template);
        return "/system/template/index1";
    }
    /**
     * 保存添加/修改的数据
     *
     * @param
     * @return
     */
    @PostMapping("/save")
    @RequiresPermissions({"system:template:add", "system:template:edit"})
    @ResponseBody
    @ActionLog(key=UserAction.USER_SAVE, action = UserAction.class)
    public ResultVo save( @EntityParam Template template ) throws Exception {
        templateService.save(template);
        return ResultVoUtil.SAVE_SUCCESS;
    }


}
