package com.linln.modules.system.service.impl;

import com.linln.common.enums.ResultEnum;
import com.linln.common.enums.StatusEnum;
import com.linln.common.exception.ResultException;
import com.linln.modules.system.domain.Dept;
import com.linln.modules.system.domain.Group;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.domain.User;
import com.linln.modules.system.repository.DeptRepository;
import com.linln.modules.system.repository.GroupRepository;
import com.linln.modules.system.repository.UserRepository;
import com.linln.modules.system.service.DeptService;
import com.linln.modules.system.service.GroupService;
import com.linln.modules.system.service.ScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;
import java.util.*;

/**
 * @author 小懒虫
 * @date 2018/12/02
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ScaleService scaleService;
    @Override
    public Page<Map<String,String>> findScaleAll(PageRequest request) {
        Page<Map<String,String>> list = groupRepository.findScaleAll(request);
        return list;
    }

    @Override
    public List<Group> findByScaleId(String scaleId) {
        return groupRepository.findByScaleId(scaleId);
    }

    @Override
    public Group fingById(String id) {
        return groupRepository.getOne(id);
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public List<String> findScaleId() {
        return groupRepository.findScaleId();
    }

    @Override
    public boolean haveGroup(String scaleId, String serial) {

        return groupRepository.findByScaleIdAndSerial(scaleId,serial)==null;
    }
}

