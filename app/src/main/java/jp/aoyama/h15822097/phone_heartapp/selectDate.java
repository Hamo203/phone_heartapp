package jp.aoyama.h15822097.phone_heartapp;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    EditText hrest;
    EditText age;
    EditText weight;
    EditText graphname;

    Date date;
    SimpleDateFormat sdf;

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

        age=findViewById(R.id.age);
        hrest=findViewById(R.id.hrest);
        weight=findViewById(R.id.weight);
        graphname=findViewById(R.id.graphname);

        // Dateオブジェクトを用いて現在時刻を取得してくる値を 変数 date に格納
        date= new Date();
        // SimpleDateFormat をオブジェクト化し、任意のフォーマットを設定
        sdf= new SimpleDateFormat("yyyy-MM-dd");

        Log.d("test","test");


        firebaseAuth=FirebaseAuth.getInstance();
        logoutBtn=findViewById(R.id.logoutBtn);
        //ユーザ取得
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
            //過去のデータ取得が選択されている場合
            Toast.makeText(getApplicationContext(),
                    ((RadioButton)findViewById(checkedId)).getText()
                            + "が選択されています:"+checkedId,
                    Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),MainTabActivity.class);
            intent.putExtra("Date",sdf.format(date)); //日付を次ページへ
            intent.putExtra("graphname","nodata");
            startActivity(intent);
            finish();


        }else if (checkedId==R.id.newBtn) {
            //新規データ取得が選択されている場合

            String sage=age.getText().toString();
            String shrest=hrest.getText().toString();
            String sweight=weight.getText().toString();
            String sgraphname=graphname.getText().toString();

            if(shrest.length()==0||sage.length()==0||sweight.length()==0||sgraphname.length()==0){
                Toast.makeText(getApplicationContext(), "未入力の欄があります",
                        Toast.LENGTH_SHORT).show();
            }else if(!sage.matches("\\d+")){
                Toast.makeText(getApplicationContext(), "年齢の値が不適切です",
                        Toast.LENGTH_SHORT).show();
            }else if(!shrest.matches("\\d+")){
                Toast.makeText(getApplicationContext(), "安静時心拍数を整数で入力してください",
                        Toast.LENGTH_SHORT).show();
            }else if(!sweight.matches("\\d+(\\.\\d+)?")){
                Toast.makeText(getApplicationContext(), "体重の値が不適切です",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Integer iage=Integer.parseInt(sage);
                Integer ihrest=Integer.parseInt(shrest);
                Float fweight=Float.parseFloat(sweight);

                Map<String, Object> pdata = new HashMap<>();
                pdata.put("age", iage);
                pdata.put("hrest", ihrest);
                pdata.put("weight", fweight);

                //各ユーザのコレクションを作成し、基本データ送信
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(user.getUid()).document(sdf.format(date)).set(pdata)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("test", "データ送信よし");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("test", "データ送信×", e);
                            }
                        });


                Intent intent=new Intent(getApplicationContext(),MainTabActivity.class);
                intent.putExtra("Date",sdf.format(date)); //日付を次ページへ
                intent.putExtra("graphname",sgraphname);
                startActivity(intent);
                finish();

            }


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
