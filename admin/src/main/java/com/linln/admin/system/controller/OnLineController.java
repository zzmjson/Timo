package com.linln.admin.system.controller;
import com.linln.common.common.Result;
import com.linln.common.common.ResultUtil;
import com.linln.modules.system.domain.ONLineConsulting;
import com.linln.modules.system.service.OnLineConService;
//import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/online")
public class OnLineController {


    @Autowired
    private OnLineConService onLineConService;
    //查询用户列表
    @PostMapping(value = "fetchONLineBySearch")
    public Result fetchONLineBySearch(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      @RequestParam(value = "searchText", defaultValue = "") String searchText
            , HttpServletRequest req ) {

        PageRequest request = PageRequest.of(page, size);
        Page<ONLineConsulting> users = onLineConService.fetchONLineBySearch(searchText,request);


        return ResultUtil.successPage(users.getTotalElements(),users.getContent());
    }








}
