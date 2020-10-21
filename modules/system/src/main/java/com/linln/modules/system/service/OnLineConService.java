package com.linln.modules.system.service;

import com.linln.modules.system.domain.ONLineConsulting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface OnLineConService extends BaseService<ONLineConsulting>{

    Page<ONLineConsulting> fetchONLineBySearch(String searchText, Pageable request);


}
