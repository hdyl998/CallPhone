package com.hd.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import com.hd.base.HdApp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class PermissionHelper {

    public String permissions[];
    public PermissionCallback permissionCallback;


    public Activity activity;

    public static PermissionHelper create(Context mContext) {
        return new PermissionHelper(mContext);
    }


    private PermissionHelper(Context mContext) {
        this.activity = (Activity) mContext;
    }


    public PermissionHelper setPermissions(String... permissions) {
        this.permissions = permissions;
        return this;
    }


    /***
     * 无回调
     */
    public void request() {
        request(null);
    }


    public void request(PermissionCallback callback) {
        if (activity == null) {
            return;
        }
        this.permissionCallback = callback;
        if (callback != null)
            callback.setContext(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(activity);
            rxPermissions.requestListPermissons(permissions).subscribe(lists -> {
                handCallback(lists);
            });
        } else {
            if (callback != null)
                callback.onPermissionGranted();
        }
    }

    /***
     * 处理权限回调
     * @param lists
     */
    private void handCallback(List<Permission> lists) {
        if (permissionCallback == null) {
            return;
        }
        List<String> listDenied = new ArrayList<>(lists.size());
        for (Permission p : lists) {
            if (!p.granted) {
                listDenied.add(p.name);
            }
        }
        if (listDenied.isEmpty()) {
            try {
                permissionCallback.onPermissionGranted();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            permissionCallback.onPermissionDenied(listDenied);
        }
    }


    public static boolean hasPermissions(String... perms) {
        // Always return true for SDK < M, let the system deal with the permissions
        //        return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (HdApp.getContext().getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {
            for (String perm : perms) {
                if (ContextCompat.checkSelfPermission(HdApp.getContext(), perm)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        } else {
            // targetSdkVersion < Android M, we have to use PermissionChecker
            for (String perm : perms) {
                return PermissionChecker.checkSelfPermission(HdApp.getContext(), perm)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return true;
    }

}
