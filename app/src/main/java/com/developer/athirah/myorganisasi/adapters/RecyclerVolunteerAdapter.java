package com.developer.athirah.myorganisasi.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.developer.athirah.myorganisasi.R;
import com.developer.athirah.myorganisasi.VolunteerActivity;
import com.developer.athirah.myorganisasi.models.ModelProfile;

import java.util.List;

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

        vh.select.setVisibility(showSelectButton ? View.VISIBLE : View.GONE);
        vh.select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VolunteerActivity activity = (VolunteerActivity) v.getContext();
                activity.editMember(profile.getUid());
            }
        });

        vh.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {

                MenuItem call = menu.add("Call");
                call.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+profile.getUid()));
                        v.getContext().startActivity(intent);

                        return true;
                    }
                });

                MenuItem whatsapp = menu.add("Whatsapp");
                whatsapp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        String url = "https://api.whatsapp.com/send?phone=" + profile.getUid();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        v.getContext().startActivity(i);

                        return true;
                    }
                });
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

        private TextView name, contact;
        private Button select;

        private VH(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.itemvolunteer_name);
            contact = itemView.findViewById(R.id.itemvolunteer_contact);
            select = itemView.findViewById(R.id.itemvolunteer_select);
        }
    }
}
