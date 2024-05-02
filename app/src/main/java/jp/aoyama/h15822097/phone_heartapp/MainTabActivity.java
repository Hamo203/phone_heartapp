package jp.aoyama.h15822097.phone_heartapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        //センサー止める時に使う
        // Dateオブジェクトを用いて現在時刻を取得してくる値を 変数 date に格納
        Date date = new Date();
        // SimpleDateFormat をオブジェクト化し、任意のフォーマットを設定
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
        sdf.format(date);
        Log.d("test","date="+sdf.format(date));

    }
    public void backonClick(View view){
        //一つ前の画面に戻る
        Intent intent=new Intent(getApplication(),selectDate.class);
        startActivity(intent);

    }
}