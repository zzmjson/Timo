package com.linln.modules.system.service.impl;

import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import com.linln.modules.system.domain.ReserveInfo;
import com.linln.modules.system.domain.Role;
import com.linln.modules.system.repository.ReserveInfoRepository;
import com.linln.modules.system.repository.RoleRepository;
import com.linln.modules.system.service.ReserveInfoService;
import com.linln.modules.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class ReserveInfoServiceImpl implements ReserveInfoService {


    @Autowired
    private ReserveInfoRepository reserveInfoRepository;
    @Override
    public ReserveInfo save(ReserveInfo reserveInfo) {
        return reserveInfoRepository.save(reserveInfo);
    }

    @Override
    public ReserveInfo getOne(String id) {
        return reserveInfoRepository.getOne(id);
    }

    @Override
    public Page<ReserveInfo> findAllBySearch(String searchText, int state, Pageable req) {
        return reserveInfoRepository.findAllBySearch(searchText,state,req);
    }

    @Override
    public Page<ReserveInfo> getListByUserId(String userId, Pageable req) {
        return reserveInfoRepository.getListByUserId(userId, req);
    }

    @Override
    public int findCountByUser(String userId) {
        return reserveInfoRepository.findCountByUser(userId);
    }

    @Override
    public List<ReserveInfo> findreserByCounselorId(String id) {
        return reserveInfoRepository.findByCounselorId(id);
    }

    @Override
    public void updataState(int state, String id) {
        reserveInfoRepository.updataState(state,id);
    }

    @Override
    public List<ReserveInfo> findUserIdAndCounId(String userId, String counselorId) {
        return reserveInfoRepository.findUserIdAndCounId(userId,counselorId);
    }

    @Override
    public void updataIshave(String id) {
        reserveInfoRepository.updataIshave(id);
    }

    @Override
    public List<ReserveInfo> findReserAll() {
        return reserveInfoRepository.findReserAll();
    }

    @Override
    public Page<ReserveInfo> findByUserIdAndSearch(String searchText, int state, String id, Pageable req) {
        return reserveInfoRepository.findByUserIdAndSearch(searchText,state,id,req);
    }
}
