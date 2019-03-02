package com.callphone.client.home;

import android.text.SpannableStringBuilder;

import com.callphone.client.base.ColorConsts;
import com.callphone.client.utils.StringUtil;
import com.hd.utils.text.ITextStyle;

import java.io.Serializable;

/**
 * Created by liugd on 2019/2/28.
 */

public class CallInfoItem implements Serializable {
    public String id;
    public String phone;
    public int status;
    public String updatetime;
    public String extraMsg;
    //  id: 日志id
//    phone：拨打的手机号
//    status：状态 1=成功 0=是吧
//    updatetime：更新时间


    public String getStatusString() {
        return status == 1 ? "成功" : "失败";
    }

    public int getStatusColor() {
        return status == 1 ? ColorConsts.COLOR_WIN : ColorConsts.COLOR_LOSE;
    }


    public CharSequence getInfo() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(String.format("手机号：%s\n拨号时间：%s\n", phone, updatetime));
        builder.append("状态：");
        builder.append(ITextStyle.createColorText(getStatusString()).setColor(getStatusColor()).getString());
        if (extraMsg != null) {
            builder.append("附加消息：");
            builder.append(extraMsg);
        }
        return builder;
    }

}
