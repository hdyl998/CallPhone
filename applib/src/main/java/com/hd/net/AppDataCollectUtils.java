package com.hd.net;

import java.util.HashMap;
import java.util.Map;

/**
 * Date:2017/11/10 11:18
 * Author:liugd
 * Modification:App信息采集
 * ................
 * 佛祖保佑，永无BUG
 **/


public class AppDataCollectUtils {


    public static String map2Url(Map<String, String> map) {
        return map2Url(map, false);
    }



    /***
     * 把 map 转成 url形式的字符串
     * 如有键值对 "key1-value1","key2-value2"  转换成key1=value2&key2=value2
     * @param map
     * @param isFirstAnd
     * @return
     */
    public static String map2Url(Map<String, String> map, boolean isFirstAnd) {
        StringBuilder sBuilder = new StringBuilder();
        boolean flag = isFirstAnd;//第一次不加&符号
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (flag) {
                sBuilder.append("&");
            }
            flag = true;
            sBuilder.append(entry.getKey());
            sBuilder.append("=");
            sBuilder.append(entry.getValue());
        }
        return sBuilder.substring(0);
    }

    /***
     * webView 增加的参数
     * @param map
     * @return
     */
    private static Map<String, String> getWebViewAddParams(Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    /***
     *  H5的url有几种情况要判断
     * index.php?c=vip&a=vipdetail   只有问号      &ck=****
     * index.php?c=vip&a=vipdetail#/userbuy   有问号+井号的    ？ck=****
     * index.php?c=vip&a=vipdetail#/databuy?p=1  有问号+井号+问号    &ck=******
     * @param url  原始 url
     * @param map 参数表，没有可以传空
     * @return
     */
    public static String getWebViewUrlWithParams(String url, Map<String, String> map) {
        map = getWebViewAddParams(map);
        int index = url.indexOf("#");
        if (index > -1) {//有#号 参数加在#号前面
            String firstUrl = url.substring(0, index);
            StringBuilder stringBuilder = new StringBuilder(firstUrl);
            if (firstUrl.contains("?") && firstUrl.contains("=")) {//两个都同时存在
                stringBuilder.append("&");
            } else if (!firstUrl.contains("?")) {
                stringBuilder.append("?");
            }
            stringBuilder.append(map2Url(map));
            stringBuilder.append(url.substring(index));
            return stringBuilder.substring(0);
        } else {//没有#号
            StringBuilder stringBuilder = new StringBuilder(url);
            if (url.contains("?") && url.contains("=")) {//两个都同时存在
                stringBuilder.append("&");
            } else if (!url.contains("?")) {
                stringBuilder.append("?");
            }
            stringBuilder.append(map2Url(map));
            return stringBuilder.substring(0);
        }
    }



    public static String map2UrlWithXiexian(Map<String, String> map) {
        return map2UrlWithXiexian(map, false);
    }


    /***
     * 把 map 转成 url形式的字符串
     * 如有键值对 "key1-value1","key2-value2"  转换成key1=value2&key2=value2
     * @param map
     * @param isFirstAnd
     * @return
     */
    public static String map2UrlWithXiexian(Map<String, String> map, boolean isFirstAnd) {
        StringBuilder sBuilder = new StringBuilder();
        boolean flag = isFirstAnd;//第一次不加&符号
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (flag) {
                sBuilder.append("/");
            }
            flag = true;
            sBuilder.append(entry.getKey());
            sBuilder.append("/");
            sBuilder.append(entry.getValue());
        }
        return sBuilder.substring(0);
    }

}
