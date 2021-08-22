package com.bike.maintenance.ars.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bike.maintenance.ars.Model.Bookings;
import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.bike.maintenance.ars.Utils.Helper;
import com.google.firebase.database.FirebaseDatabase;
import com.jackandphantom.circularimageview.CircleImage;

import java.util.HashMap;
import java.util.List;


public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.MyViewHolder> {
    private final List<Bookings> bookings;

    public BookingsAdapter(List<Bookings> bookings) {
        this.bookings = bookings;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mName, mStauts, description, actionCancel, mDateTime;
        private CircleImage mProfile;

        public MyViewHolder(View v) {
            super(v);
            try {
                mName = v.findViewById(R.id.buyerName);
                mStauts = v.findViewById(R.id.previewStatus);
                mProfile = v.findViewById(R.id.buyerImage);
                description = v.findViewById(R.id.buyerDescription);
                actionCancel = v.findViewById(R.id.actionCancel_accept);
                mDateTime = v.findViewById(R.id.previewDateTime);
            } catch (Exception e) {
                System.out.println("Exception Screen : StudentAdapter" + e.toString());
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder vh = null;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_orders, parent, false);
        vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Bookings item = bookings.get(position);

        if (item == null) return;

        holder.mName.setText(item.getName());
        holder.mStauts.setText(item.isStatus() ? "Accepted" : "In-progress");

        String description = "Company : " + item.getCompany() + "\n" + "Power engine : " + item.getPowerengine()
                + "\n Description : " + item.getRepairdescription();

        holder.description.setText(description);
        holder.mDateTime.setText(item.getTimestamp());

        if (Helper.userType.equals(AppConstant.MECHANIC)) {
            holder.actionCancel.setText("Accept");
        } else {
            holder.actionCancel.setText("Cancel");
        }

        if (holder.mStauts.getText().toString().equals("Accepted")) {
            holder.actionCancel.setText("Cancel");
        }

        holder.actionCancel.setOnClickListener(v -> {
            if (holder.actionCancel.getText().toString().equals("Cancel")) {
                if (Helper.userType.equals(AppConstant.MECHANIC)) {

                    acceptRejectOffers(v.getContext(), () -> {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("status", false);
                        FirebaseDatabase.getInstance().getReference()
                                .child(AppConstant.BOOKING_REQUESTS)
                                .child(item.getKey())
                                .updateChildren(hashMap);

                    }, "Do you can to cancel request ?");


                } else {
                    acceptRejectOffers(v.getContext(), () -> {
                        FirebaseDatabase.getInstance().getReference()
                                .child(AppConstant.BOOKING_REQUESTS)
                                .child(item.getKey()).removeValue();
                        bookings.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(v.getContext(), "Request Canceled Successfully ...", Toast.LENGTH_SHORT).show();
                    }, "Do you can to cancel request ?");


                }

            } else if (holder.actionCancel.getText().toString().equals("Accept")) {
                acceptRejectOffers(v.getContext(), () -> {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("status", true);
                    FirebaseDatabase.getInstance().getReference()
                            .child(AppConstant.BOOKING_REQUESTS)
                            .child(item.getKey())
                            .updateChildren(hashMap);
                }, "Do you want to Accept request ?");


            }
        });


    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }


    public interface DialogListener {
        void onYes();

    }

    private void acceptRejectOffers(Context activity, DialogListener listener, String msg) {
        final View v = LayoutInflater.from(activity).inflate(R.layout.custom_accept_offer_diaog, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        AlertDialog dialog = alertDialog.create();

        TextView message = v.findViewById(R.id.dilaogMessage);
        message.setText(msg);

        v.findViewById(R.id.actionNo).setOnClickListener(v1 -> {
            dialog.dismiss();
        });

        v.findViewById(R.id.actionYes).setOnClickListener(v12 -> {
            dialog.dismiss();
            listener.onYes();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setView(v);
        dialog.show();
    }


}


