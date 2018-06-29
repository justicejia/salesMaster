package com.sohu.focus.salesmaster.kernal.utils;

/**
 * 根据url裁剪图片
 * Created by yuanminjia on 2017/10/24.
 */

public class ClipUtils {
    /**
     * 楼盘首页头部楼盘预览图
     */
    public static String getBuildHomeClip() {
        return "sh750x375sh";
    }

    /**
     * 楼盘相册页
     */
    public static String getBuildingAlbumClip() {
        return "sh750x420sh";
    }

    /**
     * 户型页户型预览图
     */
    public static String getHouseTypeClip() {
        return "sh750x420sh";
    }

    /**
     * 户型相册
     */
    public static String getHouseTypeAlbumClip() {
        return "sh800x700sh";
    }

    /**
     * 楼盘列表页楼盘封面
     */
    public static String getBuildingListClip() {
        return "sh240x180sh";
    }

    /**
     * 直播列表封面
     */
    public static String getLiveListCoverClip() {
        return "sh373x373sh";
    }


    /**
     * 获取通用的图片host地址
     */
    public static String getCommonPicHost() {
        return "https://t.focus-img.cn/";
    }

    /**
     * 裁剪楼盘、户型图的相册和预览，楼盘列表封面
     * 仅对符合 http(s)://xxxx/xf/xxxxx 样式的url裁剪
     *
     * @param clip 裁剪尺寸
     * @param url  后缀或完整的未裁剪url
     */
    public static String getCustomPicUrl(String clip, String url) {
        if (CommonUtils.notEmpty(url)) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                return getCommonPicHost() + clip + url;
            }
            if (url.contains("/xf")) {
                return getCommonPicHost() + clip + url.substring(url.indexOf("/xf"));
            } else {
                return url;
            }
        }
        return "";
    }

    /**
     * 裁剪直播间列表封面
     * 仅对符合 http(s)://t.focus-img.cn/live/xxxxx 样式的url裁剪
     *
     * @param url 封面原始url，
     */
    public static String getLiveListCover(String url) {
        if (url.contains("t.focus-img.cn/live")) {
            return getCommonPicHost() + getLiveListCoverClip() + url.substring(url.indexOf("/live"));
        }
        return url;
    }
}
