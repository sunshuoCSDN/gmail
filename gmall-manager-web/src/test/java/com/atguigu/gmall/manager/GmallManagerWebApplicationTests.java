package com.atguigu.gmall.manager;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class GmallManagerWebApplicationTests {

    @Test
    public void contextLoads() throws IOException, MyException {
        //配置fdfs的全局链接地址
        String path = this.getClass().getResource("/tracker.conf").getPath();
        ClientGlobal.init(path);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        String orginalFilename = "E:/b.webp";
        String[] uploadFile = storageClient.upload_file(orginalFilename, "webp", null);
        String url = "http://192.168.1.10";
        for (String s : uploadFile) {
            url += "/" + s;
        }
        System.out.println(url);
    }

}
