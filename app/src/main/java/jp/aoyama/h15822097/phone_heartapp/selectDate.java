package jp.aoyama.h15822097.phone_heartapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class selectDate extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    RadioGroup radioGroup;
    int checkedId=-1;
    Calendar c;
    TextView datetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        radioGroup=(RadioGroup) findViewById(R.id.radioGroup);

        datetext=findViewById(R.id.dateText);

        c=Calendar.getInstance();
        datetext.setText(String.format("%d年%d月%d日", c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)));
    }


    public void nextonClick(View v){
        checkedId = radioGroup.getCheckedRadioButtonId();
        if (checkedId==R.id.historicBtn) {
            //radio buttonが選択されている場合
            Toast.makeText(getApplicationContext(),
                    ((RadioButton)findViewById(checkedId)).getText()
                            + "が選択されています:"+checkedId,
                    Toast.LENGTH_SHORT).show();
        }else if (checkedId==R.id.newBtn) {
            //radio buttonが選択されている場合
            Toast.makeText(getApplicationContext(),
                    ((RadioButton)findViewById(checkedId)).getText()
                            + "が選択されています:"+checkedId,
                    Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),
                    "何も選択されていません",
                    Toast.LENGTH_SHORT).show();
        }


    }

    public void textonClick(View v){
        //DatePickerFragmentを表示
        DatePick datePicker = new DatePick();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    public void backonClick(View v){
        //初期画面に戻る
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        datetext.setText(String.format("%d年%d月%d日", year, monthOfYear + 1, dayOfMonth));
    }
}