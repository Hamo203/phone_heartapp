package jp.aoyama.h15822097.phone_heartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SelectActivity extends AppCompatActivity {
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //スピナーの設定
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("A型");
        adapter.add("B型");
        adapter.add("AB型");
        adapter.add("O型");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);


        //ボタンの設定
        nextButton=findViewById(R.id.nextToBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected=(String) spinner.getSelectedItem();
                Intent intent =new Intent(getApplication(),MainActivity.class);
                intent.putExtra("KEY_STRING",selected);
                startActivity(intent);
            }
        });
    }
}