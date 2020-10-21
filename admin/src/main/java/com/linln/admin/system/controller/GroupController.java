package com.linln.admin.system.controller;

import com.linln.common.utils.RandomUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.modules.system.domain.Group;
import com.linln.modules.system.domain.ReserveInfo;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.service.GroupService;
import com.linln.modules.system.service.ScaleService;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@RequestMapping("/system/group")
@RequiresPermissions("system:group:index")
@Controller
public class GroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private ScaleService scaleService;

    /**
     * 因子分组首页
     * @param page
     * @param size
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(@RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model){
        PageRequest req = PageRequest.of(page-1, size);
        Page<Map<String,String>> list = groupService.findScaleAll(req);
        model.addAttribute("list",list.getContent());
        model.addAttribute("page",list);
        return "/system/group/index";
    }

    /**
     * 因子分组详情页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id,Model model){
        List<Group> list = groupService.findByScaleId(id);
        model.addAttribute("list",list);
        return "/system/group/detail";
    }

    /**
     * 因子编辑页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model){
        Group group = groupService.fingById(id);
        model.addAttribute("item",group);
        return "/system/group/edit";
    }

    /**
     * 保存因子分组信息
     * @param group
     * @return
     */
    @ResponseBody
    @RequestMapping("/save")
    public ResultVo save(Group group){
        if (group.getId()==null){
            if (groupService.haveGroup(group.getScaleId(),group.getSerial()+"")){
                group.setId(RandomUtil.getRandom620(8));
                groupService.save(group);
            }else {
                return ResultVoUtil.error("序号已存在");
            }
        }else {
            Group group1 = groupService.fingById(group.getId());
            group1.setInfo(group.getInfo());
            group1.setIsStat(group.getIsStat());
            group1.setSerial(group.getSerial());
            group1.setStatType(group.getStatType());
            groupService.save(group1);
        }

        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 根据量表添加因子分组页面
     * @return
     */
    @RequestMapping("/addScale")
    public String addScale(Model model){
        List<String> scaleId = groupService.findScaleId();
        List<Scale> scaleList = scaleService.findByIsGroup(scaleId.toArray());
        model.addAttribute("scales",scaleList);
        return "/system/group/addScale";
    }

    /**
     * 保存根据量表添加的因子分组
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveByscale")
    public ResultVo saveByscale(@RequestParam("scaleId") String scaleId){
        Group group = new Group();
        group.setId(RandomUtil.getRandom620(8));
        group.setSerial(1);
        group.setScaleId(scaleId);
        group.setIsStat(0);
        group.setInfo("默认请修改");
        groupService.save(group);
        return  ResultVoUtil.SAVE_SUCCESS;
    }
    /**
     * 因子添加页面
     * @param scaleId
     * @param model
     * @returnadd
     */
    @RequestMapping("/add/{scaleId}")
    public String add(@PathVariable("scaleId") String scaleId, Model model){
        model.addAttribute("scaleId",scaleId);
        return "/system/group/add";
    }

}
