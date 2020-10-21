package com.linln.admin.system.controller;


import com.alibaba.fastjson.JSON;
import com.linln.admin.system.detailsRealize.TopicMaterialRealize;
import com.linln.admin.system.validator.ContentTopicValidator;
import com.linln.admin.system.validator.ScaleValid;
import com.linln.common.utils.RandomUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.modules.system.domain.*;
import com.linln.modules.system.service.*;
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



/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/contentTopic")
public class ContentTopicController {


    @Autowired
    ContentTopicService contentTopicService;
    @Autowired
    ScaleService scaleService;
    @Autowired
    TopicMaterialRealize topicMaterialRealize;

    @Autowired
    TopicMaterialService topicMaterialService;
    @Autowired
    QuesService quesService;
    @Autowired
    GroupService groupService;
    /**
     * 列表页面
     */
    @GetMapping("/index/{id}")
//    @RequiresPermissions("system:contentTopic:index")
    public String index(@PathVariable("id") String  scaleId,
                        @RequestParam(value = "searchText",defaultValue = "") String searchText ,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model,  HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        Page<ContentTopic> list = contentTopicService.fetchContentTopicBySearch(scaleId,searchText,"1",req);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        model.addAttribute("scaleId", scaleId);
        model.addAttribute("superior", "1");
        return "/system/scale/topic";
    }



    /**
     * 跳转到题目添加页面
     */
    @GetMapping("/add/{scaleId}/{superior}")
//    @RequiresPermissions("system:contentTopic:add")
    public String toAdd(@PathVariable("scaleId")String scaleId,@PathVariable("superior")String superior, Model model) {
        List<Group> groups = groupService.findByScaleId(scaleId);
        model.addAttribute("groups",groups);
        model.addAttribute("scaleId",scaleId);
        model.addAttribute("superior",superior);
        return "/system/scale/topicAdd";
    }
//    /**
//     * 跳转到题目选项添加页面
//     */
//    @GetMapping("/addOption/{topicId}")
//    @RequiresPermissions("system:contentTopic:addOption")
//    public String addOption(@PathVariable("topicId")String topicId, Model model) {
//        model.addAttribute("topicId",topicId);
//        return "/system/scale/optionAdd";
//    }
//



    /**
     * 保存添加/修改的数据
     * @param contentTopicValidator
     * @param contentTopic
     * @return
     */
    @PostMapping("/save")
//    @RequiresPermissions({"system:contentTopic:add", "system:contentTopic:edit"})
    @ResponseBody
    @ActionLog(key=UserAction.USER_SAVE, action = UserAction.class)
    public ResultVo save(@Validated ContentTopicValidator contentTopicValidator,
                         @EntityParam ContentTopic contentTopic,
                         @RequestParam(value = "superior",defaultValue = "1")String superior ,
                         @RequestParam(value = "content")List<String> content ,
                         @RequestParam(value = "scores")List<String> scores ) throws Exception {

        if(contentTopic.getId()==null){   //添加
            Long id= RandomUtil.getId();
            contentTopic.setId(id+"");
            Integer checkNo=contentTopicService.checkContentTopic(contentTopic.getScaleId(),superior);
            if(checkNo==null){
                checkNo=0;
            }
            checkNo+=1;
            contentTopic.setNo(checkNo+"");
            if(superior.equals("1")){
                Scale scale=scaleService.getOne(contentTopic.getScaleId());
                contentTopic.setTitle(scale.getTitle());
            }else {

                Questionnaire questionnaire1=quesService.getOne(contentTopic.getScaleId());
                contentTopic.setTitle(questionnaire1.getTitle());
            }

        }else{    //编辑
            ContentTopic contentTopic1=contentTopicService.getOne(contentTopic.getId());
            contentTopic.setNo(contentTopic1.getNo());
            contentTopic.setScaleId(contentTopic1.getScaleId());
            contentTopic.setTitle(contentTopic1.getTitle());
            contentTopic.setModifyDateTime(new Date());

        }
        List<Group> byScaleId = groupService.findByScaleId(contentTopic.getScaleId());
        for (Group group : byScaleId) {
            if (contentTopic.getSerial().equals(group.getSerial()+"")){
                contentTopic.setInfo(group.getInfo());
                contentTopic.setGroupId(group.getId());
            }
        }
        contentTopic.setSuperior(superior); //上级类型 ：1、量表、2、问卷调查、
        if(!topicMaterialRealize.saveTopicMaterial(contentTopic.getScaleId(),contentTopic.getId(),contentTopic.getNo(),content,scores,contentTopic.getGroupId(),contentTopic.getSerial(),contentTopic.getInfo())){
            return ResultVoUtil.error("选项信息错误！");
        }
        contentTopicService.save(contentTopic);
        return ResultVoUtil.SAVE_SUCCESS;
    }


//
//    /**
//     * 获取排序部门列表
//     */
//    @GetMapping("/scaleTypeList")
//    @RequiresPermissions({"system:scale:add", "system:scale:edit"})
//    @ResponseBody
//    public Map<String, String> sortList(){
//        List<ScaleType> levelDept = scaleTableService.getBayAll();
//        Map<String, String> sortMap = new TreeMap<>();
//        for (int i = 1; i <= levelDept.size(); i++) {
//            sortMap.put(levelDept.get(i-1).getId(), levelDept.get(i - 1).getName());
//        }
//        return sortMap;
//    }
//
    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
//    @RequiresPermissions("system:contentTopic:edit")
    public String toEdit(@PathVariable("id") ContentTopic contentTopic, Model model) {
        List<TopicMaterial> topicMaterials=topicMaterialService.findByScaleIdAndAppraisalTopicId(contentTopic.getScaleId(),contentTopic.getId());
        List<Group> groups = groupService.findByScaleId(contentTopic.getScaleId());
        model.addAttribute("groups",groups);
        model.addAttribute("contentTopic", contentTopic);
        model.addAttribute("topicMaterials", topicMaterials);
        return "/system/scale/topicAdd";
    }

//    /**
//     * 跳转到题目选项编辑页面
//     */
//    @GetMapping("/edit/{id}")
//    @RequiresPermissions("system:contentTopic:edit")
//    public String toEdit(@PathVariable("id") ContentTopic contentTopic, Model model) {
//        model.addAttribute("contentTopic", contentTopic);
//        return "/system/scale/topicAdd";
//    }




}
