package com.ahri.chat.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by zouyingjie on 2017/3/6.
 */

public class ChatMessageListAdapter extends BaseAdapter {

    private Context context;

    public ChatMessageListAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
