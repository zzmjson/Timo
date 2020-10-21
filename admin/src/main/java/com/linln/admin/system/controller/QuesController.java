package com.linln.admin.system.controller;


import com.alibaba.fastjson.JSON;
import com.linln.admin.system.detailsRealize.SummaryRealize;
import com.linln.admin.system.validator.QuesValid;
import com.linln.admin.system.validator.ScaleValid;
import com.linln.common.utils.MsgUtil;
import com.linln.common.utils.RandomUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.*;
import com.linln.modules.system.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

//import com.linln.modules.system.service.ScaleService;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/question")
@RequiresPermissions("system:question:index")
public class QuesController {

    @Autowired
    QuesService quesService;

    @Autowired
    ContentTopicService contentTopicService;
    @Autowired
    QuesStatisService quesStatisService;
    @Autowired
    AppraisalService appraisalService;
    @Autowired
    UserService userService;
    @Autowired
    DeptService deptService;


    /**
     * 列表页面
     */
    @GetMapping("/index")
//    @RequiresPermissions("system:question:index")
    public String index(@RequestParam(value = "searchText",defaultValue = "") String searchText ,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model,  HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        Page<Questionnaire> list = quesService.fetchQuestionnaire(searchText,req);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/survey/index";
    }


    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
//    @RequiresPermissions("system:question:add")
    public String toAdd() {
        return "/system/survey/add";
    }


    /**
     * 保存添加/修改的数据
     * @param quesValid
     * @param questionnaire
     * @return
     */
    @PostMapping("/save")
//    @RequiresPermissions({"system:question:add", "system:question:edit"})
    @ResponseBody
    @ActionLog(key=UserAction.USER_SAVE, action = UserAction.class)
    public ResultVo save(@Validated QuesValid quesValid, @EntityParam Questionnaire questionnaire
                         ) throws Exception {
        User user= ShiroUtil.getSubject();
        System.out.println(questionnaire);
        if(questionnaire.getId()==null){
            questionnaire.setId( RandomUtil.getId().toString());
            questionnaire.setCreateUser(user.getId().toString());
        }else {
            Questionnaire questionnaire1=quesService.getOne(questionnaire.getId());
            questionnaire.setCreateUser(questionnaire1.getCreateUser());
            questionnaire.setCreateDateTime(questionnaire1.getCreateDateTime());
            questionnaire.setNumber(questionnaire1.getNumber());
        }
        questionnaire.setModifyUser(user.getId().toString());
        questionnaire.setModifyDateTime(new Date());
        quesService.save(questionnaire);
        return ResultVoUtil.SAVE_SUCCESS;
    }


    /**
     * 修改状态
     * @param id
     * @param name
     * @return
     */
    @GetMapping("/updataQues")
//    @RequiresPermissions({"system:question:updataQues", "system:question:updataQues"})
    @ResponseBody
    public ResultVo updataQues(@RequestParam(value = "id")String id,@RequestParam(value = "name")String name ) throws Exception {
        User user= ShiroUtil.getSubject();
        Questionnaire questionnaire1=quesService.getOne(id);
        if(name.equals("viewing")){
           int viewing= Integer.parseInt(questionnaire1.getViewing());
           if(viewing==1){
               questionnaire1.setViewing("2");
           }else {
               questionnaire1.setViewing("1");
           }
        }else if(name.equals("state")){
            int state= Integer.parseInt(questionnaire1.getState());
            if(state==1){
                questionnaire1.setState("2");
            }else {
                questionnaire1.setState("1");
            }
        }
        questionnaire1.setModifyUser(user.getId().toString());
        questionnaire1.setModifyDateTime(new Date());
        quesService.save(questionnaire1);
        return ResultVoUtil.SAVE_SUCCESS;
    }



    /**
     * 列表页面（题目列表显示页面）
     */
    @GetMapping("/questionList/{id}")
//    @RequiresPermissions("system:question:index")
    public String index(@PathVariable("id") String  quesId,
                        @RequestParam(value = "searchText",defaultValue = "") String searchText ,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model,  HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        Page<ContentTopic> list = contentTopicService.fetchContentTopicBySearch(quesId,searchText,"2",req);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        model.addAttribute("scaleId", quesId);
        model.addAttribute("superior", "2");
        return "/system/scale/topic";
    }


    /**
     * 列表页面收集页面
     */
    @GetMapping("/gather/{id}")
//    @RequiresPermissions("system:question:gather")
    public String gather(@PathVariable("id") String  quesId,
                        @RequestParam(value = "searchText",defaultValue = "") String searchText ,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model,  HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        Page<QuesStatistics> list = quesStatisService.fetchQuesStatisAll(searchText,quesId, req);  //获取当前问卷调查下的参与记录
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        model.addAttribute("quesId", quesId);  //问卷调查id
        model.addAttribute("superior", "2");  // 题目表区分问卷调查和量表的类型
        return "/system/survey/statis";
    }



    @RequestMapping(value = "/template/{quesId}")
//    @RequiresPermissions("system:question:template")
    public String record(@PathVariable("quesId") String quesId,Model model){
        List<List<Map<String,Object>>>  getAllSale= quesService.getAllQues(quesId);
        Questionnaire questionnaire1=quesService.getOne(quesId);
        model.addAttribute("list",getAllSale);
        model.addAttribute("questionnaire1",questionnaire1);
        return "/system/survey/preview";
    }




    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
//    @RequiresPermissions("system:question:edit")
    public String toEdit(@PathVariable("id") Questionnaire questionnaire, Model model) {
        model.addAttribute("questionnaire", questionnaire);
        return "/system/survey/add";
    }


    //通过问卷调调查记录id 获取用户答题记录
    @RequestMapping(value = "/quesRecord/{queSId}")
//    @RequiresPermissions("system:question:quesRecord")
    public String quesRecord(@PathVariable("queSId") String queSId,Model model){
        //获取答题信息
        QuesStatistics quesStatistics=quesStatisService.getOne(queSId);
        //获取问卷信息
        Questionnaire questionnaire=quesService.getOne(quesStatistics.getQuesId());
        List<List<Map<String,Object>>>  getAllQues= appraisalService.getAllSale(quesStatistics.getQuesId(),"2",quesStatistics.getId());
        model.addAttribute("list",getAllQues);
        model.addAttribute("quesS",questionnaire);
        return "/system/survey/record";
    }
    //跳转到调查调查统计页面
    @RequestMapping(value = "/quesStatistics/{queSId}")
    public String quesStatistics(@PathVariable("queSId") String queSId,Model model){
        //获取答题信息
        List<List<Map<String, Object>>> allByscaleId = appraisalService.getAllByscaleId(queSId);
        //获取调查表信息
        Questionnaire one = quesService.getOne(queSId);
        model.addAttribute("obj",one);
        model.addAttribute("list",allByscaleId);
        return "/system/survey/collent";
    }




    /**
     * 修改参与统计状态
     */
    @GetMapping("/updateNumber")
//    @RequiresPermissions("system:question:updateNumber")
    @ResponseBody
    @Transactional
    public ResultVo toEdit(@RequestParam("id") String id ,@RequestParam("getNumber") Integer getNumber ) {
        QuesStatistics quesStatistics=quesStatisService.getOne(id);
        User user= ShiroUtil.getSubject();
        if(quesStatistics.getGetNumber()==0){
            quesStatistics.setGetNumber(1);
        }else {
            quesStatistics.setGetNumber(0);
        }
        quesStatistics.setModifyUser(user.getId().toString());
        quesStatistics.setModifyDateTime(new Date());
        quesStatisService.save(quesStatistics);
//        String url="/system/question/gather/"+quesStatistics.getQuesId();
        return ResultVoUtil.success(MsgUtil.ADDSUCCESS);
    }

    /**
     * 跳转到筛选用户条件页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/permission/{id}")
    public String detail(@PathVariable("id") String id,Model model){
        List list = quesService.findPerListById(id);
        model.addAttribute("permission",list);
        model.addAttribute("quesId",id);
        return "/system/survey/permission";
    }

    /**
     * 删除筛选用户条件
     * @param quesId
     * @param type
     * @param deptName
     * @param nickname
     * @return
     */
    @ResponseBody
    @RequestMapping("/deletePerList")
    public ResultVo deletePerList(@RequestParam("quesId") String quesId,
                                  @RequestParam("type") String type,
                                  @RequestParam(value = "deptName",defaultValue = "不为空") String deptName,
                                  @RequestParam(value = "nickname",defaultValue = "不为空") String nickname){
        System.out.println(quesId);
        System.out.println(type);
        System.out.println(deptName);
        System.out.println(nickname);
        Questionnaire one = quesService.getOne(quesId);
        String str = one.getPerList();
        String[] split = str.split(",");
        String s="";
        for(int i=0;i<split.length;i++){
            if (split[i].indexOf(deptName)!=-1){
                s = str.replaceAll(split[i]+",", "");
                break;
            }
            if (split[i].indexOf(nickname)!=-1){
                s = str.replaceAll(split[i]+",", "");
                break;
            }
        }
        if ("".equals(s)){
            s=null;
        }
        one.setPerList(s);
        quesService.save(one);
        return ResultVoUtil.success("删除成功！");
    }

    /**
     * 跳转到添加筛选用户权限页面
     * @param quesId
     * @param model
     * @return
     */
    @RequestMapping("/addPerList")
    public String addPerList(@RequestParam(value = "quesId",defaultValue = "") String quesId,
                             Model model){
        List<Map<String, String>> users = userService.findUsers();
        List<Dept> deptAll = deptService.findDeptAll();
        Questionnaire one = quesService.getOne(quesId);
        String perList = one.getPerList();
        if (perList!=null){
            String [] str = perList.split(",");
            Iterator<Map<String, String>> iterator = users.iterator();
            while (iterator.hasNext()){
                if (perList.indexOf(iterator.next().get("nickname"))!=-1){
                    iterator.remove();
                }
            }
            Iterator<Dept> iterator1 = deptAll.iterator();
            while (iterator1.hasNext()){
                if (perList.indexOf(iterator1.next().getTitle())!=-1){
                    iterator1.remove();
                }
            }
        }
        model.addAttribute("quesId",quesId);
        model.addAttribute("users",users);
        model.addAttribute("depts",deptAll);
        return "/system/survey/addPerList";
    }

    /**
     * 保存筛选用户条件
     * @param users
     * @param depts
     * @param quesId
     * @return
     */
    @RequestMapping("/savePerList")
    @ResponseBody
    public ResultVo savePerList(@RequestParam(value = "userList",defaultValue = " ") String users,
                                @RequestParam(value = "deptList",defaultValue = " ") String depts,
                                @RequestParam(value = "quesId",defaultValue = "") String quesId){
        Questionnaire one = quesService.getOne(quesId);
        String perList="";
        if (one.getPerList()!=null){
            perList = one.getPerList();
        }
        int temp = 0;
        if (!users.equals(" ")){
            String [] str = users.split(",");
            for (int i=0;i<str.length;i++){
                temp = Integer.valueOf(str[i]);
                User user = userService.getById((long) temp);
                String s = "2:"+" "+"/"+" "+":"+user.getUsername()+"/"+user.getNickname()+",";
                perList +=s;
            }
            one.setPerList(perList);
        }
        if (!depts.equals(" ")){
            String [] dept = depts.split(",");
            for (int i=0;i<dept.length;i++){
                temp = Integer.valueOf(dept[i]);
                Dept dept1 = deptService.getById((long)temp);
                String s = "1:"+dept1.getTitle()+"/"+dept1.getId()+":"+" "+"/"+" "+",";
                perList +=s;
            }
            one.setPerList(perList);
        }
        quesService.save(one);
        return ResultVoUtil.SAVE_SUCCESS;
    }



//
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
//    /**
//     * 跳转到编辑页面
//     */
//    @GetMapping("/edit/{id}")
//    @RequiresPermissions("system:scale:edit")
//    public String toEdit(@PathVariable("id") Scale scale, Model model) {
//        List<Summary>  summaries=summaryService.findByScaleIdGradeOrderByAsc(scale.getId());
//
//        model.addAttribute("summaries", summaries);
//        model.addAttribute("scale", scale);
//        return "/system/scale/add";
//    }
//





}
