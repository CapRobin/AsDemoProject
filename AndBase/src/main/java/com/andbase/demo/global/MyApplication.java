package com.andbase.demo.global;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.andbase.demo.R;
import com.andbase.library.global.AbConstant;
import com.andbase.library.util.AbSharedUtil;

public class MyApplication extends Application {

	public SharedPreferences sharedPreferences = null;

	/** 主题*/
	public int themeId = -1;

	@Override
	public void onCreate() {
		super.onCreate();
		sharedPreferences = AbSharedUtil.getDefaultSharedPreferences(this);
		initTheme();

	}

	public void initTheme(){

		themeId = AbSharedUtil.getInt(this, AbConstant.THEME_ID,-1);
		if(themeId==-1){
			themeId = R.style.AppTheme1;
			this.setTheme(themeId);
            Editor editor = sharedPreferences.edit();
            editor.putInt(AbConstant.THEME_ID, themeId);
            editor.commit();

		}
	}
	
	public void updateTheme(int themeId){
		this.themeId = themeId;
		Editor editor = sharedPreferences.edit();
		editor.remove(AbConstant.THEME_ID);
		editor.putInt(AbConstant.THEME_ID, this.themeId);
		editor.commit();
	}


	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
