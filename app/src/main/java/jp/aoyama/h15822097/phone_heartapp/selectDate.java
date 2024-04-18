package jp.aoyama.h15822097.phone_heartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class selectDate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
    }

    public void histonClick(View v){
        Log.d("test","histry");
    }

    public void newonClick(View v){
        Log.d("test","newdata");
    }
}