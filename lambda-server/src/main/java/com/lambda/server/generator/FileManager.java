package com.lambda.server.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
public class FileManager {
	
	/**
	 * Method createDir.
	 * @param path
	 * 建立一个目录
	 */
	public static void createDir(String path)  {
		try {			
					
			File file = new File(path);	
			String filename = file.getName();
			if(filename.indexOf(".")>0){
				file = new File(path.substring(0,path.length()-filename.length()));
			}
			if (!file.exists())
			{
				if(!file.mkdirs()){
					createDir(file.getParent()+File.separator);					
				}
			}				
			
		} catch (Exception e) {
			e.printStackTrace();
			//throw e;
		}
	}
	
	public static boolean fileExists(String fileFullName){
		try {
			System.out.println(fileFullName);
			File file = new File(fileFullName);	
			return file.exists();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 写入到文件
	 * 
	 * @param body
	 * @param fileName
	 *           path+filename
	 * @throws IOException
	 */
	public static void writeFile(String body, String fileFullName, boolean overwrite) throws IOException {
		FileWriter writer = null;
		try {
			FileManager.createDir(fileFullName);
			if((!overwrite) && fileExists(fileFullName)){
				System.out.println("WARNING: File Exists! SKIPPED!");
				return;	//不覆盖
			}
			writer = new FileWriter(fileFullName);
			writer.write(body);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(writer!=null) writer.close();
		}
	}

	/**
	 * 读文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String filefullName) throws Exception {
		InputStreamReader  read = null;
		BufferedReader reader= null;
		try {
			File f = new File(filefullName);
			read = new InputStreamReader (new FileInputStream(f));
			reader=new BufferedReader(read);
			StringBuffer content = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line + "\n");
			}
			return content.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			reader.close();
			read.close();			
		}
	}


		

}
