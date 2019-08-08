package com.lambda.api;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lambda.api.fdfs.FastDFSClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiApplicationTests {

	@Autowired
	private FastDFSClient fdfsClient;
	
	@Test
	public void contextLoads() throws IOException {
		File file = new File("/Users/wkx/Desktop/IMG_1086.JPG");
		String res = fdfsClient.uploadFile(file);
		System.out.println(res);
		
		//fdfsClient.deleteFile("group1/M00/00/00/wKhj510qx4OAVIw-AAEor0QPotI728.JPG");
	}
}