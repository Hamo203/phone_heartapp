package jp.aoyama.h15822097.phone_heartapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button start_btn;
    private Button stop_btn;
    private String sensorname;
    private String userName;
    private String info="2024.02.07";
    int id=1;

    //Entryクラスのオブジェクトを格納するためのリスト
    List<Entry> entries =new ArrayList<>();
    //chart用
    private LineChart lineChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GUI設定
        this.start_btn=findViewById(R.id.start_btn);
        this.stop_btn=findViewById(R.id.stop_btn);
        this.lineChart=findViewById(R.id.chart);
        lineChart.getAxisRight().setDrawLabels(false);

        //心拍数か加速度 を変数として受け取る
        Intent intent=getIntent();
        String stringVal=intent.getStringExtra("KEY_STRING");
        //心拍数を選択されたら変数をheartbeat,加速度を選択されたらaccにする
        if(stringVal.equals("心拍数")) sensorname="heartbeat";
        else sensorname="acc";

        //入力された名前を変数として受け取る
        userName=intent.getStringExtra("editName");
        Log.d("test",userName);
        //入力された日付を変数として受け取る
        info=intent.getStringExtra("editDay");
        Log.d("test",info);


        //ボタンの設定
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//ログアウトみたいな状態 初期画面表示
            }
        });

        //グラフの設定
        //x軸について
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(4);//グリッドの縦線の数
        xAxis.setGranularity(1f);
        //y軸について
        YAxis yAxis =lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);//グリッドの横線の数

        Log.d("test","on create");
    }
    public void startonClick(View view){


        Log.d("test","Sensorname=="+sensorname);
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();

        firebase.collection(userName).document(info).collection(sensorname).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && sensorname.equals("heartbeat")) {
                    QuerySnapshot documentSnapshots = task.getResult();
                    if (documentSnapshots.isEmpty()) {
                        //ユーザに指定されたコレクションが存在しない場合の処理
                        Log.d("test", "Collection " + sensorname + " does not exist for user " + userName);
                    } else {
                        // 指定されたコレクションが存在する場合の処理
                        Log.d("test", "Collection " + sensorname + " exists for user " + userName);
                        // データ処理
                        firebase.collection(userName).document(info).collection(sensorname).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Log.w(TAG, "Listen failed.", error);
                                    return;
                                }

                                entries.clear();//データ格納用配列をクリアする
                                int col = 0;
                                for (QueryDocumentSnapshot document : value) {
                                    //firebaseのid順でデータ(心拍数)を読み取る
                                    Map<String, Object> data = document.getData();
                                    if (data != null && data.containsKey("beat")) {
                                        Object beatObject = data.get("beat");

                                        // タイムスタンプを取得してx軸の値として使用する
                                        Timestamp timestamp = document.getTimestamp("timestamp");
                                        long epochTime = timestamp.getSeconds(); // エポック秒を取得する

                                        Log.d("test","epochtime->"+epochTime);
                                        if (beatObject instanceof Number) {
                                            double y = ((Number) beatObject).doubleValue();
                                            float yFloat = (float) y;

                                            int x = col;
                                            entries.add(new Entry(x, yFloat));
                                            //entries.add(new Entry(epochTime, yFloat));
                                        } else {

                                            Log.d(TAG, "Invalid 'beat' data type");
                                        }
                                    } else {
                                        //コレクションは存在してもデータが存在しない場合
                                        Log.d(TAG, "Document does not contain 'beat'");
                                    }

                                    col++;
                                }
                                Log.d("test", "entries:"+String.valueOf(entries));
                                LineDataSet dataSet1 = new LineDataSet(entries, "beat");
                                dataSet1.setColor(Color.BLUE);

                                LineData lineData = new LineData(dataSet1);
                                lineChart.setData(lineData);
                                lineChart.invalidate();
                            }
                        });
                    }
                } else {
                    // エラーが発生した場合の処理
                    //加速度の値は適切な図示の仕方がよくわからなかった(3次元か？)
                    Log.e("test", "Error getting documents: ", task.getException());
                }
            }
        });








    }

}