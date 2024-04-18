package jp.aoyama.h15822097.phone_heartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class loginName extends AppCompatActivity {
    EditText editName;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_name);

        editName=findViewById(R.id.editName);
        nextBtn=findViewById(R.id.nextBtn);

    }
    public void nextonClick(View v){
        //名前をstring型にして次のActivityで利用
        String name=editName.getText().toString();
        Intent intent =new Intent(getApplication(),selectDate.class);
        intent.putExtra("editName",editName.getText().toString());
        startActivity(intent);
    }
}