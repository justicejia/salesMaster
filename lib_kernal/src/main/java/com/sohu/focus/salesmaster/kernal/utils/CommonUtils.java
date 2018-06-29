package com.sohu.focus.salesmaster.kernal.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sohu.focus.salesmaster.kernal.BaseApplication;
import com.sohu.focus.salesmaster.kernal.KernalConstants;
import com.sohu.focus.salesmaster.kernal.log.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * common utils
 * Created by qiangzhao on 2016/11/9.
 */

public class CommonUtils {

    /**
     * 将文本内容复制到剪贴板
     */
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(text);
    }

    /**
     * 判断手机是否安装了指定应用
     */
    public static boolean isAppAvilible(Context context, String pkg) {
        PackageManager packageManager = context.getPackageManager();//获取packageManager
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);//获取所有已安装应用包信息
        if (packageInfos != null && packageInfos.size() > 0) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String pn = packageInfos.get(i).packageName;
                if (pn.equals(pkg))
                    return true;
            }
        }
        return false;
    }

    /**
     * 拨打电话
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);
    }

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isMobile(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isMobile(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * @param password 用户输入密码
     * @return 有效则返回true, 无效则返回false
     */
    public static boolean isLoginPasswordValid(String password) {
        String regExp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\d]{6,16}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * @param username 用户名
     * @return 同上
     */
    public static boolean isUsernameVaild(String username) {
        return !username.matches("[0-9]+") && username.matches("^[a-z0-9_-]{4,24}$");
    }

    /**
     * @param verifyCode 验证码
     * @return 同上
     */
    public static boolean isVerifyCodeValid(String verifyCode) {
        return verifyCode.length() > 3;
    }

    /**
     * @param countryCode 国家码
     * @param phoneNumber 手机号
     * @return 返回拼接后的字符串
     */
    public static String getWellFormatMobile(String countryCode, String phoneNumber) {
        return countryCode + "-" + phoneNumber;
    }

    // 根据原图绘制圆形图片
    static public Bitmap createCircleImage(Bitmap source, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (0 == min) {
            min = source.getHeight() > source.getWidth() ? source.getWidth() : source.getHeight();
        }
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        // 创建画布
        Canvas canvas = new Canvas(target);
        // 绘圆
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        // 设置交叉模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 绘制图片
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    // 字符串截断
    public static String getLimitString(String source, int length) {
        if (null != source && source.length() > length) {
//            int reallen = 0;
            return source.substring(0, length) + "...";
        }
        return source;
    }

    // 字符串截断
    public static String getLimitStringWithoutNode(String source, int length) {
        if (null != source && source.length() > length) {
            return source.substring(0, length);
        }
        return source;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), ParseUtil.parseLong(id, 0));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * 时间格式化
     */
    public static String formattedTime(long second) {
        String hs, ms, ss, formatTime;

        long h, m, s;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
//        if (hs.equals("00")) {
//            formatTime = ms + ":" + ss;
//        } else {
        formatTime = hs + ":" + ms + ":" + ss;
//        }

        return formatTime;
    }

    public static int dp2pxConvertInt(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public static float getDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }


    /**
     * 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
     */
    public static int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }

    /**
     * 返回字符串里中文字或者全角字符的个数
     */
    public static int getChineseNum(String s) {
        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }

    /**
     * 网络是否正常
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                return KernalConstants.NETTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mobileInfo != null) {
                    switch (mobileInfo.getType()) {
                        case ConnectivityManager.TYPE_MOBILE:// 手机网络
                            switch (mobileInfo.getSubtype()) {
                                case TelephonyManager.NETWORK_TYPE_UMTS:
                                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                                case TelephonyManager.NETWORK_TYPE_HSDPA:
                                case TelephonyManager.NETWORK_TYPE_HSUPA:
                                case TelephonyManager.NETWORK_TYPE_HSPA:
                                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                                case TelephonyManager.NETWORK_TYPE_EHRPD:
                                case TelephonyManager.NETWORK_TYPE_HSPAP:
                                    return KernalConstants.NETTYPE_3G;
                                case TelephonyManager.NETWORK_TYPE_CDMA:
                                case TelephonyManager.NETWORK_TYPE_GPRS:
                                case TelephonyManager.NETWORK_TYPE_EDGE:
                                case TelephonyManager.NETWORK_TYPE_1xRTT:
                                case TelephonyManager.NETWORK_TYPE_IDEN:
                                    return KernalConstants.NETTYPE_2G;
                                case TelephonyManager.NETWORK_TYPE_LTE:
                                    return KernalConstants.NETTYPE_4G;
                                default:
                                    return KernalConstants.NETTYPE_NONE;
                            }
                    }
                }
            }
        }

        return KernalConstants.NETTYPE_NONE;
    }

    public static boolean isNetworkConnected(Context context) {
        return getNetWorkType(context) != KernalConstants.NETTYPE_NONE;
    }


    /**
     * 根据比例转化实际数值为相对值
     *
     * @param gear 档位
     * @param max  最大值
     * @param curr 当前值
     * @return 相对值
     */
    public static int filtNumber(int gear, int max, int curr) {
        return curr / (max / gear);
    }


    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.equals("") || str.equals("　") || str.equals(" ")
                || str.equals("null") || str.equals("(null)");
    }

    public static boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    /**
     * 返回非空字符串
     */
    public static String getDataNotNull(String str) {
        if (CommonUtils.isEmpty(str)) return "";
        return str;
    }

    /**
     * 返回非空字符串
     */
    public static String getDataNotNull(String str, String def) {
        if (CommonUtils.isEmpty(str)) return def;
        return str;
    }

    /**
     * 判断两个String是否相等
     */
    public static boolean isSameString(String str1, String str2) {
        boolean isSame = false;
        if (str1 == null && str2 != null) return false;
        if (str1 != null && str2 == null) return false;
        if (str1 == null && str2 == null) return true;
        if (str1 != null && str2 != null) {
            return str1.equals(str2);
        }
        return isSame;
    }

    public static boolean isSameArray(Object[] array1, Object[] array2) {
        if (array1 == null && array2 != null) return false;
        if (array1 != null && array2 == null) return false;
        if (array1 == null && array2 == null) return true;
        if (array1 != null && array2 != null) {
            if (array1.length != array2.length)
                return false;
            for (int i = 0; i < array1.length; i++) {
                if (!array1[i].equals(array2[i]))
                    return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean notEmpty(String str) {
        return (str != null && !str.equals("") && !str.equals("null") && !str.equals("　") && !str
                .equals(" ") && !str.equals("(null)"));
    }

    public static boolean notEmpty(Map map) {
        return (map != null && map.size() > 0);
    }


    /**
     * 判断List是否不为空
     *
     * @param list
     * @return
     */
    public static boolean notEmpty(List list) {
        return list != null && list.size() > 0;
    }

    /**
     * 判断List是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断数组是否不为空
     *
     * @param strArray
     * @return
     */
    public static boolean notEmpty(Object[] strArray) {
        return (strArray != null && strArray.length != 0);
    }

    public static boolean notEmpty(int[] intArray) {
        return (intArray != null && intArray.length != 0);
    }

    /**
     * 字符串转换UTF-8格式
     */
    public static String getUTF8String(String str) {
        String stringUTF8 = "";
        try {
            stringUTF8 = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringUTF8;
    }

    /**
     * long类型时间格式化
     */
    public static String convertToTime(long time, String match) {
        SimpleDateFormat df = new SimpleDateFormat(match);
        Date date = new Date(time);
        return df.format(date);
    }

    /**
     * 时间个数转化为long类型
     */
    public static long timeConvertToLong(String time, String match) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(match);
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int[] getDayHourMinute(long seconds) {
        int day = (int) seconds / (60 * 60 * 24);//换成天
        int hour = (int) (seconds - (60 * 60 * 24 * day)) / 3600;//总秒数-换算成天的秒数=剩余的秒数    剩余的秒数换算为小时
        int minute = (int) (seconds - 60 * 60 * 24 * day - 3600 * hour) / 60;//总秒数-换算成天的秒数-换算成小时的秒数=剩余的秒数    剩余的秒数换算为分
        int[] rev = new int[3];
        rev[0] = day;
        rev[1] = hour;
        rev[2] = minute;
        return rev;
    }

    public static String getHourMinuteSeconds(long seconds) {
        return getHourMinuteSeconds(seconds, false);
    }

    /**
     * 转换时间格式
     *
     * @param seconds
     * @param isSecond 传入的是否是秒， 则是秒单位，否则是毫秒单位，默认否
     * @return
     */
    public static String getHourMinuteSeconds(long seconds, boolean isSecond) {
        Integer ss = 1000;
        if (isSecond) {
            ss = 1;
        }
        Integer mi = ss * 60;
        Integer hh = mi * 60;

        int hour = (int) seconds / hh;//换小时
        int minute = (int) (seconds - (hh * hour)) / mi;//换分钟
        int second = (int) (seconds - (hh * hour) - mi * minute) / ss;//换算成秒
        StringBuffer sb = new StringBuffer();
        if (hour < 10) {
            sb.append("0" + hour + ":");
        } else {
            sb.append(hour + ":");
        }
        if (minute < 10) {
            sb.append("0" + minute + ":");
        } else {
            sb.append(minute + ":");
        }
        if (second < 10) {
            sb.append("0" + second);
        } else {
            sb.append(second);
        }
        return sb.toString();
    }

    /**
     * 邮箱正确性判断
     */
    public static boolean isEmail(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}"); // 验证邮箱
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 判断身份证号是否合法
     *
     * @return true 合法，false 非法
     */
    public static boolean isIdLegal(String id) {
        int[] weight = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        int[] checkDigit = new int[]{1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2};
        int[] arr = new int[18];
        int sum = 0;
        int flag = 0;
//        id = id.toUpperCase();
        if (!id.matches("^\\d{17}[0-9X]$")) return false;
        for (int i = 0; i < 18; i++) {
            arr[i] = Integer.parseInt(id.substring(i, i + 1)); // 使用IdentityCard.substring(i,
            // i+1)方法可取出号码的每一位
            if (i != 17) {
                sum += weight[i] * arr[i];
            }
        }
        flag = sum % 11; // 得出余数
        return checkDigit[flag] == arr[arr.length - 1];
    }

    /**
     * JavaBean对象转字符串
     *
     * @param object 对象
     * @return 字符串
     */
    public static String objectToJsonString(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * json字符串转JavaBean对象
     *
     * @param jsonString json字符串
     * @param valueType  对象类型
     * @param <T>        泛型
     * @return 对象
     */
    public static <T> T jsonStringToObject(String jsonString, Class<T> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, valueType);
        } catch (IOException e) {
            Logger.ZQ().e(jsonString + "\nconvert error : " + new Throwable(e));
        }
        return null;
    }

    /**
     * json数组字符串转List
     *
     * @param jsonStr   json数组字符串
     * @param valueType list type
     * @param <T>       泛型
     * @return 列表对象
     */
    public static <T> T jsonArrayToList(String jsonStr, TypeReference<T> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonStr, valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成随机数
     */
    public static String createNum(double max) {
        return (long) (max * Math.random()) + "";
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())
                    && !cpn.getClassName().contains("com.android.packageinstaller")) {
                return true;
            }
        }

        return false;
    }

    private static String getAvailMemory(Context context) {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }

    private static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("//s+");
            for (String num : arrayOfString) {
                Logger.ZQ().v(str2, num + "/t");
            }

            initial_memory = ParseUtil.parseInt(arrayOfString[1], 0) * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    public static void freeMemoryIfNecessary(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        float memRatio = (float) (getCurUsingMemory() / (double) Runtime.getRuntime().maxMemory());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mActivityManager.isLowRamDevice()) {
                BaseApplication.getApplication().freeMemory();
                Logger.ZQ().i("app is low memory device, free img memory");
            } else if (memRatio >= 0.5) {
                BaseApplication.getApplication().freeMemory();
                Logger.ZQ().i("app memory is low memory : has used " + memRatio + "%");
            }
        }
    }

    public static long getCurUsingMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    /**
     * 转义特殊url特殊字符
     *
     * @param str
     * @return
     */
    public static String toURLEncoded(String str) {
        if (CommonUtils.isEmpty(str)) return "";
        String temp;
        try {
            temp = new String(str.getBytes(), "UTF-8");
            temp = URLEncoder.encode(temp, "UTF-8");
            return temp;
        } catch (UnsupportedEncodingException e) {
            Logger.ZQ().e("toURLEncodedErr:  " + str, e.toString());
        }
        return "";
    }

    public static boolean isContainSpecialCharacter(String str) {
        String regex = "^[a-zA-Z0-9_\u4E00-\u9FA5]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(str);
        return !match.matches();
    }

    /**
     * @param cs
     * @return
     * @desc <pre>表情解析,转成unicode字符</pre>
     */
    public static String convertToMsg(CharSequence cs) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(cs);
        ImageSpan[] spans = ssb.getSpans(0, cs.length(), ImageSpan.class);
        for (int i = 0; i < spans.length; i++) {
            ImageSpan span = spans[i];
            String c = span.getSource();
            int a = ssb.getSpanStart(span);
            int b = ssb.getSpanEnd(span);
            if (c.contains("[")) {
                ssb.replace(a, b, convertUnicode(c));
            }
        }
        ssb.clearSpans();
        return ssb.toString();
    }


    private static String convertUnicode(String emo) {
        emo = emo.substring(1, emo.length() - 1);
        if (emo.length() < 6) {
            return new String(Character.toChars(Integer.parseInt(emo, 16)));
        }
        String[] emos = emo.split("_");
        char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
        char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
        char[] emoji = new char[char0.length + char1.length];
        for (int i = 0; i < char0.length; i++) {
            emoji[i] = char0[i];
        }
        for (int i = char0.length; i < emoji.length; i++) {
            emoji[i] = char1[i - char0.length];
        }
        return new String(emoji);
    }

    public static String getVersionName(Context context) {
        String pkName = context.getPackageName();
        String versionName;
        try {
            versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "1.5.0";
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        String pkName = context.getPackageName();
        int versionCode;
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionCode = 111;
        }
        return versionCode;
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    /**
     * MD5加密字符串
     */
    public static String getMD5String(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuilder strBuf = new StringBuilder();
            for (byte anEncryption : encryption) {
                if (Integer.toHexString(0xff & anEncryption).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & anEncryption));
                } else {
                    strBuf.append(Integer.toHexString(0xff & anEncryption));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String generateRequestJson(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        if (notEmpty(params)) {
            stringBuilder.append("{");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuilder.append("\"");
                stringBuilder.append(entry.getKey());
                stringBuilder.append("\":\"");
                stringBuilder.append(entry.getValue());
                stringBuilder.append("\",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }

    /**
     * 获取不超过4位数的数字，大于一万时显示小数点后一位，单位万人
     */
    public static String getShowCountString(int count) {
        String num;
        if (count >= 10000) {
            num = String.valueOf((double) Math.round(count / 1000) / 10) + "万";
        } else {
            num = String.valueOf(count);
        }
        return num;
    }
}
