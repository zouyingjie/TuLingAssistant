package com.ahri.chat.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.ahri.chat.R;
import com.ahri.chat.ui.fragment.ExtendChatFragment;
import com.hyphenate.easeui.EaseConstant;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FrameLayout rlChat = (FrameLayout) findViewById(R.id.rl_chat);

        ExtendChatFragment chatFragment = new ExtendChatFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, "图灵小助手");
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.rl_chat, chatFragment).commit();


    }
}
