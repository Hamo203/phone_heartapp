package jp.aoyama.h15822097.phone_heartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class loginName extends AppCompatActivity {
    TextInputEditText editMail;
    TextInputEditText editPass;
    FirebaseAuth mAuth;
    Button nextBtn;
    ProgressBar progressBar;
    TextView regNow;

    @Override
    public void onStart() {
        //サインインしているかどうか確認
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //サインインしていたらselectDateへ
            Intent intent=new Intent(getApplicationContext(),selectDate.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_name);

        editMail=findViewById(R.id.email);
        editPass=findViewById(R.id.editPass);
        nextBtn=findViewById(R.id.nextBtn);
        mAuth=FirebaseAuth.getInstance();
        regNow=findViewById(R.id.regNow);
        progressBar=findViewById(R.id.progressbar);
        Log.d("test","mAuth:"+mAuth);
        //mAuth:com.google.firebase.auth.internal.zzad@7c42322
    }
    public void nextonClick(View v){
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
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //進度見れるバー
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Authentication Success!.", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),selectDate.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        //名前をstring型にして次のActivityで利用
        /*String name=editName.getText().toString();
        Intent intent =new Intent(getApplication(),selectDate.class);
        intent.putExtra("editName",editName.getText().toString());
        startActivity(intent);
        */

    }
    public void regNowClick(View v){
        Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
    }
}