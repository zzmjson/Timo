package com.linln.admin.system.controller;


import com.alibaba.fastjson.JSON;
import com.linln.admin.system.validator.CaseSetValid;
import com.linln.common.utils.RandomUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.modules.system.domain.CaseSet;
import com.linln.modules.system.domain.Report;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.service.CaseService;
import com.linln.modules.system.service.ReportService;
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
import java.util.Map;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/caseSet")
@RequiresPermissions("system:caseSet:index")
public class CaseSetController {

    @Autowired
    CaseService caseService;
    @Autowired
    ReportService reportService;


    /**
     * 列表页面
     */
    @GetMapping("/index")
//    @RequiresPermissions("system:caseSet:index")
    public String index(@RequestParam(value = "searchText",defaultValue = "") String searchText ,
                        @RequestParam(value = "type",defaultValue = "") String type,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model,  HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        Page<Map<String,Object>> list = caseService.fetchCaseSetBySearch(searchText,type,req);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/caseSet/index";
    }


    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
//    @RequiresPermissions("system:caseSet:add")
    public String toAdd() {
        return "/system/caseSet/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid
     * @param caseSet
     * @return
     */
    @PostMapping("/save")
//    @RequiresPermissions({"system:case:addSet", "system:case:edit"})
    @ResponseBody
    @ActionLog(key= UserAction.USER_SAVE, action = UserAction.class)
    public ResultVo save(@Validated CaseSetValid valid, @EntityParam CaseSet caseSet) throws Exception {

        System.out.println(System.out.printf(JSON.toJSONString(caseSet)));
        if(caseSet.getId()==null){   //添加
            Long id= RandomUtil.getId();
            caseSet.setId(id+"");
        }else{    //编辑
            caseSet.setModifyDateTime(new Date());
        }
        caseService.save(caseSet);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public ResultVo delete(@RequestParam(value = "id") String id){
        System.out.println(id);
        CaseSet one = caseService.findById(id);
        boolean isTrue = reportService.isHasDataByCaseIdOrRankid(id);
        if (isTrue){
            caseService.delete(id);
            return ResultVoUtil.success("删除成功");

        }
        return  ResultVoUtil.error("有关联数据，不能删除");

    }












}
