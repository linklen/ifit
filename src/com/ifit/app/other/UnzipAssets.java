package com.ifit.app.other;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.util.Log;

public class UnzipAssets {

	
	//解压Assets中的zip文件
	//context 上下文对象
	//assetName 压缩包文件名
	//outputDirectory 输出目录
	
	public static void unZip(Context context,String assetName,
			String outputDirectory) throws IOException{
		//创建解压的目标目录
		File file = new File(outputDirectory);
		//如果目标目录不存在，则创建
		if(!file.exists()){
			file.mkdirs();
		}
		Log.d("xxx", "进入函数");
		InputStream inputStream = null;
		//打开压缩文件
		Log.d("xxx", "打开压缩包");
		//Log.d("xxx", context.getAssets().toString());
		inputStream = context.getAssets().open(assetName);
		Log.d("xxx", "准备输入流");
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		//读取一个进入点
		Log.d("xxx", "读取第一个进入点");
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		
		//使用1M buffer
		Log.d("xxx", "设置缓存");
		byte[] buffer = new byte[1024*1024];
		
		//解压是字节计数器
		int count = 0;
		Log.d("xxx", "准备进入遍历");
		//如果进入点为空说明已经遍历完所有压缩包总文件和目录
		int i = 0;
		while(zipEntry != null){
			//如果是一个目录
			
			//Log.d("xxx", "进行遍历"+(i++));
			if(zipEntry.isDirectory()){

				 //String name = zipEntry.getName();  
				 //name = name.substring(0, name.length() - 1);  
				 file = new File(outputDirectory + File.separator + zipEntry.getName());  
				 if(!file.exists()){
				 file.mkdir(); 
				 Log.d("xxx", "创建文件夹");}
			}else{
				//如果是文件
				file = new File(outputDirectory + File.separator
						+zipEntry.getName());
				if(!file.exists()){
				//创建该文件
				file.createNewFile();
				FileOutputStream fileOutStream = new FileOutputStream(file);
				while((count = zipInputStream.read(buffer))>0){
					fileOutStream.write(buffer,0,count);
				}
				Log.d("xxx", "创建文件");
				fileOutStream.close();}
			}
			//定位到下一个文件入口
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
	}
}
