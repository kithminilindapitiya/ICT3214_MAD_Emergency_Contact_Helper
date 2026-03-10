package com.example.emergencycontacthelper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.mdp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    // ─────────────────────────────────────────────
    // Variables
    // ─────────────────────────────────────────────
    Context context;
    List<EmergencyContact> contactList;
    OnCallClickListener callClickListener;

    // Pastel avatar colors — cycles through for each contact
    String[] avatarColors = { "#FFB3B3", "#B3D4FF", "#B3F0D4", "#FFE0B3", "#E8B3FF" };

    // ─────────────────────────────────────────────
    // Interface — for call button click
    // ─────────────────────────────────────────────
    public interface OnCallClickListener {
        void onCallClick(EmergencyContact contact);
    }

    // ─────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────
    public ContactAdapter(Context context, List<EmergencyContact> contactList, OnCallClickListener listener) {
        this.context           = context;
        this.contactList       = contactList;
        this.callClickListener = listener;
    }

    // ─────────────────────────────────────────────
    // Create each row view from item_contact.xml
    // ─────────────────────────────────────────────
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    // ─────────────────────────────────────────────
    // Fill each row with contact data
    // ─────────────────────────────────────────────
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        EmergencyContact contact = contactList.get(position);

        // Set name, relation, phone
        holder.tvName.setText(contact.getName());
        holder.tvRelation.setText(contact.getRelation());
        holder.tvPhone.setText(contact.getPhone());

        // Avatar = first letter of name  e.g. "Mom" → "M"
        String firstLetter = String.valueOf(contact.getName().charAt(0)).toUpperCase();
        holder.tvAvatar.setText(firstLetter);

        // Set pastel background color — cycles through colors list
        String color = avatarColors[position % avatarColors.length];
        holder.tvAvatar.setBackgroundColor(Color.parseColor(color));

        // Call button click
        holder.btnCall.setOnClickListener(v -> callClickListener.onCallClick(contact));
    }

    // ─────────────────────────────────────────────
    // Total number of contacts
    // ─────────────────────────────────────────────
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    // ─────────────────────────────────────────────
    // ViewHolder — holds references to each view
    // in the item_contact.xml row
    // ─────────────────────────────────────────────
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
