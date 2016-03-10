package com.ifit.app.admin;

import com.ifit.app.R;
import com.ifit.app.other.BackupAndRestore;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Backup_and_restore extends Activity {
	
	private Button backup,restore;
	private BackupAndRestore mBackupAndRestore;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backup_and_restore);
		
		backup = (Button)findViewById(R.id.backup);
		restore = (Button)findViewById(R.id.restore);
		mBackupAndRestore = new BackupAndRestore(this);
		

		
		backup.setOnClickListener(new MyOnClickListen());
		restore.setOnClickListener(new MyOnClickListen());
		
	}

	class MyOnClickListen implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.backup:
				
				mBackupAndRestore.backupDB();
				
				break;
			case R.id.restore:
				
				mBackupAndRestore.restoreDB();
				
				break;
	
			}
		}
		
	}
	
}
