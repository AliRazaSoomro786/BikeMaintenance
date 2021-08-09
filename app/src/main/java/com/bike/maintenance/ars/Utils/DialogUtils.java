package com.bike.maintenance.ars.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.bike.maintenance.ars.R;

public class DialogUtils {
    private final ProgressDialog mDialog;

    public DialogUtils(Context context) {
        mDialog = new ProgressDialog(context);
    }

    public void show(String message) {
        mDialog.setMessage(message);
        mDialog.show();
    }

    public void updateMessage(String message) {
        mDialog.setMessage(message);
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public void dismiss() {
        mDialog.dismiss();
    }
}
