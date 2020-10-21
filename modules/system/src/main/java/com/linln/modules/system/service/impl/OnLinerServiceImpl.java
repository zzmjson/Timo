package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.ONLineConsulting;
import com.linln.modules.system.repository.OnLineConRepository;
import com.linln.modules.system.service.OnLineConService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OnLinerServiceImpl implements OnLineConService {

    @Autowired
    OnLineConRepository onLineConRepository;

    @Override
    public Page<ONLineConsulting> fetchDataBySearch(String searchText, Pageable pageable) {
        return null;
    }

    @Override
    public List<ONLineConsulting> findAll() {
        return null;
    }

    @Override
    public ONLineConsulting fetchOne(String id) {
        return null;
    }

    @Override
    public void update(ONLineConsulting onLineConsulting) {

    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void save(ONLineConsulting onLineConsulting) {
        onLineConRepository.save(onLineConsulting);
    }

    @Override
    public Page<ONLineConsulting> fetchONLineBySearch(String searchText, Pageable request) {
        return onLineConRepository.fetchONLineBySearch(searchText,request);
    }
}
