package jp.aoyama.h15822097.phone_heartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText editMail;
    TextInputEditText editPass;
    FirebaseAuth mAuth;
    Button registerBtn;
    ProgressBar progressBar;
    TextView logNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editMail=findViewById(R.id.email);
        editPass=findViewById(R.id.editPass);
        registerBtn=findViewById(R.id.registerBtn);
        progressBar=findViewById(R.id.progressbar);
        logNow=findViewById(R.id.loginNow);
        mAuth= FirebaseAuth.getInstance();

    }
    public void birthonClick(View v){
        //日付Text押されたらDatePickerFragmentを表示
        DatePick datePicker = new DatePick();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }
    public void regonClick(View v){
        //進度見れるバー
        progressBar.setVisibility(View.VISIBLE);

        String email,password;
        email=String.valueOf(editMail.getText());
        password=String.valueOf(editPass.getText());

        if(TextUtils.isEmpty(email)){
            //email入力なかったら
            Toast.makeText(getApplicationContext(),"Email が空欄です",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //pass入力なかったら
            Toast.makeText(getApplicationContext(),"Pass が空欄です",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //進度見れるバー
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication success!.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }
    public void logNowClick(View v){
        Intent intent=new Intent(getApplicationContext(),loginName.class);
        startActivity(intent);
    }
}