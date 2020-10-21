package com.linln.common.utils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 文件上传工具类
 */
public class UploadUtil {

    public static final String[] VIDEOFORMAT = {"rmvb", "avi", "wmv"};
    public static final String[] PICTUREFORMAT = {"jpg", "jpeg", "png", "gif", "bmp"};

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
