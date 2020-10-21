package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.Questionnaire;
import com.linln.modules.system.repository.QuesRepository;
import com.linln.modules.system.service.QuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class QuesServiceImpl implements QuesService {


    @Autowired
    QuesRepository quesRepository;


    @Override
    public Questionnaire save(Questionnaire questionnaire) {
        return quesRepository.save(questionnaire);
    }

    @Override
    public Page<Questionnaire> fetchQuestionnaire(String searchText, Pageable req) {
        return quesRepository.fetchQuestionnaire(searchText,req);
    }

    @Override
    public List<List<Map<String, Object>>> getAllQues(String id) {
        List<List<Map<String, Object>>>   packaging=new ArrayList<>();
        List<Map<String,Object>>  getAll=quesRepository.getAllQues(id);
        String noAll="";
        for (int i=0;i<getAll.size();i++){
            String no= getAll.get(i).get("no").toString();
            List<Map<String,Object>>   aa=getAll.stream().filter(item->item.get("no").toString().equals(no)).collect(Collectors.toList());
            if(noAll.indexOf("["+no+"]")<0){
                noAll+="["+no+"]";
                packaging.add(aa);
            }
        }
        return packaging;
    }

    @Override
    public Questionnaire getOne(String id) {
        return quesRepository.getOne(id);
    }

    @Override
    public List<Map<String, String>> getQuesAll(String userId,String nickname,String deptName) {
        return quesRepository.getQuesAll( userId,nickname,deptName);
    }

    @Override
    public List findPerListById(String id) {
        String str = quesRepository.findPerListById(id);
        List list = new ArrayList();
        if (str!=null){
            String [] strs = str.split(",");
            for(int i = 0;i<strs.length;i++){
                Map<String,Object> maps = new HashMap<String,Object>();
                Map<String,String> map = new HashMap<String,String>();
                String [] poprt=strs[i].split(":");
                map.put("type",poprt[0]);
                String [] dept = poprt[1].split("/");
                String [] user = poprt[2].split("/");
                map.put("deptName",dept[0]);
                map.put("deptId",dept[1]);
                map.put("username",user[0]);
                map.put("nickname",user[1]);
                maps.put("map",map);
                list.add(maps);
            }
        }
        return list;
    }
}