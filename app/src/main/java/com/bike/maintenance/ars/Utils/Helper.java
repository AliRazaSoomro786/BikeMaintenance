package com.bike.maintenance.ars.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Helper {
    public static  String userType = "";

    public static void sendMessage(String phone, Context context) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);

            i.putExtra("address", phone);
            i.putExtra("sms_body", "Aslam O Alaikum");
            i.setType("vnd.android-dir/mms-sms");
            context.startActivity(i);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void makeCall(String phone, Context context) {
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setData(Uri.parse("tel:" + phone));
        context.startActivity(call);
    }
}
