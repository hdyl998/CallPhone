package com.callphone.client.home;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.callphone.client.R;
import com.callphone.client.base.AppConstants;
import com.callphone.client.home.detail.CallInfoFragment;
import com.callphone.client.home.socket.MsgSocket;
import com.callphone.client.main.bean.EventItem;
import com.callphone.client.main.mine.LoginManager;
import com.callphone.client.utils.StringUtil;
import com.hd.base.adapterbase.BaseViewHolder;
import com.hd.base.adapterbase.SuperAdapter;
import com.hd.base.fragment.IBasePullListViewFragment;
import com.hd.net.NetBuilder;
import com.hd.net.socket.NetEntity;
import com.hd.permission.PermissionCallback;
import com.hd.permission.PermissionHelper;
import com.hd.utils.GoUtils;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;
import com.hd.utils.log.impl.LogUitls;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by liugd on 2019/2/26.
 */

public class HomeFragment extends IBasePullListViewFragment<CallInfoItem> {

    @MyBindView(R.id.tvBroadcast)
    TextView tvBroadcast;
    @MyBindView(R.id.tvSocketStatus)
    TextView tvSocketStatus;




    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initTitleBarListView() {
        MyBufferKnifeUtils.inject(this);
        createService();
    }

    @Override
    protected void setAPIRequest() {
        if (LoginManager.isLogin()) {
            listView.getEmptyLayout().setEmptyMessage(getString(R.string.empty_message));
        }
        CallInfoItem item = getTailItem();

        NetBuilder.create(mContext)
                .add2Url("id", item == null ? "" : item.id)
                .start("getLog", this);
    }

    @Override
    protected void onItemClickSafely(CallInfoItem item, int position) {
        startFragment(CallInfoFragment.class, item);
    }

    @Override
    public void onSuccess(NetEntity entity) throws Exception {
        super.onSuccess(entity);
        //获取成功直接开启socket
    }

    @Override
    public void onError(NetEntity entity) throws Exception {
        if (entity.isLoginError()) {
            listView.getEmptyLayout().setEmptyMessage("登录后查看数据");
            listView.showEmpty();
            listView.setDataFromNetWork(null);
        } else {
            super.onError(entity);
        }
    }

    @Override
    protected boolean isEventBus() {
        return true;
    }


    /***
     * 当用户登录时
     */
    @Subscribe
    public void onUserLogin(EventItem.LoginEvent item) {
        setAPIRequest();
        if (binder != null) {
            binder.startLooper();
        }
    }

    /***
     * 当用户登录时
     */
    @Subscribe
    public void onUserLogout(EventItem.LoginOutEvent item) {
        listData.clear();
        adapter.notifyDataSetChanged();
        listView.getEmptyLayout().setEmptyMessage("登录后查看数据");
        listView.showEmpty();

        if (binder != null) {
            binder.stopLooper();;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unbindService(getConn());
    }

    @Override
    public int setAdapterLayoutID() {
        return R.layout.item_call_history;
    }


    @Override
    protected void onAdapterBind(BaseViewHolder holder, CallInfoItem item, int position) {
        holder.setText(R.id.tvDate, StringUtil.getPostTime(item.updatetime));
        holder.setText(R.id.tvPhone, item.phone);
        holder.setText(R.id.tvStatus, item.getStatusString());
    }

    private void createService() {
        Intent intent = new Intent(mContext, LoopService.class);
        mContext.bindService(intent, getConn(), Service.BIND_AUTO_CREATE);
    }


    @Override
    protected BaseAdapter setBaseAdapter(Context mContext, List<CallInfoItem> listData) {
        return super.setBaseAdapter(mContext, listData);
    }

    LoopService mService;
    ServiceConnection conn = null;
    LoopService.LocalBinder binder;

    public ServiceConnection getConn() {
        if (conn == null) {
            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUitls.print(TAG, "绑定成功调用：onServiceConnected");
                    binder = (LoopService.LocalBinder) service;

                    binder.setTvInfo(tvSocketStatus);
                    binder.setAdapter((SuperAdapter<CallInfoItem>) adapter);
                    mService = binder.getService();

                    if(LoginManager.isLogin()){
                        binder.startLooper();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mService = null;
                }
            };
        }
        return conn;
    }


}
