package com.callphone.client.mine;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.callphone.client.R;
import com.callphone.client.main.mine.LoginManager;
import com.hd.base.fragment.IBaseTitleBarFragment;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;

/**
 * Created by liugd on 2019/2/26.
 */

public class MineFragment extends IBaseTitleBarFragment {

    @MyBindView(value = R.id.ivHead, click = true)
    ImageView ivHead;

    @MyBindView(value = R.id.tvLogin, click = true)
    TextView tvLogin;


    @Override
    public void onClick(View v) {
        if(LoginManager.isLoginAndRedict(mContext)){
            return;
        }
        switch (v.getId()){
            case R.id.ivHead:
            case R.id.tvLogin:
                break;

        }
    }

    @Override
    protected void initTitleBarView() {
        MyBufferKnifeUtils.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }
}
