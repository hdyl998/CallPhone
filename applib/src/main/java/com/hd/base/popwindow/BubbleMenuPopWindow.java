package com.hd.base.popwindow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hd.R;
import com.hd.base.adapterbase.BaseViewHolder;
import com.hd.base.adapterbase.LinearLayoutAdapter;
import com.hd.base.maininterface.IComCallBacks;
import com.hd.utils.ViewUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liugd on 2017/7/7.
 */
//提供下拉的气泡选择菜单
public class BubbleMenuPopWindow extends IBasePopupWindow {
    IComCallBacks<Integer> comCallBacks;
    LinearLayout llContent;

    public BubbleMenuPopWindow(Context mContext) {
        super(mContext);
        llContent = findViewByID(R.id.ll_content);
//        getRootView().setBackgroundResource(R.color._00000000);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public BubbleMenuPopWindow setDatas(List<String> listStrings) {
        new LinearLayoutAdapter<String>(llContent, listStrings, R.layout.layout_base_item_pop_menu) {
            @Override
            public void onBind(BaseViewHolder holder, String item, int position) {
                TextView textView = holder.getView(R.id.textView);
                textView.setText(item);
                textView.setId(position);
                textView.setOnClickListener(BubbleMenuPopWindow.this);
                if (position != 0) {
                    ViewUtils.getInflateView(getLinearLayout(), R.layout.view_line_1_deep, true);
                }
            }
        };
        return this;
    }

    public BubbleMenuPopWindow setDatas(String strings[]) {
        return setDatas(Arrays.asList(strings));
    }

    public static BubbleMenuPopWindow create(Context context) {
        BubbleMenuPopWindow popWindow = new BubbleMenuPopWindow(context);
        return popWindow;
    }

    public BubbleMenuPopWindow setComCallBacks(IComCallBacks<Integer> comCallBacks) {
        this.comCallBacks = comCallBacks;
        return this;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            default:
                comCallBacks.call(v.getId());
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_base_bubble_pop;
    }
}
