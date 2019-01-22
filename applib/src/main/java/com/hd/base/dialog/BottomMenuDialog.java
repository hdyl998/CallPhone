package com.hd.base.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hd.R;
import com.hd.base.adapterbase.BaseViewHolder;
import com.hd.base.adapterbase.FullViewGroupAdapter;
import com.hd.base.maininterface.IComCallBacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 无选择项目的底部弹出对话框
 * Created by liugd on 2017/6/12.
 */

public class BottomMenuDialog extends IBaseBottomDialog {

    LinearLayout llContent;
    IComCallBacks<Integer> comCallBacks;
    List<IComCallBacks> listCallBacks;
    FullViewGroupAdapter<String> adapter;

    public BottomMenuDialog(Context context) {
        super(context);
        llContent = findViewByID(R.id.ll_content);
        adapter = new FullViewGroupAdapter<String>(llContent, getItemId()) {
            @Override
            public void onBind(BaseViewHolder holder, String item, int position) {
                BottomMenuDialog.this.onBind(holder, item, position);
            }
        };
    }


    public LinearLayout getContentView() {
        return llContent;
    }

    /***
     * 设置绑定需要做的事
     *
     * @param holder
     * @param item
     * @param position
     */
    public void onBind(BaseViewHolder holder, String item, int position) {
        TextView textView = holder.getView(R.id.textView);
        textView.setText(item);
        textView.setId(position);
        textView.setOnClickListener(BottomMenuDialog.this);

        holder.setVisibility(R.id.line1, position != 0 ? View.VISIBLE : View.GONE);
    }


    /***
     * 设置布局ID
     *
     * @return
     */
    public int getItemId() {
        return R.layout.layout_base_item_bottom_menu;
    }


    public static BottomMenuDialog create(Context context) {
        BottomMenuDialog dialog = new BottomMenuDialog(context);
        dialog.show();
        return dialog;
    }

    @Override
    public int[] setClickIDs() {
        return new int[]{R.id.tv_cancel};
    }


    public BottomMenuDialog setDatas(List<String> listStrings) {
        adapter.setDatas(listStrings);
        return this;
    }

    public BottomMenuDialog setDatas(String strings[]) {
        return setDatas(Arrays.asList(strings));
    }


    public BottomMenuDialog setComCallBacks(IComCallBacks<Integer> comCallBacks) {
        this.comCallBacks = comCallBacks;
        return this;
    }


    public BottomMenuDialog setListCallBacks(List<IComCallBacks> listCallBacks) {
        this.listCallBacks = listCallBacks;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_cancel) {
            cancel();//cancel
        } else {
            dismiss();
            int index = v.getId();
            if (comCallBacks != null) {
                comCallBacks.call(index);
            }
            if (listCallBacks != null) {
                listCallBacks.get(index).call(adapter.getDatas().get(index));
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_base_bottom_menu_dialog;
    }


    public final static class Builder {
        List<String> listTexts = new ArrayList<>(3);
        List<IComCallBacks> listCallBacks = new ArrayList<>(3);
        Context context;

        public Builder(Context context) {
            this.context = context;
        }

        /***
         * 动态添加添加条目
         * @param text
         * @param callBacks
         * @return
         */
        public Builder addItems(String text, IComCallBacks<String> callBacks) {
            listTexts.add(text);
            listCallBacks.add(callBacks);
            return this;
        }

        public BottomMenuDialog build() {
            return new BottomMenuDialog(context).setDatas(listTexts).setListCallBacks(listCallBacks);
        }

        public BottomMenuDialog buildShow() {
            BottomMenuDialog dialog = build();
            dialog.show();
            return dialog;
        }

    }
}
