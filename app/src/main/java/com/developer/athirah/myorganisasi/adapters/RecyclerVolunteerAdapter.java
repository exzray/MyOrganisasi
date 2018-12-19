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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerVolunteerAdapter extends RecyclerView.Adapter<RecyclerVolunteerAdapter.VH> {

    private List<ModelProfile> list;
    private List<String> picture;
    private boolean showSelectButton = false;


    public RecyclerVolunteerAdapter(List<ModelProfile> list) {
        this.list = list;

        picture = new ArrayList<>();
        picture.add("https://images.unsplash.com/photo-1527153907022-465ee4752fdc?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1300&q=80");
        picture.add("https://images.unsplash.com/photo-1506158981101-17d5fadfa720?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80");
        picture.add("https://images.unsplash.com/photo-1529932260967-af9d3bbd8138?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80");
        picture.add("https://images.unsplash.com/photo-1530268729831-4b0b9e170218?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80");
        picture.add("https://images.unsplash.com/photo-1486649567693-aaa9b2e59385?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80");
        picture.add("https://images.unsplash.com/photo-1496345875659-11f7dd282d1d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80");
        picture.add("https://images.unsplash.com/photo-1463453091185-61582044d556?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80");
        picture.add("https://images.unsplash.com/photo-1500561607578-e542f3149b97?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80");
        picture.add("https://images.unsplash.com/photo-1489779162738-f81aed9b0a25?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1382&q=80");
        picture.add("https://images.unsplash.com/photo-1519295918781-d590afd8e95d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80");
        picture.add("https://images.unsplash.com/photo-1520881363902-a0ff4e722963?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80");
        picture.add("https://images.unsplash.com/photo-1496275068113-fff8c90750d1?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80");
        picture.add("https://images.unsplash.com/photo-1504254342572-aa8c19155918?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80");
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_volunteer, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        Random random = new Random();

        final ModelProfile profile = list.get(i);

        // bind data
        vh.name.setText(profile.getName());
        vh.contact.setText(profile.getUid());

        Glide.with(vh.itemView).load(picture.get(random.nextInt(picture.size()))).into(vh.image);

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
