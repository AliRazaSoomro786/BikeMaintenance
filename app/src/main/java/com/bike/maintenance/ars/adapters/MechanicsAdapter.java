package com.bike.maintenance.ars.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bike.maintenance.ars.Model.Mechanic;
import com.bike.maintenance.ars.R;

import java.util.List;


public class MechanicsAdapter extends RecyclerView.Adapter<MechanicsAdapter.ViewHolder> {
    private final ItemClickListener listener;
    private final List<Mechanic> mechanics;

    public MechanicsAdapter(List<Mechanic> mechanics, ItemClickListener listener) {
        this.mechanics = mechanics;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mechanic_info_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mechanic item = mechanics.get(position);

        if (item == null) return;

        holder.mName.setText(item.getName());
        holder.mPhone.setText(item.getPhone());
        holder.mAddress.setText("Location : lat : lng " + item.getLat() + " : " + item.getLng());

        holder.actionMessage.setOnClickListener(v -> {
        });
        holder.actionCall.setOnClickListener(v -> {
        });
        holder.actionSendRequest.setOnClickListener(v -> {
            listener.onItemClick(position);
        });

    }

    @Override
    public int getItemCount() {
        return mechanics.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mName, mPhone, mAddress, actionSendRequest;
        private final ImageView actionCall, actionMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.custom_dialoge_tv_name);
            mPhone = itemView.findViewById(R.id.custom_dialoge_tv_phone);
            mAddress = itemView.findViewById(R.id.custom_dialoge_tv_address);
            actionSendRequest = itemView.findViewById(R.id.actionSendRequest);
            actionCall = itemView.findViewById(R.id.custom_dialoge_call_icon);
            actionMessage = itemView.findViewById(R.id.custom_dialoge_msg_icon);

        }
    }
}
