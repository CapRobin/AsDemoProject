package com.andbase.demo.db.inside;

import android.content.Context;

import com.andbase.demo.model.FuncMenu;
import com.andbase.demo.model.LocalUser;
import com.andbase.demo.model.Phone;
import com.andbase.demo.model.Stock;
import com.andbase.library.db.orm.helper.AbDBHelper;

/**
 * 
 * © 2012 amsoft.cn
 * 名称：DBInsideHelper.java 
 * 描述：手机data/data下面的数据库
 * @author 还如一梦中
 * @date：2013-7-31 下午3:50:18
 * @version v1.0
 */
public class DBInsideHelper extends AbDBHelper {

	private static DBInsideHelper dbHelper;

	// 数据库名
	private static final String DB_NAME = "andbase.db";
    
    // 当前数据库的版本
	private static final int DB_VERSION = 1;

	// 要初始化的表
	private static final Class<?>[] clazz = { LocalUser.class, Stock.class,Phone.class};

	public static DBInsideHelper getInstance(Context context) {
		if(dbHelper==null){
			dbHelper = new DBInsideHelper(context);
		}
		return dbHelper;
	}

	public DBInsideHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION, clazz);
	}

}



