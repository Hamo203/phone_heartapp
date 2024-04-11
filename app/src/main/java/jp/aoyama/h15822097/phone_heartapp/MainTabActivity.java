package jp.aoyama.h15822097.phone_heartapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        // ViewPagerをアダプタに関連付ける
        ViewPager2 pager = (ViewPager2)findViewById(R.id.pager);
        TapPagerAdapter adapter = new TapPagerAdapter(this);
        pager.setAdapter(adapter);

        TabLayout tabs =(TabLayout) findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabs, pager,
                (tab, position) -> tab.setText("PAGE " + (position + 1))
        ).attach();

    }
}