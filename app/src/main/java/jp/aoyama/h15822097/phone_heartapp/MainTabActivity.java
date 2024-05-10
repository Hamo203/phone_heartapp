package jp.aoyama.h15822097.phone_heartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainTabActivity extends AppCompatActivity {
    Button funcBtn;
    Button backBtn;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Date date;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        funcBtn=findViewById(R.id.funcBtn);
        backBtn=findViewById(R.id.backBtn);

        // Dateオブジェクトを用いて現在時刻を取得してくる値を 変数 date に格納
        date= new Date();
        // SimpleDateFormat をオブジェクト化し、任意のフォーマットを設定
        sdf= new SimpleDateFormat("yyyy-MM-dd");

        Intent intent = getIntent();
        String graphname=intent.getStringExtra("graphname");
        Log.d("test",graphname);



        //グラフタイトルをfirebaseから読み込みスピナーに入れる
        ArrayAdapter<String> titleadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        titleadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(!graphname.equals("nodata")){
            //新規データ入力の場合タイトルをスピナーに追加
            titleadapter.add(graphname);
        }

        firebaseAuth=FirebaseAuth.getInstance();
        user= firebaseAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //過去のグラフタイトルをスピナーで選べるようにした
        db.collection(user.getUid())
                .document(sdf.format(date))
                .collection("heart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("test", document.getId() + " => " + document.getData());
                                titleadapter.add((String) document.getId());
                            }
                        } else {
                            Log.d("test", "Error getting documents: ", task.getException());
                        }
                    }
                });
        Spinner spinner = (Spinner) findViewById(R.id.titlespin);
        spinner.setAdapter(titleadapter);

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