package com.mvvm.lux.framework.utils;

import android.content.Context;
import android.widget.Toast;

import com.mvvm.lux.framework.BaseApplication;

/**
 * 单例Toast
 * @author ex-caowanjiang001
 * 2016年1月4日14:35:30
 */
public class ToastUtil {

	private static String oldMsg;
	protected static Toast toast = null;
	private static long oneTime = 0;
	private static long twoTime = 0;

	/**
	 * 弹吐司,指定时间间隔内不能弹出重复信息.
	 * @param context
	 * @param s
     */
	public static void showToast(Context context, String s) {
		if (toast == null) {
			toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
			toast.show();
			oneTime = System.currentTimeMillis();
		} else {
			twoTime = System.currentTimeMillis();
			if (s.equals(oldMsg)) {
				if (twoTime - oneTime > Toast.LENGTH_SHORT) {
					toast.show();
				}
			} else {
				oldMsg = s;
				toast.setText(s);
				toast.show();
			}
		}
		oneTime = twoTime;
	}

	/**
	 * 弹吐司,指定时间间隔内不能弹出重复信息.
	 * @param s
	 */
	public static void showToast(String s) {
		if (toast == null) {
			toast = Toast.makeText(BaseApplication.getAppContext(), s, Toast.LENGTH_SHORT);
			toast.show();
			oneTime = System.currentTimeMillis();
		} else {
			twoTime = System.currentTimeMillis();
			if (s.equals(oldMsg)) {
				if (twoTime - oneTime > Toast.LENGTH_SHORT) {
					toast.show();
				}
			} else {
				oldMsg = s;
				toast.setText(s);
				toast.show();
			}
		}
		oneTime = twoTime;
	}

	public static void showToast(String s, int type) {
		if (toast == null) {
			toast = Toast.makeText(BaseApplication.getAppContext(), s, type);
			toast.show();
			oneTime = System.currentTimeMillis();
		} else {
			twoTime = System.currentTimeMillis();
			if (s.equals(oldMsg)) {
				if (twoTime - oneTime > type) {
					toast.show();
				}
			} else {
				oldMsg = s;
				toast.setText(s);
				toast.show();
			}
		}
		oneTime = twoTime;
	}

	public static void showToast(int resId) {
		showToast(BaseApplication.getAppContext(), BaseApplication.getAppContext().getString(resId));
	}

}
