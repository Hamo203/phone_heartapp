package jp.aoyama.h15822097.phone_heartapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class selectDate extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    RadioGroup radioGroup;
    int checkedId=-1;
    Calendar c;
    TextView datetext;
    ArrayAdapter<String> adapter;
    Spinner spinner;
    FirebaseAuth firebaseAuth;
    Button logoutBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        radioGroup=(RadioGroup) findViewById(R.id.radioGroup);

        //日付表示用TextView
        datetext=findViewById(R.id.dateText);

        //カレンダー　初期設定
        c=Calendar.getInstance();
        datetext.setText(String.format("%d年%d月%d日", c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH)));

        firebaseAuth=FirebaseAuth.getInstance();
        logoutBtn=findViewById(R.id.logoutBtn);
        user= firebaseAuth.getCurrentUser();
        if(user==null){
            //ユーザがいなかった場合、ログインしなおし
            Intent intent=new Intent(getApplicationContext(),loginName.class);
            startActivity(intent);
            finish();
        }

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        //firebaseからデータをとり,spinnerに格納
        onSetfirebase(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
    }


    public void nextonClick(View v){//次へボタンが押されたとき
        //radio button のid取得
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
        //日付Text押されたらDatePickerFragmentを表示
        DatePick datePicker = new DatePick();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    public void logoutClick(View v){
        //ログアウトして初期画面に戻る
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(getApplicationContext(),loginName.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //選ばれた日付を表示
        datetext.setText(String.format("%d年%d月%d日", year, monthOfYear + 1, dayOfMonth));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    public void onSetfirebase(int year, int monthOfYear, int dayOfMonth){
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        //firebaseから指定された日程のコレクションを見つけ、adapterに追加する
    }

}
