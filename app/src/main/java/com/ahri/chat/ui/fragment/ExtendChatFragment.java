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
//                    Toast.makeText(getActivity(), (String) msg.obj, Toast.LENGTH_SHORT).show();
//                    parseInfo();
                    refreshMessageList(TuLingParseUtils.parseInfo((String) msg.obj));
                    break;
            }
        }
    };

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        MyMessageAdapter messageAdapter = new MyMessageAdapter(getActivity(), toChatUsername, chatType, messageList.getListView());
//        messageAdapter.setShowAvatar(true);
//        messageAdapter.setShowUserNick(false);
//        messageAdapter.setMyBubbleBg(messageList.myBubbleBg);
//        messageAdapter.setOtherBuddleBg(messageList.otherBuddleBg);
//        messageAdapter.setCustomChatRowProvider(null);
//        messageList.getListView().setAdapter(messageAdapter);
//
//    }

    @Override
    protected void sendTextMessage(final String content) {
//            super.sendTextMessage(content);
        conversation.appendMessage(createSendMessage(content));
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
