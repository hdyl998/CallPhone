package com.hd.utils.other;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 键盘工具类
 * Created by liugd on 2017/3/20.
 */

public class KeyboardUtils {

//    public static void popSoftkeyboard(Context ctx, View view, boolean wantPop) {
//        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (wantPop) {
//            view.requestFocus();
//            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//        } else {
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//
//    public static void hideSoftKeyboard(Activity activity) {
//        View view = activity.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//

    /***
     * 隐藏键盘
     *
     * @param context
     * @param editText
     */
    public static void hideSoftKeyboard(Activity context, EditText editText) {
        try {
            ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager
                            .HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param activity 当前Activity
     * @return void
     * @方法名: hideSoftKeyboard
     * @功能描述:隐藏键盘
     */
    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity != null && activity.getCurrentFocus() != null) {
                ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param activity
     * @param view
     * @return void
     * @方法名: showSoftKeyboard
     * @功能描述:显示键盘
     */
    public static void showSoftKeyboard(Activity activity, View view) {
        ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
    }

}
