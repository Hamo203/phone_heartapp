package jp.aoyama.h15822097.phone_heartapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Locale;

public class selectDate extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
    }

    public void histonClick(View v){
        Log.d("test","histry");
        DialogFragment newFragment=new DatePick();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void newonClick(View v){
        Log.d("test","newdata");
        Intent intent=new Intent(getApplication(),MainTabActivity.class);
        startActivity(intent);
    }
    public void backonClick(View v){
        //初期画面に戻る
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String str = String.format(Locale.JAPAN, "%d/%d/%d",year, monthOfYear+1, dayOfMonth);
        Log.d("test",str);
    }
}