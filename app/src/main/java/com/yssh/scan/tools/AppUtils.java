package com.yssh.scan.tools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获得包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static String encodeMD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toUpperCase();
            //  return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isAllPermissionGranted(Context mContext,
                                                 String permissionList[]) {
        if (permissionList.length == 0)
            return false;
        for (int i = 0; i < permissionList.length; i++) {
            if (ContextCompat.checkSelfPermission(mContext, permissionList[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }


    /**
     * 拨打电话
     *
     * @param mContext    猜猜这是啥
     * @param phoneNum    打给谁
     * @param msg         提示语
     * @param requestCode 假如没有权限的话——————你说咋办就咋办
     */
    public static void callPhoneNum(final Activity mContext, final String phoneNum, String msg, int requestCode) {
        if (AppUtils.isAllPermissionGranted(mContext, new String[]{Manifest.permission.CALL_PHONE})) {
            AlertUtils.showConfirmDialog(mContext, msg,
                    true, new AlertUtils.IDialogClickListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onConfirm() {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                    .parse("tel:" + phoneNum));
                            mContext.startActivity(intent);
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CALL_PHONE}, requestCode);
        }


    }

//
//    /**
//     * @param mActivity
//     * @param phoneNum
//     */
//    public static void callPhone(final Activity mActivity, final String phoneNum) {
//        getPersimmions(Manifest.permission.CALL_PHONE);
//        AlertUtils.showConfirmDialog(mActivity, "拨打：" + phoneNum, true,
//                new AlertUtils.IDialogClickListener() {
//                    @SuppressLint("MissingPermission")
//                    @Override
//                    public void onConfirm() {
//                        if (mActivity != null) {
//                            Intent intent = new Intent(Intent.ACTION_CALL, Uri
//                                    .parse("tel:" + phoneNum));
//                            mActivity.startActivity(intent);
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//                    }
//                });
//    }

    public static void setupApk(Context context, File file) {
        Uri file1 = FileProvider.getUriForFile(context, getPackageName(context) + ".provider", file);
        if (file.exists()) {
            Intent intentApk = new Intent(Intent.ACTION_VIEW);
            intentApk.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentApk.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentApk.setDataAndType(file1,
                    "application/vnd.android.package-archive");
            context.startActivity(intentApk);
        }
    }

    /**
     * 获取屏幕的宽度
     *
     * @param mContext
     * @return
     */
    public static int getWithDisplay(Context mContext) {

        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取状态栏高度一
     *
     * @param context
     * @return
     */
    public int getStatusBarHeight(Context context) {

        int height = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resId > 0) {
            height = context.getResources().getDimensionPixelSize(resId);
        }
        if(height==0){
            return getStatusHeight(context);
        }
        return height;

    }

    /**
     * 获取状态栏高度二
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = 0;

        Class<?> localClass;
        try {
            localClass = Class.forName("com.android.internal.R$dimen");
            Object localObject = localClass.newInstance();
            int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
            statusHeight = context.getResources().getDimensionPixelSize(i5);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return statusHeight;
    }

    public static String getSafetySign(Map<String, String> params,
                                       String signKey) {
        Map<String, String> tempMap = new HashMap<String, String>();
        Set<String> set = params.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String content = params.get(key);
            if (content != null && !content.equals("null")
                    && !TextUtils.isEmpty(content))
                tempMap.put(key, content);
        }

        if (tempMap.size() == 0)
            return null;

        String stringA = "";
        Map<String, String> orderMap = orderkey(tempMap);
        set = orderMap.keySet();
        it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String content = orderMap.get(key);
            if (TextUtils.isEmpty(stringA))
                stringA = key + "=" + content;
            else
                stringA += "&" + key + "=" + content;
        }

        String stringSignTemp = stringA + "&key=" + signKey;

        String sign = encodeMD5(stringSignTemp).toUpperCase();

        return sign;

    }

    /**
     * Map key 按字典排序
     *
     * @param map
     * @return
     */
    private static Map<String, String> orderkey(Map<String, String> map) {
        HashMap<String, String> tempMap = new LinkedHashMap<String, String>();
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(
                map.entrySet());

        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, String> item = infoIds.get(i);
            tempMap.put(item.getKey(), item.getValue());
        }
        return tempMap;
    }

    public static String getRandomNum(int num) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            int x = (int) Math.rint((Math.random() * 8) + 1);
            builder.append(x);
        }

        return builder.toString();

    }
}