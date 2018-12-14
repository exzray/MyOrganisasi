package com.developer.athirah.myorganisasi.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.athirah.myorganisasi.R;
import com.developer.athirah.myorganisasi.VolunteerActivity;
import com.developer.athirah.myorganisasi.models.ModelProfile;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerVolunteerAdapter extends RecyclerView.Adapter<RecyclerVolunteerAdapter.VH> {

    private List<ModelProfile> list;
    private boolean showSelectButton = false;


    public RecyclerVolunteerAdapter(List<ModelProfile> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_volunteer, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        final ModelProfile profile = list.get(i);

        // bind data
        vh.name.setText(profile.getName());
        vh.contact.setText(profile.getUid());

        Glide.with(vh.itemView).load("https://images.unsplash.com/photo-1506158981101-17d5fadfa720?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80").into(vh.image);

        vh.select.setVisibility(showSelectButton ? View.VISIBLE : View.GONE);
        vh.select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VolunteerActivity activity = (VolunteerActivity) v.getContext();
                activity.editMember(profile.getUid());
            }
        });
    }

    public void setSelectVisibility(boolean state) {
        showSelectButton = state;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VH extends RecyclerView.ViewHolder {

        private CircleImageView image;
        private TextView name, contact;
        private Button select;

        private VH(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.itemvolunteer_image);
            name = itemView.findViewById(R.id.itemvolunteer_name);
            contact = itemView.findViewById(R.id.itemvolunteer_contact);
            select = itemView.findViewById(R.id.itemvolunteer_select);
        }
    }
}
