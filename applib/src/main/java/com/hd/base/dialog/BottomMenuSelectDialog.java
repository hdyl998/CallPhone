package com.hd.base.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hd.R;
import com.hd.base.adapterbase.BaseViewHolder;

import java.util.List;

/**
 * 可以有选择项的底部弹出选择框
 * Created by liugd on 2017/6/12.
 */

public class BottomMenuSelectDialog extends BottomMenuDialog {

    private int selectIndex = -1;

    public BottomMenuSelectDialog(Context context) {
        super(context);
    }


    public BottomMenuSelectDialog setDatas(String[] strings, int selectIndex) {
        this.selectIndex = selectIndex;
        super.setDatas(strings);
        return this;
    }

    public BottomMenuSelectDialog setDatas(List<String> strings, int selectIndex) {
        this.selectIndex = selectIndex;
        super.setDatas(strings);
        return this;
    }

    public BottomMenuSelectDialog setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        adapter.notifyDataSetChanged();
        return this;
    }

    @Override
    public void onBind(BaseViewHolder holder, String item, int position) {
        TextView textView = holder.getView(R.id.textView);
        textView.setText(item);
        holder.getItemView().setId(position);
        holder.getItemView().setOnClickListener(this);
        ImageView imageView = holder.getView(R.id.imageView);
        imageView.setImageResource(selectIndex == position ? R.mipmap.checkbox_true : R.mipmap.nothing);

        holder.setVisibility(R.id.line1, position != 0 ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClick(View v) {
        dismiss();
        if(v.getId()==R.id.tv_cancel){
            return;
        }
        switch (v.getId()) {
            default://当不相同时才进行回调
                if (v.getId() != this.selectIndex) {
                    this.selectIndex = v.getId();
                    comCallBacks.call(this.selectIndex);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }


    @Override
    public int getItemId() {
        return R.layout.layout_base_item_bottom_menu_select;
    }
}
