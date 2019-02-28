package com.callphone.client.home.detail;

import com.callphone.client.R;
import com.callphone.client.home.CallInfoItem;
import com.hd.base.fragment.IBaseTitleBarFragment;

/**
 * Created by liugd on 2019/2/28.
 */

public class CallInfoFragment extends IBaseTitleBarFragment {
    @Override
    protected void initTitleBarView() {


        CallInfoItem item = getSerializableExtra(ID_NEW_PARAM1);
        setText(R.id.textView, item.toString());
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_call_info;
    }
}
