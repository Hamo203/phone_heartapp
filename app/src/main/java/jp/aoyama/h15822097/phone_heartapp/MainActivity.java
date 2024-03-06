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
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button start_btn;
    private Button stop_btn;
    private String sensorname="heartbeat";
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
        this.start_btn=findViewById(R.id.start_btn);
        this.stop_btn=findViewById(R.id.stop_btn);
        this.lineChart=findViewById(R.id.chart);
        lineChart.getAxisRight().setDrawLabels(false);

        Intent intent=getIntent();
        String stringVal=intent.getStringExtra("KEY_STRING");
        Log.d("test",stringVal);

        //ボタンの設定
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//ログアウトみたいな状態 初期画面表示
            }
        });

        //x軸の設定
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(4);
        xAxis.setGranularity(1f);
        //y軸の設定
        YAxis yAxis =lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        Log.d("test","on create");
    }
    public void startonClick(View view){
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        firebase.collection(sensorname).document(info).collection("heartbeat").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                entries.clear();
                int col = 0;
                for (QueryDocumentSnapshot document : value) {
                    Log.d(TAG, document.getId() + " => " + document.getData());

                    Map<String, Object> data = document.getData();
                    if (data != null && data.containsKey("beat")) {
                        Object beatObject = data.get("beat");

                        if (beatObject instanceof Number) {
                            double y = ((Number) beatObject).doubleValue();
                            float yFloat = (float) y;

                            int x = col;
                            entries.add(new Entry(x, yFloat));
                        } else {
                            Log.d(TAG, "Invalid 'beat' data type");
                        }
                    } else {
                        Log.d(TAG, "Document does not contain 'beat'");
                    }

                    col++;
                }

                LineDataSet dataSet1 = new LineDataSet(entries, "beat");
                dataSet1.setColor(Color.BLUE);

                LineData lineData = new LineData(dataSet1);
                lineChart.setData(lineData);
                lineChart.invalidate();
            }
        });
    }

}