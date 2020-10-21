package com.linln.admin.system.controller;

import com.linln.modules.system.domain.QuesStatistics;
import com.linln.modules.system.service.QuesStatisService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/system/survey")
@RequiresPermissions("system:survey:index")
public class SurveyController {

    @Autowired
    QuesStatisService quesStatisService;

    /**
     * 进入问卷管理首页
     * @return
     */
    @RequestMapping("/index")
//    @RequiresPermissions("system:survey:index")
    public String index(){
        return "/system/survey/index";
    }

    /**
     * 跳转到问卷预览页面
     * @return
     */
    @RequestMapping("/preview")
//    @RequiresPermissions("system:survey:preview")
    public String preview(){
        return "/system/survey/preview";
    }

    @RequestMapping("/surveyFile")
    @RequiresPermissions("system:survey:surveyFile")
    public String surveyFile(){



        return "/system/survey/surveyFile";
    }


    /**
     *  调查档案页面
     */
    @GetMapping("/surveyFile")
//    @RequiresPermissions("system:question:surveyFile")
    public String gather(@RequestParam(value = "searchText",defaultValue = "") String searchText ,
                         @RequestParam(value = "page",defaultValue = "1") Integer page,
                         @RequestParam(value = "size",defaultValue = "10") Integer size,
                         Model model, HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        Page<Map<String,String>> list = quesStatisService.fetchQuesStatisAll(searchText, req);  //获取所有问卷调查记录
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
//        model.addAttribute("quesId", quesId);  //问卷调查id
//        model.addAttribute("superior", "2");  // 题目表区分问卷调查和量表的类型
        return "/system/survey/surveyFile";
    }







}
