package jp.aoyama.h15822097.phone_heartapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainTabActivity extends AppCompatActivity {
    Button funcBtn;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        funcBtn=findViewById(R.id.funcBtn);
        backBtn=findViewById(R.id.backBtn);

        // ViewPagerをアダプタに関連付ける
        ViewPager2 pager = (ViewPager2)findViewById(R.id.pager);
        TapPagerAdapter adapter = new TapPagerAdapter(this);
        pager.setAdapter(adapter);

        TabLayout tabs =(TabLayout) findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabs, pager,
                (tab, position) -> tab.setText("PAGE " + (position + 1))
        ).attach();
    }
    public void funcOnClick(View view){

    }
    public void backonClick(View view){
        //一つ前の画面に戻る
        Intent intent=new Intent(getApplication(),selectDate.class);
        startActivity(intent);

    }
}