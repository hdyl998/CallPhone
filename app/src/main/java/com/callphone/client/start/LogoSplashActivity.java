package com.callphone.client.start;

import android.app.Activity;
import android.os.Bundle;

/**
 * Note：None
 * Created by Liuguodong on 2019/1/3 16:11
 * E-Mail Address：986850427@qq.com
 */
public class LogoSplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartActivity.launch(this);
        finish();
    }
}
