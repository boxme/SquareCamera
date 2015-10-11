package com.desmond.squarecamera;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class RuntimePermissionActivity extends AppCompatActivity {

    public static final String REQUESTED_PERMISSION = "requested_permission";
    private static final int REQUEST_CODE = 1;

    public static void startActivity(@NonNull final Fragment fragment,
                                     final int requestCode,
                                     @NonNull final String requestedPermission,
                                     final String... permissions) {

        final Intent intent = new Intent(fragment.getActivity(), RuntimePermissionActivity.class);

        final int capacity = 1 + (permissions != null ? permissions.length : 0);
        final ArrayList<String> requestedPermissions = new ArrayList<>(capacity);
        requestedPermissions.add(requestedPermission);
        if (permissions != null) {
            requestedPermissions.addAll(Arrays.asList(permissions));
        }

        intent.putStringArrayListExtra(REQUESTED_PERMISSION, requestedPermissions);
        fragment.startActivityForResult(intent, requestCode);
    }

    /* https://code.google.com/p/android-developer-preview/issues/detail?id=2353 */
    @Override
    protected void onStart() {
        super.onStart();
        setVisible(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ArrayList<String> reqPermissions = getIntent().getStringArrayListExtra(REQUESTED_PERMISSION);
        final ArrayList<String> permissionsNeeded = getPermissionNeeded(reqPermissions);
        final ArrayList<String> permissionRationaleNeeded = getPermissionRationaleNeeded(permissionsNeeded);

        if (!permissionRationaleNeeded.isEmpty()) {
            String message = getString(R.string.squarecamera__request_write_storage_permission_text);
            for (int i = 1; i < permissionRationaleNeeded.size(); ++i) {
                message += ", " + permissionRationaleNeeded.get(i);
            }

            showPermissionRationaleDialog(message, permissionsNeeded.toArray(new String[permissionsNeeded.size()]));
        } else if (!permissionsNeeded.isEmpty()) {
            requestForPermission(permissionsNeeded.toArray(new String[permissionsNeeded.size()]));
        } else {
            sendResult(true);
        }
    }

    private ArrayList<String> getPermissionNeeded(@NonNull final ArrayList<String> reqPermissions) {
        final ArrayList<String> permissionNeeded = new ArrayList<>(reqPermissions.size());

        for (String reqPermission : reqPermissions) {
            if (ContextCompat.checkSelfPermission(RuntimePermissionActivity.this, reqPermission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionNeeded.add(reqPermission);
            }
        }

        return permissionNeeded;
    }

    private ArrayList<String> getPermissionRationaleNeeded(@NonNull final ArrayList<String> permissionsNeeded) {
        final ArrayList<String> rationaleNeeded = new ArrayList<>(permissionsNeeded.size());

        for (String permissionNeeded : permissionsNeeded) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    RuntimePermissionActivity.this, permissionNeeded)) {
                rationaleNeeded.add(permissionNeeded);
            }
        }

        return rationaleNeeded;
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

    private void showPermissionRationaleDialog(final String message, final String[] permissions) {
        new AlertDialog.Builder(RuntimePermissionActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RuntimePermissionActivity.this.requestForPermission(permissions);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RuntimePermissionActivity.this.sendResult(false);
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        RuntimePermissionActivity.this.sendResult(false);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        RuntimePermissionActivity.this.sendResult(false);
                    }
                })
                .create()
                .show();
    }

    private void requestForPermission(final String[] permissions) {
        ActivityCompat.requestPermissions(RuntimePermissionActivity.this, permissions, REQUEST_CODE);
    }

    private void sendResult(final boolean isPermissionGranted) {
        final Intent resultIntent = new Intent();
        resultIntent.putExtra(REQUESTED_PERMISSION, isPermissionGranted);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
