package com.ahri.chat.ui.fragment;

import android.os.Handler;
import android.os.Message;

import com.ahri.chat.util.MessagePostUtil;
import com.ahri.chat.util.TuLingParseUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * Created by zouyingjie on 2017/3/1.
 */

public class ExtendChatFragment extends EaseChatFragment {

    private static final int FLAG_SEND_MESSAGE = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_SEND_MESSAGE:
                    refreshMessageList(TuLingParseUtils.parseInfo((String) msg.obj));
                    break;
            }
        }
    };


    @Override
    protected void sendTextMessage(final String content) {
        conversation.appendMessage(createSendMessage(content));
        messageList.refresh();
        new Thread() {
            @Override
            public void run() {
                String result = MessagePostUtil.post(content);
                Message msg = Message.obtain();
                msg.what = FLAG_SEND_MESSAGE;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private EMMessage createSendMessage(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        message.setDirection(EMMessage.Direct.SEND);
        message.setStatus(EMMessage.Status.SUCCESS);
        return message;
    }

    private EMMessage createReceiveMessage(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        message.setDirection(EMMessage.Direct.RECEIVE);
        return message;
    }

    private void refreshMessageList(String content) {
        conversation.appendMessage(createReceiveMessage(content));
        messageList.refresh();
    }
}
