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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

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
    TextView userid;
    Date date;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        firebaseAuth=FirebaseAuth.getInstance();
        user= firebaseAuth.getCurrentUser();

        if (user == null) {
            redirectToLogin();
        } else {
            userid = findViewById(R.id.userid);
            userid.setText(user.getUid());
        }
        initializeUI();
    }

    private void redirectToLogin() {
        //Login画面へのリダイレクト
        Intent intent = new Intent(getApplicationContext(), loginName.class);
        startActivity(intent);
        finish();
    }

    private void initializeUI(){
        radioGroup=(RadioGroup) findViewById(R.id.radioGroup);
        // SpinnerとAdapterの初期化
        spinner = findViewById(R.id.calenderSpinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //カレンダー　初期設定
        c=Calendar.getInstance();
        datetext=findViewById(R.id.dateText); //日付表示用TextView

        datetext.setText(String.format("%d年%d月%d日", c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH)));

        age=findViewById(R.id.age);
        hrest=findViewById(R.id.hrest);
        weight=findViewById(R.id.weight);
        graphname=findViewById(R.id.graphname);
        logoutBtn=findViewById(R.id.logoutBtn);

        // SimpleDateFormat をオブジェクト化し、任意のフォーマットを設定
        sdf= new SimpleDateFormat("yyyy-MM-dd");

        //spinnerにgraphnameを格納
        onSetfirebase(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
    }

    public void nextonClick(View v){//次へボタンが押されたとき
        //radio button のid取得
        checkedId = radioGroup.getCheckedRadioButtonId();
        if (checkedId==R.id.historicBtn) {
            //過去のデータ取得が選択されている場合
            String selectedGraphName = spinner.getSelectedItem().toString();

            if (selectedGraphName.equals("データがありません")) {
                // データがない場合の処理
                Toast.makeText(getApplicationContext(), "有効なグラフがありません", Toast.LENGTH_SHORT).show();
                return; // 処理を終了
            }

            Intent intent=new Intent(getApplicationContext(),MainTabActivity.class);
            intent.putExtra("Date",sdf.format(date)); //日付を次ページへ

            //spinner にグラフタイトルいれる それを返す
            intent.putExtra("graphname",selectedGraphName);
            startActivity(intent);
            finish();

        }
        else if (checkedId==R.id.newBtn) {
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
                // 現在時刻
                date= new Date();

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
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0); // 時間は0:00:00に設定
        date = calendar.getTime(); // Dateオブジェクトに反映

        //firebaseからデータ表示してspinner に表示
        onSetfirebase(year, monthOfYear + 1, dayOfMonth);
    }

    public void onSetfirebase(int year, int monthOfYear, int dayOfMonth) {
        // Calendar オブジェクトに選択された年、月、日を設定
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, monthOfYear - 1, dayOfMonth); // 月は0ベースなので-1

        // sdf でフォーマットされた日付 (例: 2024-07-06)
        String formattedDate = sdf.format(selectedDate.getTime());

        // 現在のユーザーIDを取得
        String userId = user.getUid();

        // 指定された日付に対応するheartコレクションからgraphtitleを取得
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(userId).document(formattedDate).collection("heart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        adapter.clear(); // Spinnerのデータをクリア
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            String graphTitle = documentSnapshot.getId(); // 各ドキュメントのIDがgraphtitle
                            adapter.add(graphTitle); // Spinnerにgraphtitleを追加
                        }
                        adapter.notifyDataSetChanged();  // Spinnerのデータを更新
                    } else {
                        // heartコレクションにデータが存在しない場合
                        adapter.add("データがありません");
                    }
                    adapter.notifyDataSetChanged(); // Spinnerのデータを更新
                })
                .addOnFailureListener(e -> {
                    Log.w("FirebaseError", "データ取得に失敗しました", e);
                    Toast.makeText(getApplicationContext(), "データ取得に失敗しました", Toast.LENGTH_SHORT).show();
                });
    }
}
