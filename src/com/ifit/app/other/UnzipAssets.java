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

	
	//��ѹAssets�е�zip�ļ�
	//context �����Ķ���
	//assetName ѹ�����ļ���
	//outputDirectory ���Ŀ¼
	
	public static void unZip(Context context,String assetName,
			String outputDirectory) throws IOException{
		//������ѹ��Ŀ��Ŀ¼
		File file = new File(outputDirectory);
		//���Ŀ��Ŀ¼�����ڣ��򴴽�
		if(!file.exists()){
			file.mkdirs();
		}
		Log.d("xxx", "���뺯��");
		InputStream inputStream = null;
		//��ѹ���ļ�
		Log.d("xxx", "��ѹ����");
		//Log.d("xxx", context.getAssets().toString());
		inputStream = context.getAssets().open(assetName);
		Log.d("xxx", "׼��������");
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		//��ȡһ�������
		Log.d("xxx", "��ȡ��һ�������");
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		
		//ʹ��1M buffer
		Log.d("xxx", "���û���");
		byte[] buffer = new byte[1024*1024];
		
		//��ѹ���ֽڼ�����
		int count = 0;
		Log.d("xxx", "׼���������");
		//��������Ϊ��˵���Ѿ�����������ѹ�������ļ���Ŀ¼
		int i = 0;
		while(zipEntry != null){
			//�����һ��Ŀ¼
			
			//Log.d("xxx", "���б���"+(i++));
			if(zipEntry.isDirectory()){

				 //String name = zipEntry.getName();  
				 //name = name.substring(0, name.length() - 1);  
				 file = new File(outputDirectory + File.separator + zipEntry.getName());  
				 if(!file.exists()){
				 file.mkdir(); 
				 Log.d("xxx", "�����ļ���");}
			}else{
				//������ļ�
				file = new File(outputDirectory + File.separator
						+zipEntry.getName());
				if(!file.exists()){
				//�������ļ�
				file.createNewFile();
				FileOutputStream fileOutStream = new FileOutputStream(file);
				while((count = zipInputStream.read(buffer))>0){
					fileOutStream.write(buffer,0,count);
				}
				Log.d("xxx", "�����ļ�");
				fileOutStream.close();}
			}
			//��λ����һ���ļ����
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
	}
}
