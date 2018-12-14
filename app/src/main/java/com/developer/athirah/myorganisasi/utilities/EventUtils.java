package com.developer.athirah.myorganisasi.utilities;

import android.content.Context;
import android.widget.TextView;

import com.developer.athirah.myorganisasi.R;
import com.developer.athirah.myorganisasi.models.ModelEvent;

import java.util.ArrayList;
import java.util.List;

public class EventUtils {

    private Context context;
    private ModelEvent event;

    private EventUtils(Context context, ModelEvent event) {
        this.context = context;
        this.event = event;
    }

    public static EventUtils getInstance(Context context, ModelEvent event){
        return new EventUtils(context, event);
    }

    public List<String> getTask(String uid){
        List<String> tasks = event.getTask().get(uid);

        if (tasks == null) tasks = new ArrayList<>();

        return tasks;
    }

    public void updateLabelTotal(String uid, int limit, TextView text){
        int size = getTask(uid).size();
        String str = "(" + size + "/" + limit + ")";

        // to reset label
        text.setTextColor(context.getResources().getColor(R.color.grey_600));

        if (size == 0) {
            str = "Tiada penyertaan " + str ;
            text.setTextColor(context.getResources().getColor(R.color.red_500));
        }
        else if (size == limit) {
            str = "Tugasan penuh " + str;
            text.setTextColor(context.getResources().getColor(R.color.green_500));
        }

        text.setText(str);
    }
}
