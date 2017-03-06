package com.ahri.chat.ui;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ahri.chat.R;

/**
 * Created by zouyingjie on 2017/3/6.
 */

public class ChatMessageList extends RelativeLayout {

    private ListView lvChatMessageList;
    private SwipeRefreshLayout swipeChatMessageRefresh;

    public ChatMessageList(Context context) {
        this(context, null);
    }

    public ChatMessageList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ChatMessageList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_list, null);
        lvChatMessageList = (ListView) view.findViewById(R.id.lv_chat_message_list);
        swipeChatMessageRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_chat_message_refresh);
    }



}
