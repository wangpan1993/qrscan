package com.yssh.scan;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activit_input extends AppCompatActivity {

    private TextView tvsure;
    private EditText etinput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        tvsure = findViewById(R.id.tv_sure);
        etinput = findViewById(R.id.et_input);

        ((TextView) findViewById(R.id.tv_titleName)).setText("手动输入");
        findViewById(R.id.rl_titleLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etinput.getText().toString().equals("")) {

                }
                Toast.makeText(Activit_input.this, "条码内容：" + etinput.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
