package com.golding.tvbvotingsystem.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

//import org.apache.http.util.EncodingUtils;

import com.golding.tvbvotingsystem.contant.Contant;

import android.util.Log;

public class FileUtils {

	// 将字符串写入到文本文件中
	public boolean writeTxtToFile(String strcontent, String filePath, String fileName) {
	    //生成文件夹之后，再生成文件，不然会出错
	    makeFilePath(filePath, fileName);
	     
	    String strFilePath = filePath+fileName;
	    // 每次写入时，都换行写
	   // String strContent = strcontent + "\r\n";
	    try {
	        File file = new File(strFilePath);
	        if (!file.exists()) {
	            Log.d("TestFile", "Create the file:" + strFilePath);
	            file.getParentFile().mkdirs();
	            file.createNewFile();
	        }
	        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
	        FileChannel fc = raf.getChannel();
	       //将文件大小截为0
	        fc.truncate(0);
	        raf.write(strcontent.getBytes());
	        raf.close();
	        Contant.fILE_SEATNUM = strcontent;
	    } catch (Exception e) {
	        Log.e("TestFile", "Error on write File:" + e);
			return false;
	    }
	   
	    return true;
	}
	 
	// 生成文件
	public void makeFilePath(String filePath, String fileName) {
	    File file ;
	    makeRootDirectory(filePath);
	    try {
	        file = new File(filePath + fileName);
	        if (!file.exists()) {
	            file.createNewFile();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	 
	// 生成文件夹
	public  void makeRootDirectory(String filePath) {
	    File file ;
	    try {
	        file = new File(filePath);
	        if (!file.exists()) {
	            file.mkdir();
	        }
	    } catch (Exception e) {
	        Log.i("error:", e+"");
	    }
	}
	
	
	
	public String readFileSdcard(String fileName) {  
        String res = "";  
        try {  
            FileInputStream fin = new FileInputStream(fileName);  
            int length = fin.available();  
            byte[] buffer = new byte[length];  
            fin.read(buffer);  
            //res = EncodingUtils.getString(buffer, "UTF-8");
			res = new String(buffer, "UTF-8");				
            fin.close();  
        }  
  
        catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return res;  
  
    }  
}
