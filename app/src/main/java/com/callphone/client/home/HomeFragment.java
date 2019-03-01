package com.callphone.client.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.callphone.client.AlarmHandlerActivity;
import com.callphone.client.HistoryItem;
import com.callphone.client.LoopService;
import com.callphone.client.PhoneSocket;
import com.callphone.client.R;
import com.callphone.client.ScreenListener;
import com.callphone.client.base.AppConstants;
import com.callphone.client.home.detail.CallInfoFragment;
import com.callphone.client.home.socket.MsgSocket;
import com.callphone.client.main.bean.EventItem;
import com.callphone.client.main.mine.LoginManager;
import com.callphone.client.utils.OSHelper;
import com.callphone.client.utils.StringUtil;
import com.hd.base.adapterbase.BaseViewHolder;
import com.hd.base.adapterbase.SuperAdapter;
import com.hd.base.fragment.IBasePullListViewFragment;
import com.hd.net.NetBuilder;
import com.hd.net.socket.NetEntity;
import com.hd.net.socket.SocketMessageListener;
import com.hd.permission.PermissionCallback;
import com.hd.permission.PermissionHelper;
import com.hd.utils.DateUtils;
import com.hd.utils.GoUtils;
import com.hd.utils.TextViewUtils;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.toast.ToastUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.List;

/**
 * Created by liugd on 2019/2/26.
 */

public class HomeFragment extends IBasePullListViewFragment<CallInfoItem> {

    @MyBindView(R.id.tvBroadcast)
    TextView tvBroadcast;
    @MyBindView(R.id.ivNext)
    View ivNext;


    boolean isPermissionOK = false;

    private void checkPermission() {
        tvBroadcast.setText("请授权...");
        ivNext.setVisibility(View.GONE);
        PermissionHelper.create(mContext).setPermissions(AppConstants.permissionStart)
                .request(new PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {
                        tvBroadcast.setText("权限授权成功");
                        isPermissionOK = true;
                        ivNext.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPermissionDenied(List<String> permissions) {
                        super.onPermissionDenied(permissions);
                        tvBroadcast.setText(String.format("缺少必要权限，请点击\"设置\"-\"权限\"-打开以下权限:\n%s", listPermissions2String(permissions)));
                        tvBroadcast.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GoUtils.goAppDetailsSetting(mContext);
                            }
                        });
                        ivNext.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initTitleBarListView() {
        MyBufferKnifeUtils.inject(this);
        checkPermission();
        createService();
    }

    @Override
    protected void setAPIRequest() {
        if (LoginManager.isLogin()) {
            listView.getEmptyLayout().setEmptyMessage(getString(R.string.empty_message));
        }
        CallInfoItem item = getTailItem();

        NetBuilder.create(mContext)
                .add2Post("id", item == null ? "" : item.id)
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
            listView.getEmptyLayout().setEmptyMessage(entity.getCodeString());
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
        MsgSocket.getInstance().sendMyInfo();
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
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MsgSocket.getInstance().onDestory();
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

    public ServiceConnection getConn() {
        if (conn == null) {
            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUitls.print(TAG, "绑定成功调用：onServiceConnected");
                    LoopService.LocalBinder binder = (LoopService.LocalBinder) service;
                    binder.setTvInfo(tvBroadcast);
                    binder.setAdapter((SuperAdapter<CallInfoItem>) adapter);
                    mService = binder.getService();
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
