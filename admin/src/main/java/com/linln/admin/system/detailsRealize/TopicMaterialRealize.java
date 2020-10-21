package com.linln.admin.system.detailsRealize;

import com.linln.common.utils.RandomUtil;
import com.linln.modules.system.domain.*;
import com.linln.modules.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class TopicMaterialRealize {

    @Autowired
    TopicMaterialService topicMaterialService;
    @Autowired
    ScaleService scaleService;
    @Autowired
    ContentTopicService contentTopicService;
    @Autowired
    MaterialService materialService;
    @Autowired
    ApprTopicService apprTopicService;
    @Autowired
    UserService userService;
    @Autowired
    SummaryService summaryService;
    @Autowired
    AppraisalService appraisalService;
    @Autowired
    QuesService quesService;
    @Autowired
    QuesStatisService quesStatisService;
    @Autowired
    GroupService groupService;

    public  boolean  saveTopicMaterial(String scaleId,String topicId,String no,List<String> content ,List<String> scores,String groupId,String serial,String info) throws Exception {  //保存题目选项（模板）
        topicMaterialService.deleteTopic(scaleId,topicId);
        if(content.size()>0  && scores.size()>0 && scores.size()==content.size()){
            for (int i=0;i<content.size();i++){
                TopicMaterial topicMaterial=new TopicMaterial();
                Long id= RandomUtil.getId();
                topicMaterial.setId(id+"");
                topicMaterial.setContent(content.get(i));
                topicMaterial.setScore(scores.get(i));
                topicMaterial.setAppraisalTopicId(topicId);
                topicMaterial.setNo(no);
                topicMaterial.setScaleId(scaleId);
                topicMaterial.setGroupId(groupId);
                topicMaterial.setInfo(info);
                topicMaterial.setSerial(serial+"");
                topicMaterialService.save(topicMaterial);
            }
            return true;
        }else {
            return false;
        }
    }


    /**
     * 保存用户答题题目(测评)
     * @param maps
     */
    public void saveTopic(List<Map<String,List<Map<String,Object>>>> maps, String  userId,String assId) throws Exception {
        String scaleId="";
        for (int i=0;i<maps.size();i++){
            Set<String> keys=maps.get(i).keySet();
            Iterator<String> iterator1=keys.iterator();
            while (iterator1.hasNext()){   //获取每一个题目
//                iterator1.next();
                List<Map<String,Object>> list= maps.get(i).get(iterator1.next());
                Map<String,Object> map=list.get(0);
                Scale scale=scaleService.getOne(map.get("scaleId").toString());
                scaleId=map.get("scaleId").toString();
                ContentTopic contentTopic=contentTopicService.getOne(map.get("tocId").toString());
                AppraisalTopic appraisalTopic= new AppraisalTopic();
                String id =  RandomUtil.getId().toString();
                appraisalTopic.setId(id);
                appraisalTopic.setNo(map.get("no").toString());
                appraisalTopic.setType(map.get("type").toString());
                appraisalTopic.setCreateDateTime(new Date());
                appraisalTopic.setCreateUser(userId);
                appraisalTopic.setModifyUser(userId);
                appraisalTopic.setModifyDateTime(new Date());
                appraisalTopic.setScaleId(map.get("scaleId").toString());
                appraisalTopic.setTitle(scale.getTitle());
                appraisalTopic.setDateil(contentTopic.getDateil());
                appraisalTopic.setWarnScope(contentTopic.getWarnScope());
                appraisalTopic.setUserId(userId);
                appraisalTopic.setGroupId(contentTopic.getGroupId());
                appraisalTopic.setSerial(contentTopic.getSerial());
                appraisalTopic.setInfo(contentTopic.getInfo());
              for(int j=0;j<list.size();j++){
                  Map<String,Object> map1=list.get(j); //每一个选项
                  TopicMaterial topicMaterial=topicMaterialService.getOne(map1.get("melId")+"");  //选项模板
                  AppraisalMaterial appraisalMaterial=new AppraisalMaterial();
                  String appId =  RandomUtil.getId().toString();
                  appraisalMaterial.setId(appId);
                  appraisalMaterial.setCreateDateTime(new Date());
                  appraisalMaterial.setCreateUser(userId);
                  appraisalMaterial.setModifyDateTime(new Date());
                  appraisalMaterial.setModifyUser(userId);
                  appraisalMaterial.setAppraisalTopicId(id);
                  appraisalMaterial.setNo(topicMaterial.getNo());
                  appraisalMaterial.setContent(topicMaterial.getContent());
                  appraisalMaterial.setScore(topicMaterial.getScore());
                  appraisalMaterial.setScaleId(topicMaterial.getScaleId());
                  appraisalMaterial.setUserId(userId);
                  appraisalMaterial.setResult(map1.get("result")+"");
                  appraisalMaterial.setGroupId(topicMaterial.getGroupId());
                  appraisalMaterial.setSerial(topicMaterial.getSerial());
                  appraisalMaterial.setInfo(topicMaterial.getInfo());
                  materialService.save(appraisalMaterial);
              }
                apprTopicService.save( appraisalTopic);
            }
        }
        correct(scaleId,userId,new Date(),userId,assId);
    }


    //计算用户评分

    /**
     *
     * @param scaleId  量表id
     * @param userId   测评用户id
     * @param submitTime  用户提交答题时间
     * @param adminId  操作者
     * @param assId 测评计划表id
     * @throws Exception
     */
    public void correct(String scaleId,String userId,Date submitTime,String adminId ,String assId) throws Exception {
        Scale scale=scaleService.getOne(scaleId);
        User user=userService.getById(Long.parseLong(userId));  //获取需要计算分数的用户信息
        List<AppraisalTopic>  appraisalTopics=apprTopicService.findByScaleIdAndUserId(scaleId,userId); //获取用户对应量表下的题目
        UserPmq userPmq=appraisalService.fetchScaleIdAndUserId(scaleId,userId,assId);  //获取用户测评记录表
        String upmqId="";
        if(userPmq!=null){  //判断测评记录表是否为空
            upmqId=userPmq.getId();   //测评记录表不为空获取测评记录表Id
        }else {
            upmqId= RandomUtil.getId().toString();   //测评记录表为空，创建id
        }
        double total=0;  //总分
        for (int i=0;i<appraisalTopics.size();i++){   //计算每一题的分数，和得到总分。
            AppraisalTopic appraisalTopic=appraisalTopics.get(i);
            List<String> scores=materialService.findByScaleIdAndTopicId(scaleId,appraisalTopic.getId());
           double score=0;
            for (int j=0;j<scores.size();j++){
                score=score+Double.parseDouble(scores.get(j));
            }
            appraisalTopic.setUpmqId(upmqId);
            appraisalTopic.setScore(score+"");
            total+=score;
            appraisalTopic.setModifyDateTime(new Date());
            apprTopicService.save(appraisalTopic);
        }
        List<Summary>  summaries=summaryService.findByScaleId(scaleId); //获取量表测评结论
        Summary summary=null;
        if(summaries.size()>0){             //判断用户测评后分数达到测评结论分数范围的结论
            for (Summary sum:summaries) {
                String a=sum.getGrade().substring(0,sum.getGrade().indexOf("-"));
                String aa=sum.getGrade().substring(sum.getGrade().indexOf("-" )+1);
                if(total>=Double.parseDouble(a) && total<=Double.parseDouble(aa)){
                    summary=sum;
                }
            }
        }

        if(userPmq==null){
            userPmq=new UserPmq();   //保存用户测评档案信息
            userPmq.setId(upmqId);
            userPmq.setUserId(user.getId().toString());
            userPmq.setCreateUser(adminId);
            userPmq.setUserName(user.getNickname());
            userPmq.setScaleId(scale.getId());
            userPmq.setScaleTitle(scale.getTitle());
            userPmq.setScaleAlias(scale.getAlias());
            userPmq.setOnlyNo(scale.getOnlyNo());
            userPmq.setScaleTypeId(scale.getScaleTypeId());
            userPmq.setScaleTypeName(scale.getScaleTypeName());
            userPmq.setSubmitTime(submitTime);
            userPmq.setAssesId(assId);
        }
        if(summary!=null){
            userPmq.setScoring(summary.getGrade());
            userPmq.setConsequence(summary.getContentSummary());
            userPmq.setSuggest(summary.getSuggest());
        }
        userPmq.setSumScore(total+"");
        userPmq.setModifyDateTime(new Date());
        userPmq.setModifyUser(adminId);
        userPmq.setCalculateTime(new Date());
        userPmq.setGetNumber(1);
        List<Group> groups = groupService.findByScaleId(userPmq.getScaleId());
        String groupScore="";
        for (Group group : groups) {
            if ("AVG".equals(group.getStatType())){
                Map<String, String> statByAVG = appraisalService.findStatByAVG(userPmq.getUserId(), userPmq.getScaleId(), group.getSerial());
                groupScore += statByAVG.get("info")+"_"+statByAVG.get("type")+","+statByAVG.get("score")+";";
            }
            if ("SUM".equals(group.getStatType())){
               Map<String, String> statByAVG = appraisalService.findStatBySUM(userPmq.getUserId(), userPmq.getScaleId(), group.getSerial());
                groupScore += statByAVG.get("info")+"_"+statByAVG.get("type")+","+statByAVG.get("score")+";";
            }
            if ("COUNT".equals(group.getStatType())){
                Map<String, String> statByAVG = appraisalService.findStatByCount(userPmq.getUserId(), userPmq.getScaleId(), group.getSerial());
                groupScore += statByAVG.get("info")+"_"+statByAVG.get("type")+","+statByAVG.get("score")+";";
            }
        }
        userPmq.setGroupScore(groupScore);
        appraisalService.save(userPmq);
    }

    /**
     * 保存用户问卷调查回答的题目
     * @param maps
     * @param user
     * @throws Exception
     */
    @Transactional
    public void saveQuesTopic(List< List<Map<String,Object>>> maps, User user) throws Exception {
        String quesId="";
        String userId=user.getId().toString();  //操作用户id
        String quId =  RandomUtil.getId().toString(); //问卷调查记录表id
        for (int i=0;i<maps.size();i++){
            Map<String,Object> map=maps.get(i).get(0);  //获取第一个问题下的第一个选项
            Questionnaire scale=quesService.getOne(map.get("scaleId").toString()); // 获取问卷调查对象
            quesId=map.get("scaleId").toString();  //得到问卷调查id
            ContentTopic contentTopic=contentTopicService.getOne(map.get("tocId").toString()); //获取题目模板对象
            String id =  RandomUtil.getId().toString();
            AppraisalTopic appraisalTopic= new AppraisalTopic(id,map.get("no").toString(),map.get("type").toString(),"2",
                    quesId,scale.getTitle(),contentTopic.getDateil(),"","",contentTopic.getWarnScope(),userId,quId);  //创建用户回答的题目对象
            appraisalTopic.setCreateDateTime(new Date());
            appraisalTopic.setCreateUser(userId);
            appraisalTopic.setModifyUser(userId);
            appraisalTopic.setModifyDateTime(new Date());
            for (int j=0;j<maps.get(i).size();j++){
                Map<String,Object> map1=maps.get(i).get(j); //每一个选项
                TopicMaterial topicMaterial=topicMaterialService.getOne(map1.get("melId")+"");  //选项模板对象
                String appId =  RandomUtil.getId().toString();
                AppraisalMaterial appraisalMaterial=new AppraisalMaterial(appId,id,topicMaterial.getNo(),
                        quesId,topicMaterial.getContent(),topicMaterial.getScore(),userId,map1.get("result")+"");  //创建选项对象
                appraisalMaterial.setCreateDateTime(new Date());
                appraisalMaterial.setCreateUser(userId);
                appraisalMaterial.setModifyDateTime(new Date());
                appraisalMaterial.setModifyUser(userId);
                materialService.save(appraisalMaterial);  //保存每一个选项
            }
            apprTopicService.save( appraisalTopic); //保存题目
        }
        QuesStatistics quesStatistics=new QuesStatistics(quId,userId,user.getNickname(),quesId); //添加用户答题记录
        quesStatistics.setCreateUser(userId);
        quesStatistics.setModifyUser(userId);
        quesStatisService.save(quesStatistics);

    }














    public static void aiXin(){
        for(float y = (float) 1.5;y>-1.5;y -=0.1)  {
            for(float x= (float) -1.5;x<1.5;x+= 0.05){
                float a = x*x+y*y-1;
                if((a*a*a-x*x*y*y*y)<=0.0)  {
                    System.out.print("^");
                }
                else
                    System.out.print(" ");
            }
            System.out.println();
        }
    }



    public  static  void  main(String [] a){
//        aiXin();
        IntStream.range(-15, 15).map(y -> -y).forEach(y -> IntStream.range(-30, 30).forEach(x ->
         System.out.print(
         Math.pow(Math.pow((x * 0.05), 2) + Math.pow((y * 0.1), 2) - 1, 3) -
         Math.pow(x * 0.05, 2) * Math.pow(y * 0.1, 3) <= 0 ? "love".charAt(Math.abs((y - x) % 4)) : " " + (x == 29 ? "\n" : "")
         )));
    }



}
