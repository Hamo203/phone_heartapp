package jp.aoyama.h15822097.phone_heartapp;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.checkerframework.checker.nullness.qual.NonNull;

public class TapPagerAdapter extends FragmentStateAdapter {
    private final String graphname;
    private final String date;
    public TapPagerAdapter(MainTabActivity fragment, String graphname, String date) {
        super(fragment);
        this.graphname = graphname;
        this.date=date;
        Log.d("TapPagerAdapter:",date);
        Log.d("graphname",graphname+"TapPagerAdapter");

    }
    /**
     * 指定されたタブの位置(position) に対応するタブページ（Fragment）を作成する
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new Page1().newInstance(graphname,date);
        } else if (position == 1) {
            fragment = new Page2();
        }
        return fragment;
    }
    /**
     * タブの数を返す
     */
    @Override
    public int getItemCount() {
        return 2;
    }
}