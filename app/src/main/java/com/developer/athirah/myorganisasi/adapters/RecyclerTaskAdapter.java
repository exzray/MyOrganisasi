package com.developer.athirah.myorganisasi.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.athirah.myorganisasi.DetailActivity;
import com.developer.athirah.myorganisasi.R;
import com.developer.athirah.myorganisasi.VolunteerActivity;
import com.developer.athirah.myorganisasi.models.ModelEvent;
import com.developer.athirah.myorganisasi.models.ModelTask;
import com.developer.athirah.myorganisasi.utilities.EventUtils;

import java.util.ArrayList;
import java.util.List;

public class RecyclerTaskAdapter extends RecyclerView.Adapter<RecyclerTaskAdapter.VH> {

    private List<ModelTask> tasks;
    private ModelEvent event;


    public RecyclerTaskAdapter(List<ModelTask> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        ModelTask task = tasks.get(i);

        // bind data
        vh.title.setText(task.getTitle());
        vh.description.setText(task.getDescription());

        // bind data that require another model
        if (event != null) {
            EventUtils utils = EventUtils.getInstance(vh.itemView.getContext(), event);

            // bind special data
            vh.setData(event, task);
            utils.updateLabelTotal(task.getUid(), task.getLimit(), vh.total);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateEvent(ModelEvent event) {
        this.event = event;

        notifyDataSetChanged();
    }


    class VH extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private ModelEvent event;
        private ModelTask task;

        private TextView title, description, total;
        private MenuItem add, remove;

        private VH(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.itemtask_title);
            description = itemView.findViewById(R.id.itemtask_description);
            total = itemView.findViewById(R.id.itemtask_total);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
            final EventUtils utils = EventUtils.getInstance(v.getContext(), event);
            final DetailActivity activity = (DetailActivity) v.getContext();

            add = menu.add(Menu.NONE, 1, 1, "Tambah sukarelawan");
            add.setVisible(utils.getTask(task.getUid()).size() < task.getLimit());
            add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // get list people do this task
                    List<String> doing = utils.getTask(task.getUid());

                    // get list people that join this event
                    List<String> joining = event.getPeople();

                    // make they different
                    List<String> list = new ArrayList<>();

                    for (String s : joining) {
                        if (!doing.contains(s)) list.add(s);
                    }

                    String[] a = new String[list.size()];
                    list.toArray(a);

                    Intent intent = new Intent(activity, VolunteerActivity.class);
                    intent.putExtra(VolunteerActivity.EXTRA_EVENT_UID, event.getUid());
                    intent.putExtra(VolunteerActivity.EXTRA_TASK_PEOPLE, a);
                    intent.putExtra(VolunteerActivity.EXTRA_TASK_UID, task.getUid());

                    activity.startActivityForResult(intent, 1);

                    return true;
                }
            });


            remove = menu.add(Menu.NONE, 2, 2, "Buang sukarelawan");
            remove.setVisible(utils.getTask(task.getUid()).size() > 0);
            remove.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String[] a = new String[utils.getTask(task.getUid()).size()];
                    utils.getTask(task.getUid()).toArray(a);

                    Intent intent = new Intent(activity, VolunteerActivity.class);
                    intent.putExtra(VolunteerActivity.EXTRA_EVENT_UID, event.getUid());
                    intent.putExtra(VolunteerActivity.EXTRA_TASK_UID, task.getUid());
                    intent.putExtra(VolunteerActivity.EXTRA_TASK_PEOPLE, a);

                    activity.startActivityForResult(intent, 2);

                    return true;
                }
            });

            remove = menu.add(Menu.NONE, 3, 3, "Senarai sukarelawan");
            remove.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String[] a = new String[utils.getTask(task.getUid()).size()];
                    utils.getTask(task.getUid()).toArray(a);

                    Intent intent = new Intent(activity, VolunteerActivity.class);
                    intent.putExtra(VolunteerActivity.EXTRA_TASK_PEOPLE, a);

                    activity.startActivityForResult(intent, 3);

                    return true;
                }
            });
        }

        private void setData(ModelEvent event, ModelTask task) {
            this.event = event;
            this.task = task;

            itemView.setOnCreateContextMenuListener(this);
        }
    }
}
