package com.leo.applock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceUtils {
	/**
	 * 手势密码状态
	 */
	public static String GESTURE_STATE = "GESTURE_STATE";
	/**
	 * 手势密码
	 */
	public static final String PARMA_GESTURE_PASSWORD = "GESTURE_PASSWORD";

	/**
	 * 取字符串
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getPrefString(Context context, String key, final String defaultValue) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

	/**
	 * 存储字符串
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setPrefString(Context context, final String key, final String value) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putString(key, value).commit();
	}

	/**
	 * 取boolean
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getPrefBoolean(Context context, final String key, final boolean defaultValue) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	/**
	 * 判断key是否存在
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean hasKey(Context context, final String key) {
		return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
	}

	/**
	 * 存boolean类型
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setPrefBoolean(Context context, final String key, final boolean value) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putBoolean(key, value).commit();
	}

	/**
	 * 存整型
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setPrefInt(Context context, final String key, final int value) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putInt(key, value).commit();
	}

	/**
	 * 取整型
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getPrefInt(Context context, final String key, final int defaultValue) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}

	/**
	 * 存float
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setPrefFloat(Context context, final String key, final float value) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putFloat(key, value).commit();
	}

	/**
	 * 取float
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */

	public static float getPrefFloat(Context context, final String key, final float defaultValue) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getFloat(key, defaultValue);
	}

	/**
	 * 存long
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */

	public static void setSettingLong(Context context, final String key, final long value) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putLong(key, value).commit();
	}

	/**
	 * 取long
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static long getPrefLong(Context context, final String key, final long defaultValue) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getLong(key, defaultValue);
	}

	/**
	 * 清除
	 * 
	 * @param context
	 * @param p
	 */
	public static void clearPreference(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		final Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}

}