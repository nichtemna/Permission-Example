package com.nichtemna.permissionsexample.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nichtemna.permissionsexample.R;
import com.nichtemna.permissionsexample.utils.PermissionChecker;

public class MainActivity extends AppCompatActivity {
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS};
    private PermissionChecker permissionChecker;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        permissionChecker = new PermissionChecker(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionChecker.lackPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, PermissionsActivity.PERMISSION_REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionsActivity.PERMISSION_REQUEST_CODE && resultCode == PermissionsActivity.RESULT_CODE_PERMISSIONS_DENIED) {
            finish();
        }
    }
}
