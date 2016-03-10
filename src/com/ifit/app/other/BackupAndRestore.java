package com.ifit.app.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BackupAndRestore {
	
	/*
	 * ��android�ֻ��Ͻ�Ӧ�õ����ݿⱸ�ݵ�sdcard
	 * ��sdcard�лָ����ݿ⵽Ӧ�� ֱ�ӵ��� restoreDB
	 * �������ݿ�ָ�����backupDB�������ݿⱸ��
	 * �ָ���ʱ�����ʾ�����Իָ������ݿ��ļ��б�
	 */

	private Context mContext = null;
	private String[] fileList = null;//���ݿ��ļ��б�
	private int choicePosition = -3;//ѡ�����ݿ��б��е�λ��
	private AlertDialog dialog = null;
	private String BACK_FOLDER = "backup_db";
	private String appName = "ifit";
	
	public BackupAndRestore(Context context){
		mContext = context;
	}
	
	/*
	 * �ָ�����dialog
	 */
	public void restoreDB(){
		fileList = getFileList();
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("�ָ�");
		if(fileList != null){
		builder.setSingleChoiceItems(fileList, -1, new DialogClick());}
		else{
			builder.setMessage("��ǰ�ޱ����ļ�");
		}
		builder.setNegativeButton("ȷ��", new DialogClick());
		builder.setPositiveButton("ȡ��", new DialogClick());
		builder.show();
	}
	
	/*
	 * �������ݿ�
	 */
	public void backupDB(){
		showDialog("�Ƿ񱸷����ݿ�",'B');
	}
	
	/*
	 * ��ʾһ��Dialog
	 * 
	 * @param title
	 * ���⣬����������ԴID��resource ID
	 * @param sign
	 * ���ݱ�ʾ���÷���I-�ָ�Ĭ�����ã�D-�ָ�Ĭ�����ã�H-ѡ������
	 */
	private void showDialog(String title,char sign){
		final char s = sign;
		new AlertDialog.Builder(mContext).setTitle(title)
		.setNegativeButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialogI, int which) {
				// TODO Auto-generated method stub
				switch(s){
				case 'B'://�������ݿ�
					if(dialog == null){
						dialog = awaitDialog(mContext);
					}else{
						dialog.show();
					}
					new ExecuteTask().execute('B');
					break;
				default:
					break;
				}
			}
		}).setPositiveButton("ȡ��", null).show();
	}
	
	
	/*
	 * ���ݲ���
	 */
	private boolean backUp(){
		boolean isOk = false;
		String sp = File.separator;//windows��\��unix��/
		File sdFile = sdCardOk();//��ȡsd·�����ļ���·��
		if(sdFile != null){
			try{
				String[] dbNames = {"DataBase.db"};
				
				
				for(int i=0;i<dbNames.length;i++){
					String dbName = dbNames[i];
					File dbFile = null;
					dbFile = dbOk(dbName);//������ݿ��ļ�
					//Log.d("xxx", dbFile.toString());
					if(dbFile != null){
						
						//���������ļ���
						String folder_data = dataPrefix();//�ļ������ǵ�ǰʱ��
						File f = new File(sdFile.getAbsolutePath()+sp+folder_data);
						if(!f.exists()){
							f.mkdirs();
						}
						
						File backFile = new File(f.getAbsolutePath()+sp
								+dbFile.getName());
						backFile.createNewFile();
						isOk = fileCopy(backFile,dbFile.getAbsoluteFile());
						if(!isOk){
							break;
						}
						
						return true;
					}else{
						return false;
					}
					
				}
			}catch(IOException e){
				e.printStackTrace();
				return false;
			}
		}
		return isOk;
		}
		
	/*
	 * ���ʱ��ǰ׺
	 */
	private String dataPrefix(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date(System.currentTimeMillis());
		String str = format.format(date);
		return str;
	}
	
	/*
	 * �ļ����б�
	 */
	private String[] getFileList(){
		String[] fileList = null;
		File file = sdCardOk();
		if(file != null){
			File[] list = file.listFiles();
			if(list != null && list.length>0){
				fileList = new String[list.length];
				for(int i =0;i<list.length;i++){
					fileList[i] = list[i].getName();
				}
			}
		}
		return fileList;
	}
	
	/*
	 * sdCard�Ƿ���� �����ļ����Ƿ����
	 * 
	 */
	private File sdCardOk(){
		File bkFile = null;
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)){ //�ж�·���Ƿ���ͬ���Ƿ��ж�ȡȨ��
			String sp = File.separator;
			String backUpPath = Environment.getExternalStorageDirectory()+sp
					+appName +sp +BACK_FOLDER;
			bkFile = new File(backUpPath);
			if(!bkFile.exists()){
				bkFile.mkdirs();
			}else{
				return bkFile;
			}
		}else{
			Toast.makeText(mContext, "Sdcard ������", Toast.LENGTH_SHORT).show();
		}
		return bkFile;
	}
	
	
	/*
	 * �ָ����ݿ�
	 * 
	 * @param name
	 * ѡ����ļ����� ѡ�е����ݿ�����
	 * @param resoreDbName
	 * ��Ҫ�ָ������ݿ�����
	 * 
	 */
	public boolean restore(String name,File f){
		boolean isOk = false;
		if(f!=null){
			File dbFile = dbOk(name);
			try{
				if(dbFile != null){
					isOk = fileCopy(dbFile,f.getAbsoluteFile());
				}else{
					dbFile = createdb(name);
					if(dbFile != null){
						isOk = fileCopy(dbFile,f.getAbsoluteFile());
						if(!isOk){
							dbFile.delete();
						}
					}else{
						isOk = false;
					}
					
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return isOk;
	}
	
	
	private File createdb(String dbName){
		String sp= File.separator;
		String absPath = Environment.getDataDirectory().getAbsolutePath(); 
		//��ȡdata��Ŀ¼ File file=Environment.getDataDirectory();
		String pakName = mContext.getPackageName();//data��Ŀ¼�����Ӧ����ļ���
		String dbPath = absPath + sp + "data" + sp +pakName + sp +
				"databases" + sp + dbName;//data/data/����ļ���
		File file = new File(dbPath);
		try {
			file.createNewFile();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	/*
	 * ���ݿ��ļ��Ƿ���ڣ�������ʹ��
	 */
	private File dbOk(String dbName){
		String sp= File.separator;
		String absPath = Environment.getDataDirectory().getAbsolutePath(); 
		//��ȡdata��Ŀ¼ File file=Environment.getDataDirectory();
		String pakName = mContext.getPackageName();//data��Ŀ¼�����Ӧ����ļ���
		String dbPath = absPath + sp + "data" + sp +pakName + sp +
				"databases" + sp + dbName;//data/data/����ļ���
		File file = new File(dbPath);
		
		if(file.exists()){
			//Log.d("xxx", file.toString());
			return file;   //�������ݿ��ļ�
		}else{
			//Log.d("xxx", "xx");
			return null;
		}
	}
	
	/*
	 * �Ⱥ򶯻�
	 */
	public AlertDialog awaitDialog(Context context){
		ProgressBar bar = new ProgressBar(context);
		bar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		dialog.show();
		Window window = dialog.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = 50;
		params.height = 50;
		window.setAttributes(params);
		window.setContentView(bar);
		return dialog;
	}
	
	/*
	 * @param outFile
	 * д��
	 * @param inFile
	 * ��ȡ
	 * @throws FileNotFoundException
	 */
	 private boolean fileCopy(File outFile, File inFile) throws IOException {
		 if (outFile == null || inFile == null) {
			 return false;
		 }
		 boolean isOk = true;
		 FileChannel inChannel = new FileInputStream(inFile).getChannel();// ֻ��
		 FileChannel outChannel = new FileOutputStream(outFile).getChannel();// ֻд
		 try {
	            long size = inChannel.transferTo(0, inChannel.size(), outChannel);
	            if (size <= 0) {
	                isOk = false;
	            }
	        } catch (IOException e) {
	            isOk = false;
	            e.printStackTrace();
	        } finally {
	            if (inChannel != null) {
	                inChannel.close();
	            }
	            if (outChannel != null) {
	                outChannel.close();
	            }
	        }
	        return isOk;
	    }
	 
	 
	 
	 private class DialogClick implements DialogInterface.OnClickListener {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            if (which == -2) {// ȷ��
	                if (choicePosition < 0) {
	                	
	                    Toast.makeText(mContext, "û��ѡ�����ݿ�", Toast.LENGTH_SHORT)
	                            .show();
	                    return;
	                }
	                String sp = File.separator;
	                String folderName = fileList[choicePosition];
	                String backUpPath = Environment.getExternalStorageDirectory()
	                        + sp + appName + sp + BACK_FOLDER + sp + folderName;
	                File file = new File(backUpPath);
	                if (file.isDirectory()) {
	                    File[] files = file.listFiles();
	                    boolean isOk = false;
	                    for (int i = 0; i < files.length; i++) {
	                        File f = files[i];
	                        isOk = restore(f.getName(), f);
	                        if (!isOk) {
	                            String fail_msg = "�ָ�ʧ��" + ":" + f.getName();
	                            Toast.makeText(mContext, fail_msg,
	                                    Toast.LENGTH_SHORT).show();
	                            return;
	                        }
	                    }
	                    if (isOk) {
	                    	Toast.makeText(mContext, "�ָ��ɹ�",
                                    Toast.LENGTH_SHORT).show();
	                    }
	                }
	            } else if (which == -1) {// ȡ��
	            } else if (which >= 0) {
	                choicePosition = which;
	            }
	        }
	    }
	   


	 	/*
	     * ִ������
	     */
	    private class ExecuteTask extends AsyncTask<Character, Void, Boolean> {
	    	
	    	private boolean successful=false;
	    	
	        @Override
	        protected Boolean doInBackground(Character... params) {
	            char c = params[0];
	            if (c == 'B') {
	            	successful =  backUp();
	            }
	            return null;
	        }
	   
	        @Override
	        protected void onPostExecute(Boolean result) {
	            super.onPostExecute(result);
	            if (dialog != null) {
	                dialog.dismiss();
	                
	                if(successful){
	                	Toast.makeText(mContext, "���ݳɹ���", Toast.LENGTH_SHORT).show();
	                }else{
	                	Toast.makeText(mContext, "����ʧ�ܣ��������ݿ��Ƿ���ڣ�", Toast.LENGTH_SHORT).show();
	                }
	                
	            }
	        }
	    }
	
}

