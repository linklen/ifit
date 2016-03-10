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
	 * 在android手机上将应用的数据库备份到sdcard
	 * 从sdcard中恢复数据库到应用 直接调用 restoreDB
	 * 进行数据库恢复调用backupDB进行数据库备份
	 * 恢复的时候会显示出可以恢复的数据库文件列表
	 */

	private Context mContext = null;
	private String[] fileList = null;//数据库文件列表
	private int choicePosition = -3;//选择数据库列表中的位置
	private AlertDialog dialog = null;
	private String BACK_FOLDER = "backup_db";
	private String appName = "ifit";
	
	public BackupAndRestore(Context context){
		mContext = context;
	}
	
	/*
	 * 恢复数据dialog
	 */
	public void restoreDB(){
		fileList = getFileList();
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("恢复");
		if(fileList != null){
		builder.setSingleChoiceItems(fileList, -1, new DialogClick());}
		else{
			builder.setMessage("当前无备份文件");
		}
		builder.setNegativeButton("确定", new DialogClick());
		builder.setPositiveButton("取消", new DialogClick());
		builder.show();
	}
	
	/*
	 * 备份数据库
	 */
	public void backupDB(){
		showDialog("是否备份数据库",'B');
	}
	
	/*
	 * 显示一个Dialog
	 * 
	 * @param title
	 * 标题，必须引用资源ID，resource ID
	 * @param sign
	 * 根据表示调用方法I-恢复默认设置；D-恢复默认设置；H-选择主机
	 */
	private void showDialog(String title,char sign){
		final char s = sign;
		new AlertDialog.Builder(mContext).setTitle(title)
		.setNegativeButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialogI, int which) {
				// TODO Auto-generated method stub
				switch(s){
				case 'B'://备份数据库
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
		}).setPositiveButton("取消", null).show();
	}
	
	
	/*
	 * 备份操作
	 */
	private boolean backUp(){
		boolean isOk = false;
		String sp = File.separator;//windows是\，unix是/
		File sdFile = sdCardOk();//获取sd路径，文件夹路径
		if(sdFile != null){
			try{
				String[] dbNames = {"DataBase.db"};
				
				
				for(int i=0;i<dbNames.length;i++){
					String dbName = dbNames[i];
					File dbFile = null;
					dbFile = dbOk(dbName);//获得数据库文件
					//Log.d("xxx", dbFile.toString());
					if(dbFile != null){
						
						//创建日期文件夹
						String folder_data = dataPrefix();//文件夹名是当前时间
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
	 * 获得时间前缀
	 */
	private String dataPrefix(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date(System.currentTimeMillis());
		String str = format.format(date);
		return str;
	}
	
	/*
	 * 文件夹列表
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
	 * sdCard是否存在 备份文件夹是否存在
	 * 
	 */
	private File sdCardOk(){
		File bkFile = null;
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)){ //判断路劲是否相同，是否有读取权限
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
			Toast.makeText(mContext, "Sdcard 不存在", Toast.LENGTH_SHORT).show();
		}
		return bkFile;
	}
	
	
	/*
	 * 恢复数据库
	 * 
	 * @param name
	 * 选择的文件名称 选中的数据库名称
	 * @param resoreDbName
	 * 需要恢复的数据库名称
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
		//获取data根目录 File file=Environment.getDataDirectory();
		String pakName = mContext.getPackageName();//data根目录下面对应软件文件夹
		String dbPath = absPath + sp + "data" + sp +pakName + sp +
				"databases" + sp + dbName;//data/data/软件文件夹
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
	 * 数据库文件是否存在，并可以使用
	 */
	private File dbOk(String dbName){
		String sp= File.separator;
		String absPath = Environment.getDataDirectory().getAbsolutePath(); 
		//获取data根目录 File file=Environment.getDataDirectory();
		String pakName = mContext.getPackageName();//data根目录下面对应软件文件夹
		String dbPath = absPath + sp + "data" + sp +pakName + sp +
				"databases" + sp + dbName;//data/data/软件文件夹
		File file = new File(dbPath);
		
		if(file.exists()){
			//Log.d("xxx", file.toString());
			return file;   //返回数据库文件
		}else{
			//Log.d("xxx", "xx");
			return null;
		}
	}
	
	/*
	 * 等候动画
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
	 * 写入
	 * @param inFile
	 * 读取
	 * @throws FileNotFoundException
	 */
	 private boolean fileCopy(File outFile, File inFile) throws IOException {
		 if (outFile == null || inFile == null) {
			 return false;
		 }
		 boolean isOk = true;
		 FileChannel inChannel = new FileInputStream(inFile).getChannel();// 只读
		 FileChannel outChannel = new FileOutputStream(outFile).getChannel();// 只写
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
	            if (which == -2) {// 确定
	                if (choicePosition < 0) {
	                	
	                    Toast.makeText(mContext, "没有选择数据库", Toast.LENGTH_SHORT)
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
	                            String fail_msg = "恢复失败" + ":" + f.getName();
	                            Toast.makeText(mContext, fail_msg,
	                                    Toast.LENGTH_SHORT).show();
	                            return;
	                        }
	                    }
	                    if (isOk) {
	                    	Toast.makeText(mContext, "恢复成功",
                                    Toast.LENGTH_SHORT).show();
	                    }
	                }
	            } else if (which == -1) {// 取消
	            } else if (which >= 0) {
	                choicePosition = which;
	            }
	        }
	    }
	   


	 	/*
	     * 执行任务
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
	                	Toast.makeText(mContext, "备份成功！", Toast.LENGTH_SHORT).show();
	                }else{
	                	Toast.makeText(mContext, "备份失败，请检查数据库是否存在！", Toast.LENGTH_SHORT).show();
	                }
	                
	            }
	        }
	    }
	
}

