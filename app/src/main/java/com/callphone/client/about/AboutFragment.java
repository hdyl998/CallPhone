package com.callphone.client.about;

import android.widget.TextView;

import com.callphone.client.R;
import com.hd.base.fragment.IBaseTitleBarFragment;
import com.hd.utils.Utils;

/**
 * Created by liugd on 2019/2/28.
 */

public class AboutFragment extends IBaseTitleBarFragment {
    @Override
    protected void initTitleBarView() {
        TextView textView=findViewByID(R.id.textView);
        textView.setText("软件版本："+ Utils.getVersionName()+"\n"+"内部代码："+ Utils.getVersionCode());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about;
    }
}
