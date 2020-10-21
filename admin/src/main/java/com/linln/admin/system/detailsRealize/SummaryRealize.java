package com.linln.admin.system.detailsRealize;

import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.Summary;
import com.linln.modules.system.domain.User;
import com.linln.modules.system.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SummaryRealize {
    @Autowired
    SummaryService summaryService;


    public  void saveSummary(List<String> grades,List<String> contentSummarys,List<String> suggests,String scaleId,String userId){
        summaryService.deleteSummarys(scaleId);

        if(grades.size()>0 && contentSummarys.size()>0&& contentSummarys.size()==grades.size())
        {
            for (int i=0;i<grades.size();i++){
                String id= UUID.randomUUID().toString();
                Summary summary=new Summary();
                summary.setCreateUser(userId);
                summary.setModifyUser(userId);
                summary.setId(id);
                summary.setGrade(grades.get(i));
                summary.setContentSummary(contentSummarys.get(i));
                summary.setScaleId(scaleId);
                summary.setSuggest(suggests.get(i));
                summaryService.save(summary);
            }
        }
    }







}
