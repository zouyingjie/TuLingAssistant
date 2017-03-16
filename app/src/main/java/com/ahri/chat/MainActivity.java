package com.ahri.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ahri.chat.ui.activity.PHPSearchActivity;
import com.ahri.chat.ui.activity.StackoverActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button btnTulingChat;
    private Button btnSearchPHP;
    private Button btnStackOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        btnTulingChat = (Button) findViewById(R.id.btn_start_chat);
        btnSearchPHP = (Button) findViewById(R.id.btn_search_php);
        btnStackOver = (Button) findViewById(R.id.btn_search_stackover);

//        btnTulingChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
//                startActivity(intent);
//            }
//        });

        btnSearchPHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PHPSearchActivity.class);
                startActivity(intent);
            }
        });

        btnStackOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StackoverActivity.class);
                startActivity(intent);
            }
        });


    }
}
