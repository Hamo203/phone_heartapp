package jp.aoyama.h15822097.phone_heartapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TapPagerAdapter extends FragmentStateAdapter {
    public TapPagerAdapter(MainTabActivity fragment) {
        super(fragment);
    }
    /**
     * 指定されたタブの位置(position) に対応するタブページ（Fragment）を作成する
     */

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment=null;
        if(position==0){
            fragment=new Page1();
        }else if(position==1){
            fragment=new Page2();
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
