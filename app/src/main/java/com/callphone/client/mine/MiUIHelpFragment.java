package com.callphone.client.mine;

import android.view.View;

import com.callphone.client.R;
import com.hd.base.fragment.IBaseScrollViewFragment;
import com.hd.utils.GoUtils;
import com.hd.utils.toast.ToastUtils;

/**
 * Created by liugd on 2019/3/1.
 */

public class MiUIHelpFragment extends IBaseScrollViewFragment {
    @Override
    protected void initScrollView() {
        getTitleBar().setTitleText("小米MIUI系统参考设置").setRightText("设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoUtils.goAppDetailsSetting(mContext);
            }
        });

    }

    @Override
    public int[] setClickIDs() {
        return new int[]{R.id.btnTestToast, R.id.btnSet};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTestToast:
                ToastUtils.show("这是一个测试弹层！");
                break;
            case R.id.btnSet:
                GoUtils.goAppDetailsSetting(mContext);
                break;
        }
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
