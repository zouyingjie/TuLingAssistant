package com.ahri.chat.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ahri.chat.R;
import com.ahri.chat.ui.fragment.PHPSearchFragment;
import com.hyphenate.easeui.EaseConstant;

public class PHPSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phpsearch);
        PHPSearchFragment fragment = new PHPSearchFragment();
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, "PHP手册查询");
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_phpsearch, fragment).commit();
    }
}
