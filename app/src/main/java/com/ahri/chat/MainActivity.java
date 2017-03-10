package com.ahri.chat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ahri.chat.ui.activity.ChatActivity;
import com.ahri.chat.ui.activity.PHPSearchActivity;
import com.ahri.chat.ui.activity.StackoverActivity;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button btnRegister;
    private Button btnLogin;
    private Button btnSendMessage;
    private Button btnStartChat;
    private Button btn_add_contact;
    private Button btnSearchPHP;
    private Button btnStackOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartChat = (Button) findViewById(R.id.btn_start_chat);
        btnSearchPHP = (Button) findViewById(R.id.btn_search_php);
        btnStackOver = (Button) findViewById(R.id.btn_search_stackover);

        btnStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

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



//        requestPermisssion();
    }

    private void requestPermisssion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA,
                    android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            //连接不到聊天服务器
                        } else {

                        }//当前网络不可用，请检查网络设置

                    }
                }
            });
        }
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Log.i(TAG, "收到消息" + messages.get(0).getBody().toString());
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            Log.i(TAG, "收到透传消息" );
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //消息已读
            Log.i(TAG, "消息已读");
        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {
            //收到已送达回执
            Log.i(TAG, "消息已送达");
        }


        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            Log.i(TAG, "消息状态变动");
        }
    };


}
