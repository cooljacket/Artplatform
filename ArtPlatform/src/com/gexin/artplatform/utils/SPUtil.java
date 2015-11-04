package com.gexin.artplatform.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreference 封装工具类
 * 里面所有的commit操作使用了SharedPreferencesCompat.apply进行了替代
 * 目的是尽可能的使用apply代替commit
 * 因为commit方法是同步的，并且我们很多时候的commit操作都是UI线程中，毕竟是IO操作，尽可能异步；
 * 所以我们使用apply进行替代，apply异步的进行写入；
 * 
 * @author xiaoxin 2015-4-5
 */
public class SPUtil {

	// 保存在手机中的文件名
	public static final String FILE_NAME = "config";

	/** 
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法 
     *  保存时键名相同会覆盖
     * @param context 所在上下文
     * @param key 键名
     * @param object 对应的值
     */ 
	public static void put(Context context, String key, Object object) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}

		SharedPreferencesCompat.apply(editor);
	}
	
	/** 
     * 获取数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值 
     *  
     * @param context 所在上下文
     * @param key 键名
     * @param defaultObject 默认值（无法找到该键对应的值的时候的值）
     * @return 得到的数据
     */ 
    public static Object get(Context context, String key, Object defaultObject)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
   
        if (defaultObject instanceof String)  
        {  
            return sp.getString(key, (String) defaultObject);  
        } else if (defaultObject instanceof Integer)  
        {  
            return sp.getInt(key, (Integer) defaultObject);  
        } else if (defaultObject instanceof Boolean)  
        {  
            return sp.getBoolean(key, (Boolean) defaultObject);  
        } else if (defaultObject instanceof Float)  
        {  
            return sp.getFloat(key, (Float) defaultObject);  
        } else if (defaultObject instanceof Long)  
        {  
            return sp.getLong(key, (Long) defaultObject);  
        }  
   
        return null;  
    }  
   
    /** 
     * 移除某个key值已经对应的值 
     * @param context 所在上下文
     * @param key 键名
     */ 
    public static void remove(Context context, String key)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sp.edit();  
        editor.remove(key);  
        SharedPreferencesCompat.apply(editor);  
    }  
   
    /** 
     * 清除所有数据 
     * @param context 所在上下文
     */ 
    public static void clear(Context context)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sp.edit();  
        editor.clear();  
        SharedPreferencesCompat.apply(editor);  
    }  
   
    /** 
     * 查询某个key是否已经存在 
     * @param context 所在上下文
     * @param key 键名
     * @return 存在返回true，不存在返回false
     */ 
    public static boolean contains(Context context, String key)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        return sp.contains(key);  
    }  
   
    /** 
     * 返回所有的键值对 
     *  
     * @param context 所在上下文
     * @return 存储所有键值对的Map
     */ 
    public static Map<String, ?> getAll(Context context)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        return sp.getAll();  
    }  

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * @author xiaoxin
	 * 2015-4-5
	 */
	private static class SharedPreferencesCompat {
		//反射机制
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			editor.commit();
		}
	}

}
