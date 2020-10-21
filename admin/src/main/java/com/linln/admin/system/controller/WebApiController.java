package com.linln.admin.system.controller;

import com.alibaba.fastjson.JSON;
import com.linln.admin.system.detailsRealize.TopicMaterialRealize;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.*;
import com.linln.modules.system.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@CrossOrigin
@RequiresPermissions("system:webApi:function")
@RequestMapping(value = "/webApi")
public class WebApiController {

    @Autowired
    TopicMaterialService topicMaterialService;

    @Autowired
    TopicMaterialRealize topicMaterialRealize;

    @Autowired
    AppraisalService appraisalService;
    @Autowired
    QuesService quesService;
    @Autowired
    QuesStatisService quesStatisService;
    @Autowired
    UserService userService;
    @Autowired
    ScaleService scaleService;



    @ResponseBody
    @GetMapping("/getTopic")   //获取量表题目
//    @RequiresPermissions("system:webApi:getTopic")
    public ResultVo  getTopic(@RequestParam(value = "scaleId")String scaleId) {
//        model.addAttribute("getAllSale", topicMaterialService.getAllSale(scaleId));
        Scale scale = scaleService.getOne(scaleId);
        List<Map<String, List<Map<String, Object>>>> allSale = topicMaterialService.getAllSale(scaleId, "1");
        Map<String,Object> map = new HashMap<>();
        map.put("scale",scale);
        map.put("topics",allSale);
        return ResultVoUtil.success(map);
    }



    /**
     * 保存用户答题信息(测评)
     * @param list
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/subScaleAnswer",method = RequestMethod.POST)
    @Transactional
    public ResultVo subScaleAnswer(@RequestParam(value = "assId")String assId, @RequestParam(value ="scaleId")String scaleId,@RequestParam(value = "list") String list) throws Exception {
        User user= ShiroUtil.getSubject();
        UserPmq userPmq=appraisalService.fetchScaleIdAndUserId(scaleId,user.getId()+"",assId);
        if(userPmq!=null){
            return ResultVoUtil.error("测评数据已存在，不可重复答题");
        }
        List<Map<String,List<Map<String,Object>>>> mapList=new ArrayList<>();
        List<Object> list1=JSON.parseArray(list);
        for(int i=0;i<list1.size();i++){
            String obj=list1.get(i).toString();
            Map<String,Object> map=JSON.parseObject(obj);
            Map<String,List<Map<String,Object>>> map1=new HashMap<>();
            List<Map<String,Object>> list2=new ArrayList<>();
            Set<String> keys=map.keySet();
            Iterator<String> iterator1=keys.iterator();
            String key="";
            while (iterator1.hasNext()){
                key=iterator1.next();
            }
            for ( int j=0;j<JSON.parseArray(JSON.toJSONString(map.get(key))).size();j++){
                Map<String,Object> map2=JSON.parseObject(JSON.parseArray(JSON.toJSONString(map.get(key))).get(j).toString());
                list2.add(map2);
            }
            map1.put(key,list2);
            mapList.add(map1);
            list1.set(i,map);
        }
        topicMaterialRealize.saveTopic(mapList,user.getId().toString(),assId);
        return ResultVoUtil.success("提交成功！");
    }


    @ResponseBody
    @GetMapping("/getQuesAll")   //获取已发布的问卷调查
//    @RequiresPermissions("system:webApi:getQuesAll")
    public ResultVo  getQuesAll(HttpServletResponse  response) throws IOException {
        User user= ShiroUtil.getSubject();
        List<Map<String,String>> getAllQues=quesService.getQuesAll(user.getId().toString(),user.getNickname(),user.getDept().getTitle());
//        model.addAttribute("getAllQues", getAllQues);
        return ResultVoUtil.success(getAllQues);
    }

    @ResponseBody
    @GetMapping("/getQuesTopic")   //根据问卷调查id获取题目
//    @RequiresPermissions("system:webApi:getQuesTopic")
    public ResultVo  getQuesTopic(@RequestParam(value = "id") String id ) throws IOException {
        List<List<Map<String,Object>>>  getAllSale= quesService.getAllQues(id);
        Questionnaire one = quesService.getOne(id);
        Map<String,Object> map = new HashMap<>();
        map.put("ques",one);
        map.put("allScale",getAllSale);
        return ResultVoUtil.success(map);
    }


    /**
     * 保存用户问卷调查答题信息
     * @param
     * @return
     * @throws Exception
     */
//    @ResponseBody
    @RequestMapping(value = "/subQuesAnswer",method = RequestMethod.POST)
    @Transactional
    public ResultVo subQuesAnswer(@RequestBody Map<String, Object> reqMap) throws Exception {
        User user= ShiroUtil.getSubject();
        String quesId=reqMap.get("quesId").toString();
        QuesStatistics quesStatistics=quesStatisService.fetchQuesIdAndUserId(quesId,user.getId()+"");
        if(quesStatistics!=null){
            return ResultVoUtil.error("测评数据已存在，不可重复答题");
        }
        List<List<Map<String,Object>>>  getQuesAll=new ArrayList<>();
        List<Object> getAll=JSON.parseArray(reqMap.get("list").toString());
        for(int i=0; i<getAll.size();i++){
            List<Object>  getQue=JSON.parseArray(getAll.get(i).toString());
            List<Map<String,Object>>  getMap=new ArrayList<>();
            for (int j=0;j<getQue.size();j++){
                Map<String,Object> map= JSON.parseObject(getQue.get(j).toString());
                getMap.add(map);
            }
           getQuesAll.add(getMap);
        }
//        System.out.println(JSON.toJSONString(getQuesAll));
        topicMaterialRealize.saveQuesTopic(getQuesAll,user);
        return ResultVoUtil.success("提交成功！");
    }

    @ResponseBody
    @GetMapping("/getQuesStatistics")   // 查询当前用户问卷调查的记录
//    @RequiresPermissions("system:webApi:getQuesStatistics")
    public ResultVo  getQuesStatistics()  {
        User user= ShiroUtil.getSubject();
         List<Map<String,String>>  getQuesStatistics= quesStatisService.getQuesAll(user.getId().toString());
        return ResultVoUtil.success(getQuesStatistics);
    }


    @ResponseBody
    @GetMapping("/answerInfo")   // 通过问卷调查记录id 获取用户答题详情
//    @RequiresPermissions("system:webApi:answerInfo")
    public ResultVo  answerInfo(@RequestParam(value = "id")String id)  {
        QuesStatistics quesStatistics=quesStatisService.getOne(id);
        Questionnaire questionnaire=quesService.getOne(quesStatistics.getQuesId());
        User user=userService.getById(Long.parseLong(quesStatistics.getUserId()));
        List<List<Map<String,Object>>>  getAllQues= appraisalService.getAllSale(quesStatistics.getQuesId(),"2",quesStatistics.getId());
        Map<String,Object> map=new HashedMap();
        map.put("getAllQues",getAllQues);
        map.put("queName",questionnaire.getTitle());
        map.put("uName",user.getNickname());
        map.put("message",questionnaire.getMessage());

        return ResultVoUtil.success(map);
    }













    public static void main(String[] args) {
        BigDecimal a=new BigDecimal("4998964");
        BigDecimal b=new BigDecimal("18.911998901367188");
        BigDecimal c=a.subtract(b);
        System.out.println(c.doubleValue());
        System.out.println(b.doubleValue());
        System.out.println(c.floatValue());
        DecimalFormat df = new DecimalFormat("0.0000");
        float dd=(float)Double.parseDouble(df.format(c));
        float ddd=(float) 4998945.0880 ;
        System.out.println(dd+">>"+ddd);
        System.out.println(df.format(c));
        System.out.println(Float.parseFloat(c.doubleValue()+""));
        System.out.println(Float.parseFloat(df.format(881010985)));
    }




}