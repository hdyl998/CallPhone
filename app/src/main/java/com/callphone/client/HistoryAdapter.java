package com.callphone.client;

import android.content.Context;
import android.view.View;

import com.hd.base.adapterbase.BaseViewHolder;
import com.hd.base.adapterbase.SuperAdapter;

import java.util.ArrayList;

/**
 * Created by liugd on 2019/1/18.
 */

public class HistoryAdapter extends SuperAdapter<HistoryItem>{
    public HistoryAdapter(Context context) {
        super(context,new ArrayList<>(), R.layout.item_history);
    }

    @Override
    protected void onBind(BaseViewHolder holder, HistoryItem item, int position) {
        holder.setOnClickListener(R.id.btnRetry,onRetryClickListener);
        holder.setTag(R.id.btnRetry,item);

        holder.setText(R.id.tvDate,item.date);
        holder.setText(R.id.tvPhone,item.targetPhone);
        holder.setText(R.id.tvStatus,item.isCallByHand?"手动拨打":"自动拨打");
    }


    View.OnClickListener onRetryClickListener;

    public void setOnRetryClickListener(View.OnClickListener onRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener;
    }
}
