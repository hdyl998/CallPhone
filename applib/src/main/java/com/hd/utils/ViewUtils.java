package com.hd.utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hd.R;
import com.hd.utils.device.AppDeviceInfo;

/**
 * Created by liugd on 2017/3/17.
 */

public class ViewUtils {

    /***
     * 解析出一个VIEW
     *
     * @param parent
     * @param layoutID
     * @return
     */
    public static View getInflateView(ViewGroup parent, @LayoutRes int layoutID) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false);
    }

    /***
     * 解析出一个VIEW
     *
     * @param parent
     * @param layoutID
     * @return
     */
    public static View getInflateView(ViewGroup parent, @LayoutRes int layoutID, boolean attachToRoot) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, attachToRoot);
    }

    public static View getInflateView(Context context, @LayoutRes int layoutID) {
        return View.inflate(context, layoutID, null);
    }

    /***
     * 设置窗口宽度为窗口大小
     *
     * @param window
     */
    public static void setWindowDialogWidth(Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = AppDeviceInfo.mDialogWidth; // 设置宽度
        window.setAttributes(lp);
    }

    /**
     * 获得textView的文本去掉首尾
     *
     * @param textView
     * @return
     */
    public static String getEditTextTrimText(TextView textView) {
        return textView.getText().toString().trim();
    }


    /**
     * 获双行的空的emptyview of double line
     *
     * @param context
     * @param title
     * @param msg
     * @return
     */
    public static LinearLayout getEmptyViewOfDoubleLine(Context context, CharSequence title,
                                                        CharSequence msg) {
        LinearLayout view = (LinearLayout) View.inflate(context, R.layout.view_empty_double_line,
                null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_tip1);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_tip2);
        tvTitle.setText(title);
        tvContent.setText(msg);
        return view;
    }

    public static LinearLayout getEmptyViewOfDoubleLine(Context context, int resID) {
        String strings[] = context.getResources().getString(resID).split("\\|");
        if (strings.length < 2) {
            return getEmptyViewOfDoubleLine(context, strings[0], "");
        }
        return getEmptyViewOfDoubleLine(context, strings[0], strings[1]);
    }

}
