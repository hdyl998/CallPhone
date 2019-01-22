package com.hd.utils.other;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Administrator on 2018/1/18.
 */

public class VibratorHelper {
    Vibrator vibrator;

    public VibratorHelper(Context mContext) {
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }


    public void doVibrator(long milliseconds) {
        vibrator.vibrate(milliseconds);
    }


    public void onDestory() {
        vibrator.cancel();
    }
}
