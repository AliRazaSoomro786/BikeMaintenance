package com.bike.maintenance.ars.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

public class PermissionHelper {
    public static final int PERMISSION_CODE = 1002;
    private final Activity mContext;

    final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public PermissionHelper(Activity context) {
        this.mContext = context;
    }

    public boolean isGranted() {
        for (String str : permissions)
            if (mContext.checkSelfPermission(str) == PackageManager.PERMISSION_DENIED)
                return false;
        return true;
    }

    public void request() {
        if (!isGranted())
            mContext.requestPermissions(permissions, PERMISSION_CODE);
    }
}
