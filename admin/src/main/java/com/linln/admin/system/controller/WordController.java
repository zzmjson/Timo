package com.linln.admin.system.controller;

import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.modules.system.domain.*;
import com.linln.modules.system.service.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/word")
public class WordController {

    @Autowired
    private QuesStatisService quesStatisService;
    @Autowired
    private AppraisalService appraisalService;
    @Autowired
    private QuesService quesService;
    @Autowired
    private UserService userService;
    @Autowired
    private CaseService caseService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private InterviewCoetentService interviewCoetentService;
    @Autowired
    private ReportContentService reportContentService;



    // word跨列合并单元格
    public  void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            if ( cellIndex == fromCell ) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    // word跨行并单元格
    public void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if ( rowIndex == fromRow ) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 导出个案信息到Word
     * @param response
     * @param id
     * @return
     * @throws IOException
     * @throws XmlException
     * @throws InvalidFormatException
     */
    @ResponseBody
    @GetMapping("/exportCase")
    public ResultVo exportCase(HttpServletResponse response, @RequestParam("id") String id)throws IOException, XmlException, InvalidFormatException{
        //获取个案信息
        Report report = reportService.getOne(id);
        //获取个人访谈
        List<Interview> interviews = interviewService.findByReportId(report.getId());
        //时间格式转换
        Date date = report.getCreateDateTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

//        QuesStatistics quesStatistics=quesStatisService.getOne(id);
//        User user = userService.getById(Long.parseLong(quesStatistics.getUserId()));
//        Questionnaire questionnaire=quesService.getOne(quesStatistics.getQuesId());
//        List<List<Map<String,Object>>>  getAllQues= appraisalService.getAllSale(quesStatistics.getQuesId(),"2",quesStatistics.getId());
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com=fsv.getHomeDirectory();
        String fileName=com.toPath()+"\\"+report.getVisitorName()+"的个案档案_"+System.currentTimeMillis()+".docx";
        System.out.println(fileName);
        XWPFDocument document= new XWPFDocument();
        File file1 = new File(fileName);
        FileOutputStream out = new FileOutputStream(file1);
        int index = 1;
        for(Interview interview :interviews){
            //添加标题
            XWPFParagraph titleParagraph = document.createParagraph();
            //设置段落居中
            titleParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleParagraphRun = titleParagraph.createRun();
            titleParagraphRun.setText(report.getVisitorName()+"的 访谈记录表(访谈"+index+")");
            titleParagraphRun.setColor("000000");
            titleParagraphRun.setFontSize(20);
            //生成表格
            XWPFTable infoTable = document.createTable(25,4);
            //列宽自动分割
            CTTblWidth infoTableWidth = infoTable.getCTTbl().addNewTblPr().addNewTblW();
            infoTableWidth.setType(STTblWidth.DXA);
            infoTableWidth.setW(BigInteger.valueOf(9072));
            //表格第一行
            XWPFTableRow infoTableRowOne = infoTable.getRow(0);
            infoTableRowOne.getCell(0).setText("个案基本信息");
            mergeCellsHorizontal(infoTable,0,0,3);
            //表格第二行
            XWPFTableRow infoTableRowTwo = infoTable.getRow(1);
            infoTableRowTwo.getCell(0).setText("来访者");
            infoTableRowTwo.getCell(1).setText(report.getVisitorName());
            infoTableRowTwo.getCell(2).setText("咨询类型");
            infoTableRowTwo.getCell(3).setText(report.getConsultingType());
            //表格第三行
            XWPFTableRow infoTableRowThree = infoTable.getRow(2);
            infoTableRowThree.getCell(0).setText("性别");
            infoTableRowThree.getCell(1).setText(report.getSex());
            infoTableRowThree.getCell(2).setText("个案来源");
            infoTableRowThree.getCell(3).setText(report.getCaseTitle());
            //表格第4行
            XWPFTableRow infoTableRowFour = infoTable.getRow(3);
            infoTableRowFour.getCell(0).setText("年龄");
            infoTableRowFour.getCell(1).setText(report.getAge()==null?"未知":report.getAge());
            infoTableRowFour.getCell(2).setText("处理级别");
            infoTableRowFour.getCell(3).setText(report.getRankContent());
            //表格第5行
            XWPFTableRow infoTableRowFive = infoTable.getRow(4);
            infoTableRowFive.getCell(0).setText("个案编号");
            infoTableRowFive.getCell(1).setText(report.getOnly());
            infoTableRowFive.getCell(2).setText("建案日期");
            String format = simpleDateFormat.format(date);
            infoTableRowFive.getCell(3).setText(format);
            //表格第6行
            XWPFTableRow infoTableRowSix = infoTable.getRow(5);
            infoTableRowSix.getCell(0).setText("当前状态");
            infoTableRowSix.getCell(1).setText(report.getState()=="1"?"未结案":"已结案");
            //跨列合并单元格
            mergeCellsHorizontal(infoTable,5,1,3);
            //表格第7行
            XWPFTableRow infoTableRowSeven = infoTable.getRow(6);
            infoTableRowSeven.getCell(0).setText("访谈基本信息");
            //跨列合并单元格
            mergeCellsHorizontal(infoTable,6,0,3);
            //表格第8行
            XWPFTableRow infoTableRowEight = infoTable.getRow(7);
            infoTableRowEight.getCell(0).setText("咨询师");
            infoTableRowEight.getCell(1).setText(interview.getConsultName());
            infoTableRowEight.getCell(2).setText("访谈时间");
            infoTableRowEight.getCell(3).setText(simpleDateFormat.format(interview.getDemanderTime()));
            //表格第9行
            XWPFTableRow infoTableRowNine = infoTable.getRow(8);
            infoTableRowNine.getCell(0).setText("咨询地点");
            infoTableRowNine.getCell(1).setText(interview.getDemanderSite());
            infoTableRowNine.getCell(2).setText("访谈方式");
            infoTableRowNine.getCell(3).setText(interview.getDemanderMode());
            //表格第10行
            XWPFTableRow infoTableRowTen = infoTable.getRow(9);
            infoTableRowTen.getCell(0).setText("访谈类容");
            //跨列合并单元格
            mergeCellsHorizontal(infoTable,9,0,3);
            //查询访谈类容
            List<InterviewContent> interviewContents = interviewCoetentService.findListByInterviewId(interview.getId());

            //表格第11行 基本信息
            XWPFTableRow infoTableRowElevn = infoTable.getRow(10);
            infoTableRowElevn.setHeight(720);
            infoTableRowElevn.getCell(0).setText("基本信息");
            //设置单元格列宽
            infoTableRowElevn.getCell(0).getCTTc().addNewTcPr().addNewTcW().setType(STTblWidth.DXA);
            infoTableRowElevn.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(360*5));
            //跨行合并
            mergeCellsVertically(infoTable,0,10,12);
            mergeCellsVertically(infoTable,1,10,12);
            //跨列合并
            mergeCellsHorizontal(infoTable,10,1,3);
            mergeCellsHorizontal(infoTable,11,1,3);
            mergeCellsHorizontal(infoTable,12,1,3);
            for (InterviewContent interviewContent : interviewContents){
                if ("基本信息".equals(interviewContent.getTypeTitle())){
                    infoTableRowElevn.getCell(1).setText(interviewContent.getContent());
                }
            }

            //表格第14行 基本信息
            XWPFTableRow infoTableRowFourteen = infoTable.getRow(13);
            infoTableRowFourteen.setHeight(720);
            infoTableRowFourteen.getCell(0).setText("个案自述");
            //设置单元格列宽
//            infoTableRowElevn.getCell(0).getCTTc().addNewTcPr().addNewTcW().setType(STTblWidth.DXA);
//            infoTableRowElevn.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(360*5));
            //跨行合并
            mergeCellsVertically(infoTable,0,13,15);
            mergeCellsVertically(infoTable,1,13,15);
            //跨列合并
            mergeCellsHorizontal(infoTable,13,1,3);
            mergeCellsHorizontal(infoTable,14,1,3);
            mergeCellsHorizontal(infoTable,15,1,3);
            for (InterviewContent interviewContent : interviewContents){
                if ("个案自述".equals(interviewContent.getTypeTitle())){
                    infoTableRowFourteen.getCell(1).setText(interviewContent.getContent());
                }
            }
            //表格第17行 基本信息
            XWPFTableRow infoTableRowSeventeen = infoTable.getRow(16);
            infoTableRowSeventeen.setHeight(720);
            infoTableRowSeventeen.getCell(0).setText("咨询过程");
            //设置单元格列宽
//            infoTableRowElevn.getCell(0).getCTTc().addNewTcPr().addNewTcW().setType(STTblWidth.DXA);
//            infoTableRowElevn.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(360*5));
            //跨行合并
            mergeCellsVertically(infoTable,0,16,18);
            mergeCellsVertically(infoTable,1,16,18);
            //跨列合并
            mergeCellsHorizontal(infoTable,16,1,3);
            mergeCellsHorizontal(infoTable,17,1,3);
            mergeCellsHorizontal(infoTable,18,1,3);
            for (InterviewContent interviewContent : interviewContents){
                if ("咨询过程".equals(interviewContent.getTypeTitle())){
                    infoTableRowSeventeen.getCell(1).setText(interviewContent.getContent());
                }
            }
            //表格第20行 基本信息
            XWPFTableRow infoTableRowTwenty = infoTable.getRow(19);
            infoTableRowTwenty.setHeight(720);
            infoTableRowTwenty.getCell(0).setText("咨询评估");
            //设置单元格列宽
//            infoTableRowElevn.getCell(0).getCTTc().addNewTcPr().addNewTcW().setType(STTblWidth.DXA);
//            infoTableRowElevn.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(360*5));
            //跨行合并
            mergeCellsVertically(infoTable,0,19,21);
            mergeCellsVertically(infoTable,1,19,21);
            //跨列合并
            mergeCellsHorizontal(infoTable,19,1,3);
            mergeCellsHorizontal(infoTable,20,1,3);
            mergeCellsHorizontal(infoTable,21,1,3);
            for (InterviewContent interviewContent : interviewContents){
                if ("咨询评估".equals(interviewContent.getTypeTitle())){
                    infoTableRowTwenty.getCell(1).setText(interviewContent.getContent());
                }
            }
            //表格第23行 基本信息
            XWPFTableRow infoTableRowTwentyTwo = infoTable.getRow(22);
            infoTableRowTwentyTwo.setHeight(720);
            infoTableRowTwentyTwo.getCell(0).setText("咨询师意见");
            //设置单元格列宽
//            infoTableRowElevn.getCell(0).getCTTc().addNewTcPr().addNewTcW().setType(STTblWidth.DXA);
//            infoTableRowElevn.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(360*5));
            //跨行合并
            mergeCellsVertically(infoTable,0,22,24);
            mergeCellsVertically(infoTable,1,22,24);
            //跨列合并
            mergeCellsHorizontal(infoTable,22,1,3);
            mergeCellsHorizontal(infoTable,23,1,3);
            mergeCellsHorizontal(infoTable,24,1,3);
            for (InterviewContent interviewContent : interviewContents){
                if ("咨询师意见".equals(interviewContent.getTypeTitle())){
                    infoTableRowTwentyTwo.getCell(1).setText(interviewContent.getContent());
                }
            }

            index++;
        }
        //判断是否结案
        if ("2".equals(report.getState())){

        //添加标题
        XWPFParagraph titleParagraph1 = document.createParagraph();
        //设置段落居中
        titleParagraph1.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleParagraphRun1 = titleParagraph1.createRun();
        titleParagraphRun1.setText(report.getVisitorName()+"的回访信息");
        titleParagraphRun1.setColor("000000");
        titleParagraphRun1.setFontSize(20);
        //生成回访信息表格
         XWPFTable infoTable1 = null;
         if (report.getReplyTime()==null){
             infoTable1= document.createTable(11,4);
         }else {
             infoTable1= document.createTable(15,4);
         }

        //列宽自动分割
        CTTblWidth infoTableWidth = infoTable1.getCTTbl().addNewTblPr().addNewTblW();
        infoTableWidth.setType(STTblWidth.DXA);
        infoTableWidth.setW(BigInteger.valueOf(9072));
        //表格第一行
        XWPFTableRow infoTableRowOne = infoTable1.getRow(0);
        infoTableRowOne.getCell(0).setText("个案基本信息");
        mergeCellsHorizontal(infoTable1,0,0,3);
        //表格第二行
        XWPFTableRow infoTableRowTwo = infoTable1.getRow(1);
        infoTableRowTwo.getCell(0).setText("来访者");
        infoTableRowTwo.getCell(1).setText(report.getVisitorName());
        infoTableRowTwo.getCell(2).setText("咨询类型");
        infoTableRowTwo.getCell(3).setText(report.getConsultingType());
        //表格第三行
        XWPFTableRow infoTableRowThree = infoTable1.getRow(2);
        infoTableRowThree.getCell(0).setText("性别");
        infoTableRowThree.getCell(1).setText(report.getSex());
        infoTableRowThree.getCell(2).setText("个案来源");
        infoTableRowThree.getCell(3).setText(report.getCaseTitle());
        //表格第4行
        XWPFTableRow infoTableRowFour = infoTable1.getRow(3);
        infoTableRowFour.getCell(0).setText("年龄");
        infoTableRowFour.getCell(1).setText(report.getAge()==null?"未知":report.getAge());
        infoTableRowFour.getCell(2).setText("处理级别");
        infoTableRowFour.getCell(3).setText(report.getRankContent());
        //表格第5行
        XWPFTableRow infoTableRowFive = infoTable1.getRow(4);
        infoTableRowFive.getCell(0).setText("个案编号");
        infoTableRowFive.getCell(1).setText(report.getOnly());
        infoTableRowFive.getCell(2).setText("建案日期");
        String format = simpleDateFormat.format(date);
        infoTableRowFive.getCell(3).setText(format);
        //表格第6行
        XWPFTableRow infoTableRowSix = infoTable1.getRow(5);
        infoTableRowSix.getCell(0).setText("当前状态");
        infoTableRowSix.getCell(1).setText(report.getState()=="1"?"未结案":"已结案");
        //跨列合并单元格
        mergeCellsHorizontal(infoTable1,5,1,3);
        //表格第7行
        XWPFTableRow infoTableRowSeven = infoTable1.getRow(6);
        infoTableRowSeven.getCell(0).setText("回访信息");
        //跨列合并单元格
        mergeCellsHorizontal(infoTable1,6,0,3);
        if (report.getReplyTime()==null) {
            //表格第8行
            XWPFTableRow infoTableRowEight = infoTable1.getRow(7);
            //跨行合并
            mergeCellsVertically(infoTable1, 0, 7, 10);
            //跨列合并
            mergeCellsHorizontal(infoTable1, 7, 0, 3);
            mergeCellsHorizontal(infoTable1, 8, 0, 3);
            mergeCellsHorizontal(infoTable1, 9, 0, 3);
            mergeCellsHorizontal(infoTable1, 10, 0, 3);
            infoTableRowEight.getCell(0).setText("暂无回访信息！");
        }else {
            //表格第8行
            XWPFTableRow infoTableRowEight = infoTable1.getRow(7);
            infoTableRowEight.getCell(0).setText("回访人");
            infoTableRowEight.getCell(1).setText(report.getReplyName());
            infoTableRowEight.getCell(2).setText("回访方式");
            infoTableRowEight.getCell(3).setText(report.getReplyType());
            //表格第9行
            XWPFTableRow infoTableRowNine = infoTable1.getRow(8);
            infoTableRowNine.getCell(0).setText("回访时间");
            infoTableRowNine.getCell(1).setText(simpleDateFormat.format(report.getReplyTime()));
            //跨列合并
            mergeCellsHorizontal(infoTable1, 8, 1, 3);
            //表格第10行
            XWPFTableRow infoTableRowTen = infoTable1.getRow(9);
            //设置单元格列宽
            infoTableRowTen.getCell(0).getCTTc().addNewTcPr().addNewTcW().setType(STTblWidth.DXA);
            infoTableRowTen.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(360*5));
            infoTableRowTen.getCell(0).setText("个案现状");

            //跨行合并
            mergeCellsVertically(infoTable1, 0, 9, 11);
            mergeCellsVertically(infoTable1, 1, 9, 11);
            //跨列合并
            mergeCellsHorizontal(infoTable1, 9, 1, 3);
            mergeCellsHorizontal(infoTable1, 10, 1, 3);
            mergeCellsHorizontal(infoTable1, 11, 1, 3);
            infoTableRowTen.getCell(1).setText(report.getReplyState());
            //表格第13行
            XWPFTableRow infoTableRowThreeteen = infoTable1.getRow(12);
            infoTableRowThreeteen.getCell(0).setText("个案备注");

            //跨行合并
            mergeCellsVertically(infoTable1, 0, 12, 14);
            mergeCellsVertically(infoTable1, 1, 12, 14);
            //跨列合并
            mergeCellsHorizontal(infoTable1, 12, 1, 3);
            mergeCellsHorizontal(infoTable1, 13, 1, 3);
            mergeCellsHorizontal(infoTable1, 14, 1, 3);
            infoTableRowThreeteen.getCell(1).setText(report.getNote());
        }

        //个案报告
            //获取个案内容
            List<ReportContent> reportContents = reportContentService.findByReportId(report.getId());
            //添加标题
            XWPFParagraph titleParagraph2 = document.createParagraph();
            //设置段落居中
            titleParagraph2.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleParagraphRun2 = titleParagraph2.createRun();
            titleParagraphRun2.setText(report.getVisitorName()+"的个案报告");
            titleParagraphRun2.setColor("000000");
            titleParagraphRun2.setFontSize(20);
            //生成回访信息表格
            XWPFTable infoTable2 = document.createTable(70,4);
            //列宽自动分割
            CTTblWidth infoTableWidth2 = infoTable2.getCTTbl().addNewTblPr().addNewTblW();
            infoTableWidth2.setType(STTblWidth.DXA);
            infoTableWidth2.setW(BigInteger.valueOf(9072));
            //表格第一行
            XWPFTableRow infoTableRowOne1 = infoTable2.getRow(0);
            infoTableRowOne1.getCell(0).setText("个案基本信息");
            mergeCellsHorizontal(infoTable2,0,0,3);
            //表格第二行
            XWPFTableRow infoTableRowTwo1 = infoTable2.getRow(1);
            infoTableRowTwo1.getCell(0).setText("来访者");
            infoTableRowTwo1.getCell(1).setText(report.getVisitorName());
            infoTableRowTwo1.getCell(2).setText("咨询类型");
            infoTableRowTwo1.getCell(3).setText(report.getConsultingType());
            //表格第三行
            XWPFTableRow infoTableRowThree1 = infoTable2.getRow(2);
            infoTableRowThree1.getCell(0).setText("性别");
            infoTableRowThree1.getCell(1).setText(report.getSex());
            infoTableRowThree1.getCell(2).setText("个案来源");
            infoTableRowThree1.getCell(3).setText(report.getCaseTitle());
            //表格第4行
            XWPFTableRow infoTableRowFour1 = infoTable2.getRow(3);
            infoTableRowFour1.getCell(0).setText("年龄");
            infoTableRowFour1.getCell(1).setText(report.getAge()==null?"未知":report.getAge());
            infoTableRowFour1.getCell(2).setText("处理级别");
            infoTableRowFour1.getCell(3).setText(report.getRankContent());
            //表格第5行
            XWPFTableRow infoTableRowFive1 = infoTable2.getRow(4);
            infoTableRowFive1.getCell(0).setText("个案编号");
            infoTableRowFive1.getCell(1).setText(report.getOnly());
            infoTableRowFive1.getCell(2).setText("建案日期");
            String format1 = simpleDateFormat.format(date);
            infoTableRowFive.getCell(3).setText(format1);
            //表格第6行
            XWPFTableRow infoTableRowSix1 = infoTable2.getRow(5);
            infoTableRowSix1.getCell(0).setText("当前状态");
            infoTableRowSix1.getCell(1).setText(report.getState()=="1"?"未结案":"已结案");
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,5,1,3);

            //表格第7行
            XWPFTableRow infoTableRowSeven1 = infoTable2.getRow(6);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,6,0,3);
            infoTableRowSeven1.getCell(0).setText("报告内容");
            //表格第8行
            XWPFTableRow infoTableRowEight1 = infoTable2.getRow(7);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,7,0,3);
            infoTableRowEight1.getCell(0).setText("个案信息");
            //表格第9行
            XWPFTableRow infoTableRowNine1 = infoTable2.getRow(8);
            infoTableRowNine1.getCell(0).setText("个案自述");
            //设置单元格列宽
            infoTableRowNine1.getCell(0).getCTTc().addNewTcPr().addNewTcW().setType(STTblWidth.DXA);
            infoTableRowNine1.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(360*5));
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 8, 10);
            mergeCellsVertically(infoTable2, 1, 8, 10);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,8,1,3);
            mergeCellsHorizontal(infoTable2,9,1,3);
            mergeCellsHorizontal(infoTable2,10,1,3);
            for (ReportContent reportContent :reportContents){
                if ("个案自述".equals(reportContent.getTinyTitle())){
                    infoTableRowNine1.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }

            //表格第12行
            XWPFTableRow infoTableRowElevn1 = infoTable2.getRow(11);
            infoTableRowElevn1.getCell(0).setText("来访原因");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 11, 13);
            mergeCellsVertically(infoTable2, 1, 11, 13);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,11,1,3);
            mergeCellsHorizontal(infoTable2,12,1,3);
            mergeCellsHorizontal(infoTable2,13,1,3);
            for (ReportContent reportContent :reportContents){
                if ("来访原因".equals(reportContent.getTinyTitle())){
                    infoTableRowElevn1.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第15行
            XWPFTableRow infoTableRowFive2 = infoTable2.getRow(14);
            infoTableRowFive2.getCell(0).setText("主要症状");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 14, 16);
            mergeCellsVertically(infoTable2, 1, 14, 16);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,14,1,3);
            mergeCellsHorizontal(infoTable2,15,1,3);
            mergeCellsHorizontal(infoTable2,16,1,3);
            for (ReportContent reportContent :reportContents){
                if ("主要症状".equals(reportContent.getTinyTitle())){
                    infoTableRowFive2.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第18行
            XWPFTableRow infoTableRowSix2 = infoTable2.getRow(17);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,17,0,3);
            infoTableRowSix2.getCell(0).setText("家庭关系、人际关系及个人成长经历");
            //表格第19行
            XWPFTableRow infoTableRowSeventeen2 = infoTable2.getRow(18);
            infoTableRowSeventeen2.getCell(0).setText("家庭关系");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 18, 20);
            mergeCellsVertically(infoTable2, 1, 18, 20);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,18,1,3);
            mergeCellsHorizontal(infoTable2,19,1,3);
            mergeCellsHorizontal(infoTable2,20,1,3);
            for (ReportContent reportContent :reportContents){
                if ("家庭关系".equals(reportContent.getTinyTitle())){
                    infoTableRowSeventeen2.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第22行
            XWPFTableRow infoTableRowTwentyTwo2 = infoTable2.getRow(21);
            infoTableRowTwentyTwo2.getCell(0).setText("人际关系");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 21, 23);
            mergeCellsVertically(infoTable2, 1, 21, 23);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,21,1,3);
            mergeCellsHorizontal(infoTable2,22,1,3);
            mergeCellsHorizontal(infoTable2,23,1,3);
            for (ReportContent reportContent :reportContents){
                if ("人际关系".equals(reportContent.getTinyTitle())){
                    infoTableRowTwentyTwo2.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第25行
            XWPFTableRow infoTableRowTwentyfive2 = infoTable2.getRow(24);
            infoTableRowTwentyfive2.getCell(0).setText("个人成长经历");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 24, 26);
            mergeCellsVertically(infoTable2, 1, 24, 26);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,24,1,3);
            mergeCellsHorizontal(infoTable2,25,1,3);
            mergeCellsHorizontal(infoTable2,26,1,3);
            for (ReportContent reportContent :reportContents){
                if ("个人成长经历".equals(reportContent.getTinyTitle())){
                    infoTableRowTwentyfive2.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第28行
            XWPFTableRow infoTableRowTwentySix2 = infoTable2.getRow(27);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,27,0,3);
            infoTableRowTwentySix2.getCell(0).setText("个人的情绪、个性特征、兴趣爱好、自我认识评价及常用的应对方式");
            //表格第29行
            XWPFTableRow infoTableRowTwentySeven2 = infoTable2.getRow(28);
            infoTableRowTwentySeven2.getCell(0).setText("个人的情绪");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 28, 30);
            mergeCellsVertically(infoTable2, 1, 28, 30);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,28,1,3);
            mergeCellsHorizontal(infoTable2,29,1,3);
            mergeCellsHorizontal(infoTable2,30,1,3);
            for (ReportContent reportContent :reportContents){
                if ("个人的情绪".equals(reportContent.getTinyTitle())){
                    infoTableRowTwentySeven2.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第32行
            XWPFTableRow infoTableRowThirty2 = infoTable2.getRow(31);
            infoTableRowThirty2.getCell(0).setText("个性特征");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 31, 33);
            mergeCellsVertically(infoTable2, 1, 31, 33);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,31,1,3);
            mergeCellsHorizontal(infoTable2,32,1,3);
            mergeCellsHorizontal(infoTable2,33,1,3);
            for (ReportContent reportContent :reportContents){
                if ("个性特征".equals(reportContent.getTinyTitle())){
                    infoTableRowThirty2.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第35行
            XWPFTableRow infoTableRowThirtythree = infoTable2.getRow(34);
            infoTableRowThirtythree.getCell(0).setText("兴趣爱好");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 34, 36);
            mergeCellsVertically(infoTable2, 1, 34, 36);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,34,1,3);
            mergeCellsHorizontal(infoTable2,35,1,3);
            mergeCellsHorizontal(infoTable2,36,1,3);
            for (ReportContent reportContent :reportContents){
                if ("兴趣爱好".equals(reportContent.getTinyTitle())){
                    infoTableRowThirtythree.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第38行
            XWPFTableRow infoTableRowThirtysix = infoTable2.getRow(37);
            infoTableRowThirtysix.getCell(0).setText("自我认识评价");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 37, 39);
            mergeCellsVertically(infoTable2, 1, 37, 39);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,37,1,3);
            mergeCellsHorizontal(infoTable2,38,1,3);
            mergeCellsHorizontal(infoTable2,39,1,3);
            for (ReportContent reportContent :reportContents){
                if ("自我认识评价".equals(reportContent.getTinyTitle())){
                    infoTableRowThirtysix.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第41行
            XWPFTableRow infoTableRowFourtyOne = infoTable2.getRow(40);
            infoTableRowFourtyOne.getCell(0).setText("常用应对方式");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 40, 42);
            mergeCellsVertically(infoTable2, 1, 40, 42);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,40,1,3);
            mergeCellsHorizontal(infoTable2,41,1,3);
            mergeCellsHorizontal(infoTable2,42,1,3);
            for (ReportContent reportContent :reportContents){
                if ("常用应对方式".equals(reportContent.getTinyTitle())){
                    infoTableRowFourtyOne.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第44行
            XWPFTableRow infoTableRowfourtyTwo = infoTable2.getRow(43);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,43,0,3);
            infoTableRowfourtyTwo.getCell(0).setText("既往病史、家族病史");
            //表格第45行
            XWPFTableRow infoTableRowFourtyfive = infoTable2.getRow(44);
            infoTableRowFourtyfive.getCell(0).setText("既往病史");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 44, 46);
            mergeCellsVertically(infoTable2, 1, 44, 46);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,44,1,3);
            mergeCellsHorizontal(infoTable2,45,1,3);
            mergeCellsHorizontal(infoTable2,46,1,3);
            for (ReportContent reportContent :reportContents){
                if ("既往病史".equals(reportContent.getTinyTitle())){
                    infoTableRowFourtyfive.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第48行
            XWPFTableRow infoTableRowFourtyeight = infoTable2.getRow(47);
            infoTableRowFourtyeight.getCell(0).setText("家族病史");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 47, 49);
            mergeCellsVertically(infoTable2, 1, 47, 49);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,47,1,3);
            mergeCellsHorizontal(infoTable2,48,1,3);
            mergeCellsHorizontal(infoTable2,49,1,3);
            for (ReportContent reportContent :reportContents){
                if ("家族病史".equals(reportContent.getTinyTitle())){
                    infoTableRowFourtyeight.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第51行
            XWPFTableRow infoTableRowfiveone = infoTable2.getRow(50);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,50,0,3);
            infoTableRowfiveone.getCell(0).setText("心理测试结果");
            //表格第52行
            XWPFTableRow infoTableRowfiivetwo = infoTable2.getRow(51);
            infoTableRowfiivetwo.getCell(0).setText("心理测试结果");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 51, 53);
            mergeCellsVertically(infoTable2, 1, 51, 53);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,51,1,3);
            mergeCellsHorizontal(infoTable2,52,1,3);
            mergeCellsHorizontal(infoTable2,53,1,3);
            for (ReportContent reportContent :reportContents){
                if ("心理测试结果".equals(reportContent.getTinyTitle())){
                    infoTableRowfiivetwo.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第55行
            XWPFTableRow infoTableRowfivefive = infoTable2.getRow(54);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,54,0,3);
            infoTableRowfivefive.getCell(0).setText("咨询师的一般印象");
            //表格第56行
            XWPFTableRow infoTableRowfiivesix = infoTable2.getRow(55);
            infoTableRowfiivesix.getCell(0).setText("咨询师的一般印象");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 55, 57);
            mergeCellsVertically(infoTable2, 1, 55, 57);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,55,1,3);
            mergeCellsHorizontal(infoTable2,56,1,3);
            mergeCellsHorizontal(infoTable2,57,1,3);
            for (ReportContent reportContent :reportContents){
                if ("咨询师的一般印象".equals(reportContent.getTinyTitle())){
                    infoTableRowfiivesix.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第59行
            XWPFTableRow infoTableRowfivenine = infoTable2.getRow(58);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,58,0,3);
            infoTableRowfivenine.getCell(0).setText("诊断评价与意见");
            //表格第60行
            XWPFTableRow infoTableRowsixty = infoTable2.getRow(59);
            infoTableRowsixty.getCell(0).setText("诊断评价与意见");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 59, 61);
            mergeCellsVertically(infoTable2, 1, 59, 61);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,59,1,3);
            mergeCellsHorizontal(infoTable2,60,1,3);
            mergeCellsHorizontal(infoTable2,61,1,3);
            for (ReportContent reportContent :reportContents){
                if ("诊断评价与意见".equals(reportContent.getTinyTitle())){
                    infoTableRowsixty.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第63行
            XWPFTableRow infoTableRowsixthree = infoTable2.getRow(62);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,62,0,3);
            infoTableRowsixthree.getCell(0).setText("咨询各阶段效果分析");
            //表格第64行
            XWPFTableRow infoTableRowsixtyfour = infoTable2.getRow(63);
            infoTableRowsixtyfour.getCell(0).setText("咨询各阶段效果分析");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 63, 65);
            mergeCellsVertically(infoTable2, 1, 63, 65);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,63,1,3);
            mergeCellsHorizontal(infoTable2,64,1,3);
            mergeCellsHorizontal(infoTable2,65,1,3);
            for (ReportContent reportContent :reportContents){
                if ("咨询各阶段效果分析".equals(reportContent.getTinyTitle())){
                    infoTableRowsixtyfour.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }
            //表格第67行
            XWPFTableRow infoTableRowsixseven = infoTable2.getRow(66);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,66,0,3);
            infoTableRowsixseven.getCell(0).setText("咨询师分析总结");
            //表格第68行
            XWPFTableRow infoTableRowsixtyeight = infoTable2.getRow(67);
            infoTableRowsixtyeight.getCell(0).setText("咨询师分析总结");
            //跨行合并
            mergeCellsVertically(infoTable2, 0, 67, 69);
            mergeCellsVertically(infoTable2, 1, 67, 69);
            //跨列合并单元格
            mergeCellsHorizontal(infoTable2,67,1,3);
            mergeCellsHorizontal(infoTable2,68,1,3);
            mergeCellsHorizontal(infoTable2,69,1,3);
            for (ReportContent reportContent :reportContents){
                if ("咨询师分析总结".equals(reportContent.getTinyTitle())){
                    infoTableRowsixtyeight.getCell(1).setText(reportContent.getContent());
                    break;
                }
            }



        }


        document.write(out);
        out.close();
        File file = new File(fileName);
        // 如果文件名存在，则进行下载
        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("Download the song successfully!");
            } catch (Exception e) {
                System.out.println("Download the song failed!");
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {//对应文件不存在
            try {
                //设置响应的数据类型是html文本，并且告知浏览器，使用UTF-8 来编码。
                response.setContentType("text/html;charset=UTF-8");

                //String这个类里面， getBytes()方法使用的码表，是UTF-8，  跟tomcat的默认码表没关系。 tomcat iso-8859-1
                String csn = Charset.defaultCharset().name();

                System.out.println("默认的String里面的getBytes方法使用的码表是： " + csn);

                //1. 指定浏览器看这份数据使用的码表
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                OutputStream os = response.getOutputStream();

                os.write("对应文件不存在".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
//                return R.error("-1","对应文件不存在");
        }
//        return ResultVoUtil.success("文件已成功下载到 D:\\");
        return null;
    }

    /**
     * 导出调查报告到Word
     * @param response
     * @param id
     * @return
     * @throws IOException
     * @throws XmlException
     * @throws InvalidFormatException
     */
    @ResponseBody
    @GetMapping("/exportSurvey")
    public ResultVo exportScoreList(HttpServletResponse response, @RequestParam("id") String id)throws IOException, XmlException, InvalidFormatException{
        QuesStatistics quesStatistics=quesStatisService.getOne(id);
        User user = userService.getById(Long.parseLong(quesStatistics.getUserId()));
        Questionnaire questionnaire=quesService.getOne(quesStatistics.getQuesId());
        List<List<Map<String,Object>>>  getAllQues= appraisalService.getAllSale(quesStatistics.getQuesId(),"2",quesStatistics.getId());
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com=fsv.getHomeDirectory();
        String fileName=com.getPath()+"\\问卷《"+questionnaire.getTitle()+"》"+quesStatistics.getUserName()+"的答题记录.docx";
        System.out.println(fileName);
        XWPFDocument document= new XWPFDocument();
        File file1 = new File(fileName);
        FileOutputStream out = new FileOutputStream(file1);

        //添加标题
        XWPFParagraph titleParagraph = document.createParagraph();
        //设置段落居中
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleParagraphRun = titleParagraph.createRun();

        titleParagraphRun.setText("问卷《"+questionnaire.getTitle()+"》"+quesStatistics.getUserName()+"的答题记录");
        titleParagraphRun.setColor("000000");
        titleParagraphRun.setFontSize(20);
        //段落
        XWPFParagraph firstParagraph = document.createParagraph();
        XWPFRun run = firstParagraph.createRun();
        run.setText("基本信息");
        run.setColor("000000");
        run.setFontSize(16);

        //设置段落背景颜色
//        CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
//        cTShd.setVal(STShd.CLEAR);
//        cTShd.setFill("97FFFF");

        //换行
        XWPFParagraph paragraph1 = document.createParagraph();
        XWPFRun paragraphRun1 = paragraph1.createRun();
        paragraphRun1.setText("\r");

        //基本信息表格
        XWPFTable infoTable = document.createTable();
        //去表格边框
//        infoTable.getCTTbl().getTblPr().unsetTblBorders();

        //列宽自动分割
        CTTblWidth infoTableWidth = infoTable.getCTTbl().addNewTblPr().addNewTblW();
        infoTableWidth.setType(STTblWidth.DXA);
        infoTableWidth.setW(BigInteger.valueOf(9072));

        //表格第一行
        XWPFTableRow infoTableRowOne = infoTable.getRow(0);
        infoTableRowOne.getCell(0).setText("姓名");
        infoTableRowOne.addNewTableCell().setText(user.getNickname());
        infoTableRowOne.addNewTableCell().setText("性别");
        infoTableRowOne.addNewTableCell().setText(user.getSex()=='1'?"男":"女");

        //表格第二行
        XWPFTableRow infoTableRowTwo = infoTable.createRow();
        infoTableRowTwo.getCell(0).setText("出生日期");
        infoTableRowTwo.getCell(1).setText(user.getBirthDate()==null?"未填写":user.getBirthDate().toString());
        infoTableRowTwo.getCell(2).setText("年龄");
        infoTableRowTwo.getCell(3).setText(user.getAge()==null?"未填写":user.getAge());

        //表格第三行
        XWPFTableRow infoTableRowThree = infoTable.createRow();
        infoTableRowThree.getCell(0).setText("账号");
        infoTableRowThree.getCell(1).setText(user.getPhone());
        infoTableRowThree.getCell(2).setText("编号");
        infoTableRowThree.getCell(3).setText(user.getSerialNo()==null?"未填写":user.getSerialNo());

        //表格第四行
        XWPFTableRow infoTableRowFour = infoTable.createRow();
        infoTableRowFour.getCell(0).setText("民族");
        infoTableRowFour.getCell(1).setText(user.getNation()==null?"未填写":user.getNation()==""?"未填写":user.getNation());
        infoTableRowFour.getCell(2).setText("学历");
        infoTableRowFour.getCell(3).setText(user.getEducation()==null?"未填写":user.getEducation()==""?"未填写":user.getEducation());

        //表格第五行
        XWPFTableRow infoTableRowFive = infoTable.createRow();
        infoTableRowFive.getCell(0).setText("家庭住址");
        infoTableRowFive.getCell(1).setText(user.getAddress()==null?"未填写":user.getAddress()==""?"未填写":user.getAddress());
        infoTableRowFive.getCell(2).setText("答卷日期");
        infoTableRowFive.getCell(3).setText(quesStatistics.getSubmitTime().toString());

        //换行
        XWPFParagraph paragraph2 = document.createParagraph();
        XWPFRun paragraphRun2 = paragraph2.createRun();
        paragraphRun2.setText("\r");

        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);

        XWPFParagraph pic = document.createParagraph();
        XWPFRun picRun = pic.createRun();
        picRun.setText("前言");
        picRun.setColor("000000");
        picRun.setFontSize(16);
        //换行
        picRun.addCarriageReturn();

        XWPFRun one = pic.createRun();
        one.setText(questionnaire.getMessage());
        one.setColor("000000");
        one.setFontSize(12);

        //换行
        one.addCarriageReturn();

        XWPFRun list = pic.createRun();
        list.setText("题目类容");
        list.setColor("000000");
        list.setFontSize(16);
        //换行
        list.addCarriageReturn();

        for (int i = 0;i<getAllQues.size();i++){
            XWPFRun ele = pic.createRun();
            ele.setText(getAllQues.get(i).get(0).get("no").toString()+". "+getAllQues.get(i).get(0).get("dateil"));
            ele.addCarriageReturn();
            ele.addCarriageReturn();
            for (int j=0;j<getAllQues.get(i).size();j++){
                XWPFRun answer = pic.createRun();
                answer.setText("  " +getAllQues.get(i).get(j).get("content").toString());
                if (getAllQues.get(i).get(j).get("result").equals("是")){
                    answer.setColor("DF0000");
                }
                answer.addCarriageReturn();
                answer.addCarriageReturn();
            }
        }
        document.write(out);
        out.close();
//        return ResultVoUtil.success("文件已成功下载到 D:\\");
        File file = new File(fileName);
        // 如果文件名存在，则进行下载
        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("Download the song successfully!");
            } catch (Exception e) {
                System.out.println("Download the song failed!");
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {//对应文件不存在
            try {
                //设置响应的数据类型是html文本，并且告知浏览器，使用UTF-8 来编码。
                response.setContentType("text/html;charset=UTF-8");

                //String这个类里面， getBytes()方法使用的码表，是UTF-8，  跟tomcat的默认码表没关系。 tomcat iso-8859-1
                String csn = Charset.defaultCharset().name();

                System.out.println("默认的String里面的getBytes方法使用的码表是： " + csn);

                //1. 指定浏览器看这份数据使用的码表
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                OutputStream os = response.getOutputStream();

                os.write("对应文件不存在".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
//                return R.error("-1","对应文件不存在");
        }
        return null;
    }


}
