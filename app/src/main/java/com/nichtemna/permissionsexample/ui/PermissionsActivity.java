package com.nichtemna.permissionsexample.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.nichtemna.permissionsexample.R;
import com.nichtemna.permissionsexample.utils.PermissionChecker;

public class PermissionsActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CODE = 0;
    public static final int RESULT_CODE_ALL_GRANTED = 1;
    public static final int RESULT_CODE_PERMISSIONS_DENIED = 2;
    private static final String EXTRA_PERMISSIONS = "com.nichtemna.permissions.EXTRA_PERMISSIONS";
    private static final String EXTRA_FINISH = "com.nichtemna.permissions.EXTRA_FINISH";
    private static final String PACKAGE_URL_SCHEME = "package:";

    private PermissionChecker permissionChecker;
    private boolean requiresCheck;

    public static void startActivityForResult(Activity activity, int requestCode, String[] permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("This activity must be started using startActivityForResult method");
        }
        setContentView(R.layout.activity_permissions);

        permissionChecker = new PermissionChecker(this);
        requiresCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requiresCheck) {
            String[] permissions = getPermissions();
            if (permissionChecker.lackPermissions(permissions)) {
                requestPermissions(permissions);
            } else {
                allPermissionsGranted();
            }
        }
    }

    private void allPermissionsGranted() {
        setResult(RESULT_CODE_ALL_GRANTED);
        finish();
    }

    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            requiresCheck = true;
            allPermissionsGranted();
        } else {
            requiresCheck = false;
            showMissingPermissionsDialog();
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResult) {
        for (int result : grantResult) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    private void showMissingPermissionsDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PermissionsActivity.this);
        dialogBuilder.setTitle(R.string.help);
        dialogBuilder.setMessage(R.string.string_help_text);
        dialogBuilder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_CODE_PERMISSIONS_DENIED);
                finish();
            }
        });
        dialogBuilder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });
        dialogBuilder.show();
    }

    private void startAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
}
