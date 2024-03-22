package jp.aoyama.h15822097.phone_heartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SelectActivity extends AppCompatActivity {
    Button nextButton;
    EditText editName;
    Spinner spin_sensor;
    EditText editDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //日付入力の設定
        editName=findViewById(R.id.editName);

        //日付入力の設定
        editDay=findViewById(R.id.editDay);

        //スピナーの設定
        //心拍数か加速度の値どちらをグラフに表示するか
        ArrayAdapter<String> adaSensor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adaSensor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaSensor.add("心拍数");
        adaSensor.add("加速度");
        spin_sensor = (Spinner) findViewById(R.id.spin_sensor);
        spin_sensor.setAdapter(adaSensor);

        //ボタンの設定
        nextButton=findViewById(R.id.nextToBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editName.getText().toString();
                String day=editDay.getText().toString();

                String selected=(String) spin_sensor.getSelectedItem();
                Intent intent =new Intent(getApplication(),MainActivity.class);

                //入力された名前
                intent.putExtra("editName",name);
                //入力された日付
                intent.putExtra("editDay",day);
                //心拍数or加速度
                intent.putExtra("KEY_STRING",selected);
                startActivity(intent);
            }
        });
    }
}