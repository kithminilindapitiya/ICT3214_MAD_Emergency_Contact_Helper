package com.example.emergencycontacthelper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private List<EmergencyContact> contactList;
    private OnCallClickListener callClickListener;

    // Pastel avatar colors — cycles through for each contact
    private String[] avatarColors = { "#FFB3B3", "#B3D4FF", "#B3F0D4", "#FFE0B3", "#E8B3FF" };

    public interface OnCallClickListener {
        void onCallClick(EmergencyContact contact);
    }

    public ContactAdapter(Context context, List<EmergencyContact> contactList, OnCallClickListener listener) {
        this.context           = context;
        this.contactList       = contactList;
        this.callClickListener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        EmergencyContact contact = contactList.get(position);

        holder.tvName.setText(contact.getName());
        holder.tvRelation.setText(contact.getRelation());
        holder.tvPhone.setText(contact.getPhone());

        if (contact.getName() != null && !contact.getName().isEmpty()) {
            String firstLetter = String.valueOf(contact.getName().charAt(0)).toUpperCase();
            holder.tvAvatar.setText(firstLetter);
        }

        String color = avatarColors[position % avatarColors.length];
        holder.tvAvatar.setBackgroundColor(Color.parseColor(color));

        holder.btnCall.setOnClickListener(v -> {
            if (callClickListener != null) {
                callClickListener.onCallClick(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatar, tvName, tvRelation, tvPhone, btnCall;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatar   = itemView.findViewById(R.id.tvContactAvatar);
            tvName     = itemView.findViewById(R.id.tvContactName);
            tvRelation = itemView.findViewById(R.id.tvContactRelation);
            tvPhone    = itemView.findViewById(R.id.tvContactPhone);
            btnCall    = itemView.findViewById(R.id.btnCall);
        }
    }
}
