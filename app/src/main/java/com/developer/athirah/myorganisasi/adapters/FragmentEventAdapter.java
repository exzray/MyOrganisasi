package com.developer.athirah.myorganisasi.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.developer.athirah.myorganisasi.fragments.EventFinishFragment;
import com.developer.athirah.myorganisasi.fragments.EventOngoingFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentEventAdapter extends FragmentStatePagerAdapter {

    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    private EventOngoingFragment ongoingFragment = new EventOngoingFragment();
    private EventFinishFragment finishFragment =  new EventFinishFragment();


    public FragmentEventAdapter(FragmentManager fm) {
        super(fm);

        fragments.add(ongoingFragment);
        fragments.add(finishFragment);

        titles.add("masih berjalan");
        titles.add("sudah selesai");
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

    public void setAdapters(RecyclerEventAdapter adapter1, RecyclerEventAdapter adapter2){
        ongoingFragment.setAdapter(adapter1);
        finishFragment.setAdapter(adapter2);
    }
}
