package com.kf5sdk.exam.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

	
	private static final String IS_LOGIN = "is_login";
	
	private static final String APP_NAME = "app_name";
	
	private static SharedPreferences getPreference(Context context){
		
		return context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
		
	}
	
	public static void saveBoolLogin(Context context, boolean isLogin){
		
		SharedPreferences.Editor editor = getPreference(context).edit();
		editor.putBoolean(IS_LOGIN, isLogin);
		editor.commit();
	}
	
	public static boolean getBoolLogin(Context context){
		 
		return getPreference(context).getBoolean(IS_LOGIN, false);
		
	}
	
}
