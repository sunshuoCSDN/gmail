package com.atguigu.gmall.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil {

    public static String uploadImage(MultipartFile multipartFile) {
        String imgUrl = "http://192.168.1.10";

        try {
            //配置fdfs的全局链接地址
            String path = PmsUploadUtil.class.getResource("/tracker.conf").getPath();
            ClientGlobal.init(path);
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            byte[] bytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            int i = originalFilename.lastIndexOf(".");
            String file_ext_name = originalFilename.substring(i + 1);
            String[] uploadFile = storageClient.upload_file(bytes, file_ext_name, null);
            for (String s : uploadFile) {
                imgUrl += "/" + s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
}
