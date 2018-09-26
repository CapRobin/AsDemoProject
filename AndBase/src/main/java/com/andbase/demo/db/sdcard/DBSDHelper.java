package com.andbase.demo.db.sdcard;

import android.content.Context;

import com.andbase.demo.model.FuncMenu;
import com.andbase.demo.model.LocalUser;
import com.andbase.demo.model.Phone;
import com.andbase.demo.model.Stock;
import com.andbase.library.db.orm.helper.AbSDDBHelper;
import com.andbase.library.util.AbFileUtil;


public class DBSDHelper extends AbSDDBHelper {

	private static DBSDHelper dbHelper;

	// 数据库名
	private static final String DBNAME = "andbase.db";
    
    // 当前数据库的版本
	private static final int DBVERSION = 1;
	// 要初始化的表
	private static final Class<?>[] clazz = {LocalUser.class, Stock.class,Phone.class};

	public static DBSDHelper getInstance(Context context) {
		if(dbHelper==null){
			dbHelper = new DBSDHelper(context);
		}
		return dbHelper;
	}

	public DBSDHelper(Context context) {
		super(context, AbFileUtil.getDbDownloadDir(context), DBNAME, null, DBVERSION, clazz);
	}

}



