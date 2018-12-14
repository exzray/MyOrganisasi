package com.developer.athirah.myorganisasi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.athirah.myorganisasi.DetailActivity;
import com.developer.athirah.myorganisasi.R;
import com.developer.athirah.myorganisasi.models.ModelEvent;

import java.util.List;

public class RecyclerEventAdapter extends RecyclerView.Adapter<RecyclerEventAdapter.VH> {

    private List<ModelEvent> events;

    public RecyclerEventAdapter(List<ModelEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        final ModelEvent event = events.get(i);

        // bind data
        vh.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Context context = v.getContext();

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_UID, event.getUid());
                context.startActivity(intent);
            }
        });

        Glide.with(vh.itemView).load(event.getImage()).into(vh.image);

        vh.title.setText(event.getTitle());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class VH extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title;

        private VH(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.itemevent_image);
            title = itemView.findViewById(R.id.itemevent_title);
        }
    }
}
