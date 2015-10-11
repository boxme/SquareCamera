package com.desmond.squarecamera;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class RuntimePermissionActivity extends AppCompatActivity {

    public static final String REQUESTED_PERMISSION = "requested_permission";
    public static final int REQUEST_CODE = 1;

    public static void startActivity(final FragmentActivity activity,
                                     final int requestCode,
                                     final Permission requestedPermission) {
        final Intent intent = new Intent(activity, RuntimePermissionActivity.class);
        intent.putExtra(REQUESTED_PERMISSION, requestedPermission);
        activity.startActivityForResult(intent, requestCode);
    }

    public enum Permission {
        WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE
    }

    @Override
    protected void onStart() {
        super.onStart();
        setVisible(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Permission requestedPermission = (Permission) getIntent().getSerializableExtra(REQUESTED_PERMISSION);
        String permission = null;
        switch (requestedPermission) {
            case WRITE_EXTERNAL_STORAGE:
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;

            case READ_EXTERNAL_STORAGE:
                permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                break;

            default:
                break;
        }

        if (ContextCompat.checkSelfPermission(RuntimePermissionActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RuntimePermissionActivity.this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            sendResult(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                final int numOfRequest = grantResults.length;
                boolean isGranted = true;
                for (int i = 0; i < numOfRequest; i++) {
                    if (PackageManager.PERMISSION_GRANTED != grantResults[i]) {
                        isGranted = false;
                        break;
                    }
                }
                sendResult(isGranted);
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(RuntimePermissionActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RuntimePermissionActivity.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RuntimePermissionActivity.this.sendResult(false);
                    }
                })
                .create()
                .show();
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(RuntimePermissionActivity.this, new String[] {permission}, REQUEST_CODE);
    }

    private void sendResult(final boolean isPermissionGranted) {
        final Intent resultIntent = new Intent();
        resultIntent.putExtra(REQUESTED_PERMISSION, isPermissionGranted);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
