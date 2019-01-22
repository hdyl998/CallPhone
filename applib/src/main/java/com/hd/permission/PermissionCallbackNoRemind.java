package com.hd.permission;

import java.util.List;

/**
 * 权限回调,没有提示
 * <p>Created by Administrator on 2018/9/10.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public abstract class PermissionCallbackNoRemind extends PermissionCallback {

    @Override
    public void onPermissionDenied(List<String> permissions) {
    }
}
