package jp.aoyama.h15822097.phone_heartapp;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Page1 extends Fragment {
    private LineChart lineChart;
    private FirebaseFirestore firebase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private List<Entry> entries = new ArrayList<>();

    private static final String ARG_GRAPHNAME = "graphname";
    private static final String ARG_DATE = "date";
    private String graphname;
    private String date;

    public static Page1 newInstance(String graphname,String date) {
        Page1 fragment = new Page1();
        Bundle args = new Bundle();
        args.putString(ARG_GRAPHNAME, graphname);
        args.putString(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            graphname = getArguments().getString(ARG_GRAPHNAME);
            date=getArguments().getString(ARG_DATE);
            Log.d("Page1",date);
            Log.d("graphname",graphname);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineChart = view.findViewById(R.id.chart);

        // Firebaseの初期化
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebase = FirebaseFirestore.getInstance();

        if (user != null && graphname != null) {
            updateChart(); // グラフを更新
        } else {
            Log.e("Page1", "User is not authenticated or graphname is null.");
        }
    }

    private void updateChart() {
        firebase.collection(user.getUid())
                .document(date)
                .collection("heart")
                .document(graphname)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e("Page1", "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        entries.clear();
                        List<Map<String, Object>> beats = (List<Map<String, Object>>) snapshot.get("beats");
                        if (beats != null) {
                            for (int i = 0; i < beats.size(); i++) {
                                Map<String, Object> beatData = beats.get(i);
                                Long beat = (Long) beatData.get("beat");

                                if (beat != null) {
                                    entries.add(new Entry(i, beat.floatValue()));
                                    Log.d("Page1", "Entry: x=" + i + ", y=" + beat);
                                }
                            }

                            LineDataSet dataSet = new LineDataSet(entries, "Heart Rate");
                            dataSet.setColor(Color.BLUE);
                            dataSet.setCircleRadius(4f);
                            dataSet.setDrawCircles(true);
                            dataSet.setDrawValues(false);

                            LineData lineData = new LineData(dataSet);
                            lineChart.setData(lineData);

                            setupChart();
                            lineChart.notifyDataSetChanged();
                            lineChart.invalidate();
                        } else {
                            Log.e("Page1", "No beats data found.");
                        }
                    } else {
                        Log.e("Page1", "Document does not exist.");
                    }
                });
    }


    private void setupChart() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // X軸の間隔を1に設定
        xAxis.setGranularityEnabled(true); // 間隔を有効化
        xAxis.setLabelRotationAngle(45f);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());

            @Override
            public String getFormattedValue(float value) {
                // ミリ秒単位のタイムスタンプを日時形式に変換
                long timestamp = (long) value;
                return sdf.format(new Date(timestamp));
            }
        });

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(150f);

        lineChart.getAxisRight().setEnabled(false); // 右のY軸を無効化
        lineChart.setVisibleXRangeMaximum(10 * 1000); // 10秒分のデータを表示
        lineChart.moveViewToX(entries.get(entries.size() - 1).getX());

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
    }


}
