package com.hd.permission;//package com.example.test1.fragmentadd.permission;
//
//import android.app.Activity;
//import android.os.Build;
//
//import com.example.test1.fragmentadd.IPermissionCallback;
//import com.example.test1.fragmentadd.OnPermissionCallback;
//import com.tbruyelle.rxpermissions.Permission;
//import com.tbruyelle.rxpermissions.RxPermissions;
//
//import java.util.function.Consumer;
//
//import rx.Observable;
//
///**
// * <p>Created by liugd on 2018/4/4.<p>
// * <p>佛祖保佑，永无BUG<p>
// */
//
//public class PermissionManager {
//
//    public static void requestEach(Activity activity, final OnPermissionCallback permissionCallback, final String... permissions) {
//        if (activity == null || permissionCallback == null) {
//            return;
//        }
//        if (permissions == null || permissions.length == 0) {
//            throw new IllegalArgumentException("permissions params is null");
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            RxPermissions rxPermissions = new RxPermissions(activity);
//            rxPermissions.requestEach(permissions).subscribe(permission -> {
//                if (permission.granted) {           // `permission.name` is granted !
//                    permissionCallback.onRequestAllow(permission.name);
//                } else if (permission.shouldShowRequestPermissionRationale) {    // Denied permission without ask never again
//                    permissionCallback.onRequestRefuse(permission.name);
//                } else { // Denied permission with ask never again
//                    // Need to go to the settings
//                    permissionCallback.onRequestNoAsk(permission.name);
//                }
//            });
//        } else {
//            //全部允许
//            for (String permission : permissions) {
//                permissionCallback.onRequestAllow(permission);
//            }
//        }
//    }
//
//    public static void request(Activity activity, final IPermissionCallback iPermissionCallback, final String... permissions) {
//        if (activity == null || iPermissionCallback == null) {
//            return;
//        }
//        if (permissions == null || permissions.length == 0) {
//            throw new IllegalArgumentException("permissions params is null");
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            RxPermissions rxPermissions = new RxPermissions(activity);
//            rxPermissions.request(permissions).subscribe(aBoolean -> {
//                if (aBoolean) {           // `permission.name` is granted !
//                    //申请的权限全部允许
//                    iPermissionCallback.onAllow();
//                } else {
//                    //iPermissionCallback，就会执行
//                    iPermissionCallback.onRefuse();
//                }
//            });
//        } else {
//            iPermissionCallback.onAllow();
//        }
//    }
//
//}
