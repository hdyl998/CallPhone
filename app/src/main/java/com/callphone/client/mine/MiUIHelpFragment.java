package com.callphone.client.mine;

import android.view.View;

import com.callphone.client.R;
import com.hd.base.fragment.IBaseScrollViewFragment;
import com.hd.utils.GoUtils;

/**
 * Created by liugd on 2019/3/1.
 */

public class MiUIHelpFragment extends IBaseScrollViewFragment {
    @Override
    protected void initScrollView() {
        getTitleBar().setTitleText("小米手机参照配置").setRightText("设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoUtils.goAppDetailsSetting(mContext);
            }
        });

    }

    @Override
    protected int getBodyId() {
        return R.layout.fragment_miui_help;
    }

    @Override
    protected void setAPIRequest() {
        scrollView.showNormal();
    }
}
