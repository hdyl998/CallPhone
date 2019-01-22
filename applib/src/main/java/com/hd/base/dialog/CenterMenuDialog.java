package com.hd.base.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hd.R;
import com.hd.base.adapterbase.BaseViewHolder;
import com.hd.base.adapterbase.FullViewGroupAdapter;
import com.hd.base.maininterface.IComCallBacks;
import com.hd.utils.ViewUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 中间弹窗的菜单选择
 * Created by liugd on 2017/7/12.
 */

public class CenterMenuDialog extends IBaseDialog implements View.OnClickListener {

    LinearLayout linearLayout;
    FullViewGroupAdapter<String> adapter;
    TextView tvTitle;
    IComCallBacks<Integer> comCallBacks;

    public CenterMenuDialog(Context context) {
        super(context);
        linearLayout = findViewByID(R.id.ll_content);
        tvTitle = findViewByID(R.id.tv_title);

        adapter = new FullViewGroupAdapter<String>(linearLayout, R.layout.item_dialog_learn_select_tag) {
            @Override
            public void onBind(BaseViewHolder holder, String item, int position) {
                TextView textView = holder.getView(R.id.textView);
                textView.setText(item);
                textView.setId(position);
                textView.setOnClickListener(CenterMenuDialog.this);
            }
        };
        ViewUtils.setWindowDialogWidth(getWindow());
    }

    public static CenterMenuDialog create(Context context) {
        CenterMenuDialog dialog = new CenterMenuDialog(context);
        dialog.show();
        return dialog;
    }


    public CenterMenuDialog setDatas(List<String> listStrings) {
        adapter.setDatas(listStrings);
        return this;
    }

    public CenterMenuDialog setTitleText(String titleText) {
        this.tvTitle.setText(titleText);
        return this;
    }

    public CenterMenuDialog setComCallBacks(IComCallBacks<Integer> comCallBacks) {
        this.comCallBacks = comCallBacks;
        return this;
    }

    public CenterMenuDialog setDatas(String strings[]) {
        return setDatas(Arrays.asList(strings));
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (comCallBacks != null)
            comCallBacks.call(v.getId());
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_learn_select_tag;
    }
}
