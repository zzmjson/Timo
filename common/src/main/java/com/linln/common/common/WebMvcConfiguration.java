//package com.linln.common.common;
//
//import com.billiards.util.ParamsUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * Created by HSY on 2018/10/12.
// */
//@Configuration
//public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
//    @Autowired
//    ParamsUtil paramsUtil;
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/uploadFile/**").addResourceLocations("file:"+paramsUtil.getUploadImg());
//        super.addResourceHandlers(registry);
//    }
//}
