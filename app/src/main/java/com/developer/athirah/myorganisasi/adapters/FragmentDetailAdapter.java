package com.developer.athirah.myorganisasi.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.developer.athirah.myorganisasi.fragments.DetailDataFragment;
import com.developer.athirah.myorganisasi.fragments.DetailTaskFragment;
import com.developer.athirah.myorganisasi.models.ModelEvent;

import java.util.ArrayList;
import java.util.List;

public class FragmentDetailAdapter extends FragmentStatePagerAdapter {

    private DetailDataFragment dataFragment = new DetailDataFragment();
    private DetailTaskFragment taskFragment = new DetailTaskFragment();

    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();


    public FragmentDetailAdapter(FragmentManager fm) {
        super(fm);

        fragments.add(dataFragment);
        fragments.add(taskFragment);

        titles.add("keterangan");
        titles.add("tugasan");
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void updateEvent(ModelEvent event) {
        dataFragment.updateEvent(event);
    }

    public void setAdapter(RecyclerTaskAdapter adapter) {
        taskFragment.setAdapter(adapter);
    }
}
