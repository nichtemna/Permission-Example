package com.nichtemna.permissionsexample.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Lina Shyshova on 27.04.16.
 */
public class PermissionChecker {
    private final Context context;

    public PermissionChecker(Context context) {
        this.context = context;
    }

    public boolean lackPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lackPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean lackPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }
}
