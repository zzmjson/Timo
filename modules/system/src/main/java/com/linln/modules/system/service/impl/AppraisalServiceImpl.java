package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.User;
import com.linln.modules.system.domain.UserPmq;
import com.linln.modules.system.repository.AppraisalRepository;
import com.linln.modules.system.repository.ScaleRepository;
import com.linln.modules.system.service.AppraisalService;
import com.linln.modules.system.service.ScaleService;
import com.linln.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class AppraisalServiceImpl implements AppraisalService  {

    @Autowired
    AppraisalRepository appraisalRepository;
    @Autowired
    UserService userService;
    @Autowired
    ScaleService scaleService;
    @Override
    public Page<UserPmq> fetchAppraisalBySearch(String searchText, Pageable request) {
        return appraisalRepository.fetchAppraisalBySearch(searchText,request);
    }

    @Override
    public UserPmq save(UserPmq userPmq) {
        return appraisalRepository.save(userPmq);
    }

    @Override
    public UserPmq fetchOne(String id) {
        return appraisalRepository.getOne(id);
    }

    @Override
    public List<List<Map<String, Object>>> getAllSale(String scaleId,String superior,String assId) {

        List<List<Map<String, Object>>>   packaging=new ArrayList<>();
        List<Map<String,Object>>  getAll=appraisalRepository.getAllSale(scaleId,superior,assId);
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
    public List<List<Map<String, Object>>> getAllByscaleId(String saleId) {
        List<Map<String, Object>> list = appraisalRepository.getAllByscaleId(saleId);
//        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        List<String> alist = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            alist.add(list.get(i).get("no").toString()+list.get(i).get("content"));
        }
        Iterator it = alist.iterator();
        List<String> list1 = new ArrayList<>();
        while (it.hasNext()){
            String str = (String) it.next();
            if (!list1.contains(str)){
                list1.add(str);
            }
        }
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (int j=0;j<list1.size();j++){
            Map<String ,Object> hashmap = new HashMap<>();
            int temp = 0;
            for (int i=0;i<list.size();i++){
                if (list1.get(j).equals(list.get(i).get("no").toString()+list.get(i).get("content"))){

                    hashmap.put("no",list.get(i).get("no"));
                    hashmap.put("content",list.get(i).get("content"));
                    hashmap.put("dateil",list.get(i).get("dateil"));
                    if (list.get(i).get("result").equals("是")){
                        temp++;
                    }
                    hashmap.put("count",temp);
                    mapList.add(hashmap);
                }
            }
        }
//        Iterator its = mapList.iterator();
//        List<Map<String,Object>> mapLists = new ArrayList<>();
//        while (its.hasNext()){
//           Object map = (Object)its.hasNext();
//            if (!mapLists.contains(map)){
//                mapLists.add(map);
//            }
//        }
        for(int i =0;i<mapList.size();i++){
            for(int j=mapList.size()-1;j>i;j--){
                if(mapList.get(i).equals(mapList.get(j)))
                    mapList.remove(mapList.get(j));
            }
        }
        System.out.println(mapList.size()+"ma");
        List<List<Map<String, Object>>>   packaging=new ArrayList<>();
        String noAll="";
        for (int i=0;i<mapList.size();i++){
            String no= mapList.get(i).get("no").toString();
            List<Map<String,Object>>   aa=mapList.stream().filter(item->item.get("no").toString().equals(no)).collect(Collectors.toList());
            if(noAll.indexOf("["+no+"]")<0){
                noAll+="["+no+"]";
                packaging.add(aa);
            }
        }
        return packaging;
    }

    @Override
    public Map<String, String> findStatByCount(String userId, String scaleId, int serial) {
        return appraisalRepository.findStatByCount(userId,scaleId,serial);
    }

    @Override
    public Map<String, String> findStatByAVG(String userId, String scaleId, int serial) {
        return appraisalRepository.findStatByAVG(userId,scaleId,serial);
    }

    @Override
    public Map<String, String> findStatBySUM(String userId, String scaleId, int serial) {
        return appraisalRepository.findStatBySUM(userId,scaleId,serial);
    }

    @Override
    public UserPmq fetchScaleIdAndUserId(String scaleId, String userId,String assId) {
        return appraisalRepository.fetchScaleIdAndUserId(scaleId,userId,assId);
    }

    @Override
    public Map<String, Object> getUserPmqBys(String id) {
        return appraisalRepository.getUserPmqBy(id);
    }


    @Override
    public List<UserPmq> fetchUserPmqByUser(String userId) {
        return appraisalRepository.fetchUserPmqByUser(userId);
    }

    @Override
    public int findCountByUser(String userId) {
        return appraisalRepository.findCountByUser(userId);
    }

    @Override
    public List<Map<String,String>> findByAssId(String id) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<UserPmq> list = appraisalRepository.findByAssId(id);
        Set<String> set = new HashSet<>();
        for (UserPmq userPmq : list) {
            set.add(userPmq.getUserId());
        }
        List<Map<String,String>> mapList = new ArrayList<>();
        for (String s : set) {
            Map<String,String> map = new HashMap<>();
            map.put("username",s);
            User user = userService.getById(Long.parseLong(s));
            map.put("nickname",user.getNickname());
            List<Scale> scaleByAsseId = scaleService.findScaleByAsseId(id);
            int x = scaleByAsseId.size();
            List<UserPmq> byUserId = appraisalRepository.findByUserIdAndAssesId(user.getId().toString(),id);
            double y = byUserId.size();
            double temp = y/x;
            double z=temp*100;
            map.put("rate",(int)z+"%");
            Date date = new Date();

            for (UserPmq userPmq : byUserId) {
                Date submitTime = userPmq.getSubmitTime();
                date =submitTime;
                boolean before = date.before(submitTime);
                if (before){
                    date = submitTime;
                }
            }
            map.put("date",simpleDateFormat.format(date));
            map.put("userId",user.getId().toString());
            map.put("assesId",id);
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public Page<UserPmq> findPageByUserIdAndAssesId(String userId, String assesId, Pageable request) {
        return appraisalRepository.findPageByUserIdAndAssesId(userId,assesId,request);
    }



    public static void main(String[] args) {
        List<Map<String,String>> list = new ArrayList<Map<String, String>>();
        Map<String,String> map1 = new HashMap<>();
        map1.put("no","1");
        map1.put("content","小鸡炖蘑菇");
        map1.put("detail","清风拂杨柳");
        map1.put("result","是");
        Map<String,String> map2 = new HashMap<>();
        map2.put("no","1");
        map2.put("content","敌不动,我不动");
        map2.put("detail","清风拂杨柳");
        map2.put("result","是");
        list.add(map1);
        list.add(map2);
        Map<String,String> map3 = new HashMap<>();
        map3.put("no","1");
        map3.put("content","先天下之忧而忧");
        map3.put("detail","清风拂杨柳");
        map3.put("result","否");
        list.add(map3);
        Map<String,String> map4 = new HashMap<>();
        map4.put("no","1");
        map4.put("content","小鸡炖蘑菇");
        map4.put("detail","清风拂杨柳");
        map4.put("result","否");
        list.add(map4);
        Map<String,String> map5 = new HashMap<>();
        map5.put("no","1");
        map5.put("content","敌不动,我不动");
        map5.put("detail","清风拂杨柳");
        map5.put("result","否");
        list.add(map5);
        Map<String,String> map6 = new HashMap<>();
        map6.put("no","1");
        map6.put("content","先天下之忧而忧");
        map6.put("detail","清风拂杨柳");
        map6.put("result","否");
        list.add(map6);
        Map<String,String> map7 = new HashMap<>();
        map7.put("no","2");
        map7.put("content","真的不能添加吗");
        map7.put("detail","无法添加题目？");
        map7.put("result","是");
        list.add(map7);
        Map<String,String> map8 = new HashMap<>();
        map8.put("no","2");
        map8.put("content","我看看否");
        map8.put("detail","无法添加题目？");
        map8.put("result","否");
        list.add(map8);
        Map<String,String> map9 = new HashMap<>();
        map9.put("no","2");
        map9.put("content","真的不能添加吗");
        map9.put("detail","无法添加题目？");
        map9.put("result","是");
        list.add(map9);
        Map<String,String> map10 = new HashMap<>();
        map10.put("no","2");
        map10.put("content","我看看否");
        map10.put("detail","无法添加题目？");
        map10.put("result","否");
        list.add(map10);
        List<String> alist = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            alist.add(list.get(i).get("no")+list.get(i).get("content"));
        }
        Iterator it = alist.iterator();
        while (it.hasNext()){
            String str = (String) it.next();
            if (!list1.contains(str)){
                list1.add(str);
            }
        }
        System.out.println("list1:"+list1.size());
        for(int i =0;i<alist.size()-1;i++){
            for(int j=alist.size()-1;j>i;j--){
                if(alist.get(i).equals(alist.get(j)))
                    alist.remove(alist.get(j));
            }
        }
        System.out.println(alist.size());
//        for (String s : alist) {
//            System.out.println(s);
//        }
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (int j=0;j<alist.size();j++){
            Map<String ,Object> hashmap = new HashMap<>();
            int temp = 0;
            for (int i=0;i<list.size();i++){
                if (alist.get(j).equals(list.get(i).get("no")+list.get(i).get("content"))){

                    hashmap.put("no",list.get(i).get("no"));
                    hashmap.put("content",list.get(i).get("content"));
                    hashmap.put("detail",list.get(i).get("detail"));
                    if (list.get(i).get("result").equals("是")){
                        temp++;
                    }
                    hashmap.put("countOk",temp);
                    mapList.add(hashmap);
                }
            }
        }
        for(int i =0;i<mapList.size();i++){
            for(int j=mapList.size()-1;j>i;j--){
                if(mapList.get(i).equals(mapList.get(j)))
                    mapList.remove(mapList.get(j));
            }
        }
        for (Map<String, Object> map : mapList) {
//            System.out.println(map.get("no")+","+map.get("detail")+","+map.get("content")+","+map.get("countOk"));
            System.out.println(map);
        }
    }
}