package com.ahri.chat.base;

import android.os.Handler;
import android.os.Message;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zouyingjie on 2017/3/8.
 */

public abstract class BaseChatFragment extends EaseChatFragment {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            responseCallBack((String) msg.obj);
        }
    };

    @Override
    protected void sendTextMessage(final String content) {
        try {
            conversation.appendMessage(createSendMessage(content));
            messageList.refresh();
            final JSONObject requestContent = getRequestContent(content);
            new Thread() {
                @Override
                public void run() {
                    try {
                        Message msg = Message.obtain();
                        msg.obj = requestService(requestContent);
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected EMMessage createSendMessage(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        message.setDirection(EMMessage.Direct.SEND);
        message.setStatus(EMMessage.Status.SUCCESS);
        return message;
    }

    protected EMMessage createReceiveMessage(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        message.setDirection(EMMessage.Direct.RECEIVE);
        return message;
    }

    protected void refreshMessageList(String content) {
        conversation.appendMessage(createReceiveMessage(content));
        messageList.refresh();
    }

    protected abstract String requestService(JSONObject requestContent);

    protected abstract JSONObject getRequestContent(String content) throws JSONException;

    protected abstract void responseCallBack(String result);
}
