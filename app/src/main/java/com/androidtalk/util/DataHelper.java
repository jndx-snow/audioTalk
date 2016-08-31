package com.androidtalk.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.androidtalk.entity.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataHelper {
    private static final String AUTHORITY = "com.androidTalk";
    private static final Uri CONTENNT_URI = Uri.parse("content://" + AUTHORITY + "/DataProvider");
    private static final String[] PROJECTION = new String[]{"id", "cmd_name", "cmd_category", "relation"};

    private static String TAG = DataHelper.class.getSimpleName();
    public static HashMap<String, String> map = new HashMap<String, String>();

    /**
     * 向数据库中添加命令
     * @param activity
     * @param name
     * @param category
     * @param relation
     */
    public static void addCommand(Activity activity, String name, String category, String relation) {
        ContentValues cv = new ContentValues();
        cv.put("cmd_name", name);
        cv.put("cmd_category", category);
        cv.put("relation", relation);
        activity.getContentResolver().insert(CONTENNT_URI, cv);
    }

    /**
     * 根据应用名，查询数据库得到已设置的该命令
     * @param category
     * @param activity
     * @return
     */
    public static Command getCommand(String category, Activity activity) {
        Command cmd = new Command();
        Cursor cur = activity.getContentResolver().query(CONTENNT_URI, PROJECTION, "cmd_category=" + "\"" + category + "\"", null, null);
        cur.moveToFirst();
        if (cur.getCount()>0) {
            Log.v(TAG, "cur==" + cur.getCount()+";"+cur.getColumnCount());
//            +";"+cur.getColumnIndex("id")
//            Log.v(TAG,"cur=="+cur.getString(0)+";"+cur.getString(1)+";"+cur.getString(2)+";"+cur.getString(3));
            cmd.setId(cur.getString(0));
            cmd.setCmdName(cur.getString(1));
            cmd.setCmdCategory(cur.getString(2));
            cmd.setRelation(cur.getString(3));
        }
        return cmd;
    }

    /**
     * 获取已设置的命令行列表
     * @param activity
     * @return
     */
    public static Cursor getCommandList(Activity activity) {
        Cursor cur = activity.getContentResolver().query(CONTENNT_URI, PROJECTION, null, null, null);
        return cur;

    }

    /**
     * 获取手机所装载的应用
     *
     * @param activity
     * @return
     */
    public static ArrayList<String> getInstalledApps(Activity activity) {
        ArrayList<String> res = new ArrayList<String>();
        List<PackageInfo> packs = activity.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);

            String appname = p.applicationInfo.loadLabel(activity.getPackageManager()).toString();
            String pname = p.applicationInfo.packageName;
            map.put(appname, pname);
            res.add(appname);
        }
        return res;
    }

    /**
     * 根据应用名，获取应用程序包名
     *
     * @param name
     * @return
     */
    public static String getPackageName(String name) {
        String value = "";
        for (String key : map.keySet()) {
            if (key.equals(name)) {
                value = map.get(key);
            }
        }
        return value;
    }
}
