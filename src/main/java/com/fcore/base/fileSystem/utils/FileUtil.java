package com.fcore.base.fileSystem.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.aspose.cells.Workbook;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

public class FileUtil {

	/**
	 * 上传文件 
	 * @param suffix
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String uploudFile(String filePath, String suffix, HttpServletRequest request)throws Exception {
		// 上传文件保存路径
		String savePath = filePath;
		File file = new File(savePath);
		// 获取文件对象
		if (!file.exists()) {
			// 创建文件夹
			file.mkdirs();
		}
		String fileName = UUID.randomUUID().toString() + suffix;
		savePath = savePath + fileName;
		file = new File(savePath);
		BufferedInputStream fileIn;
		try {
			fileIn = new BufferedInputStream(request.getInputStream());
			BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(file));
			byte[] buf = new byte[1024];
			Long size = 0l;
			while (true) {
				// 读取数据
				int bytesIn = fileIn.read(buf, 0, 1024);
				if (bytesIn == -1) {
					break;
				} else {
					size += bytesIn;
					fileOut.write(buf, 0, bytesIn);
				}
			}
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  savePath;
	}
	
	/**
	 * 文件单位大小换算
	 * @param fileLength
	 * @return 包含单位的字符串
	 */
	public String conversion(Long fileLength){
		String str = "";
		if(fileLength>1024){
			str = fileLength%1024 +"B";
			fileLength = fileLength/1024;
			if(fileLength>1024){
				str = fileLength%1024+"KB" + str;
				fileLength = fileLength/1024;
				if(fileLength>1024){
					return fileLength/1024 + fileLength%1024 +"MB" +str;
				}else {
					return fileLength+"MB" +str;
				}
			}else {
				return fileLength+"KB" + str;
			}
		}else {
			return fileLength + "B";
		}
	}

	public static String getFileType(String fileType) {
		String str = "no type";
		if(StringUtils.isNotEmpty(fileType)){
			if(fileType.contains("word")){
				str = "word";
			}
			if(fileType.contains("video")){
				str = "video";
			}
			if(fileType.contains("image")){
				str = "image";
			}
			if(fileType.contains("excel") || fileType.contains("sheet")){
				str = "excel";
			}
			if(fileType.contains("pdf")){
				str = "pdf";
			}
			if(fileType.contains("ppt")){
				str = "ppt";
			}
			if(fileType.contains("text")){
				str = "txt";
			}
		}
		return str;
	}
	
	/**
	 * 创建目录
	 * @param path
	 */
	public static boolean createDir(String path){
		File file = new File(path);
		if(!file .exists()  && !file .isDirectory()) {
			return file.mkdirs();
		}
		return true;
	}
	
	/**
	 * 修改文件夹名称
	 * @param path
	 * @param newPath
	 * @return
	 */
	public static boolean renameDir(String path,String newPath) {
		File srcDir = new File(path);  
        return srcDir.renameTo(new File(newPath));  
	}
	
	/**
	 * 删除文件或文件夹
	 * @param path
	 */
	public static void deleteAllFilesOfDir(File path) {  
	    if (!path.exists())  
	        return;  
	    if (path.isFile()) {  
	        path.delete();  
	        return;  
	    }  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        deleteAllFilesOfDir(files[i]);  
	    }  
	    path.delete();  
	} 
	
	private static boolean getWordLicense(){
		boolean result = false;
		try {
			 ClassLoader loader = Thread.currentThread().getContextClassLoader();
			 InputStream license = new FileInputStream(loader.getResource("license.xml").getPath());// 凭证文件
			 License aposeLic = new License();
             aposeLic.setLicense(license);
             result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private boolean getCellsLicense(){
		boolean result = false;
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream license = new FileInputStream(loader.getResource("license.xml").getPath());// 凭证文件
			com.aspose.cells.License aposeLic = new com.aspose.cells.License();
			aposeLic.setLicense(license);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	private boolean getSlidesLicense(){
		boolean result = false;
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream license = new FileInputStream(loader.getResource("license.xml").getPath());// 凭证文件
			com.aspose.slides.License aposeLic = new com.aspose.slides.License();
			aposeLic.setLicense(license);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void slides2pdf(String originalPath,String dirPath){
		if (!getWordLicense()) {
			return;
		}
		try {
			long old = System.currentTimeMillis();
			FileInputStream fileInput = new FileInputStream(originalPath);
			File outputFile = new File(dirPath);
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			Presentation pres = new Presentation(fileInput);
			FileOutputStream fileOS = new FileOutputStream(outputFile);
			pres.save(fileOS, com.aspose.slides.SaveFormat.Pdf);
			long now = System.currentTimeMillis();
			System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + outputFile.getPath());
			fileOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void word2pdf(String originalPath,String dirPath){
		if (!getWordLicense()) {
			return;
		}
		try {
			long old = System.currentTimeMillis();
			FileInputStream fileInput = new FileInputStream(originalPath);
			Document doc = new Document(fileInput);
			File outputFile = new File(dirPath);
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			FileOutputStream fileOS = new FileOutputStream(outputFile);
			doc.save(fileOS, SaveFormat.PDF);
			long now = System.currentTimeMillis();
			System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + outputFile.getPath());
			fileOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}
	}
	
	public static void cells2pdf(String originalPath,String dirPath){
		if (!getWordLicense()) {
			return;
		}
		try {
			long old = System.currentTimeMillis();
			FileInputStream fileInput = new FileInputStream(originalPath);
			Workbook wb = new Workbook(fileInput);
			
			File outputFile = new File(dirPath);
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			FileOutputStream fileOS = new FileOutputStream(outputFile);
			wb.save(fileOS, com.aspose.cells.SaveFormat.PDF);
			long now = System.currentTimeMillis();
			System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + outputFile.getPath());
			fileOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
