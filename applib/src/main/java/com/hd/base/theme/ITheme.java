package com.hd.base.theme;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public interface ITheme {

    /***
     * 背景颜色
     * 背景颜色默认为灰色
     *
     * @return
     */
    int setBackgroundColor();

//    /***
//     * 设置状态栏是颜色(API 21及以上有效)(含有电量，WIFI状态，时间显示的顶栏)
//     *
//     * @return
//     */
//    public int setStatusBarColor() {
//        return R.color.statusBarColorLight;
//    }

    /***
     * 是否有颜色状态栏
     * 是否是颜色式状态栏(API 21及以上有效)默认是有的，当需要全屏顶部沉浸显示图片时重写此方法，把变量设置成false
     *
     * @return
     */
    boolean isStatusBarColor();


    /***
     * 是否是浅色的状态栏
     * @return
     */
    boolean isStatusBarLightStyle();
}
