package com.ahri.chat.ui.fragment;

import android.os.Handler;
import android.os.Message;

import com.ahri.chat.base.BaseChatFragment;
import com.ahri.chat.constant.Constant;
import com.ahri.chat.util.MessagePostUtil;
import com.ahri.chat.util.PHPParseUtils;
import com.ahri.chat.util.TuLingParseUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zouyingjie on 2017/3/1.
 */

public class PHPSearchFragment extends BaseChatFragment {

    private static final int FLAG_SEND_MESSAGE = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_SEND_MESSAGE:
                    refreshMessageList(PHPParseUtils.parseInfo((String) msg.obj));
                    break;
            }
        }
    };


    @Override
    protected void sendTextMessage(final String content) {
        super.sendTextMessage(content);
//        conversation.appendMessage(createSendMessage(content));
//        messageList.refresh();
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//
//                    String result = MessagePostUtil.post(Constant.PHP_SEARCH_URL, json.toString());
//                    Message msg = Message.obtain();
//                    msg.what = FLAG_SEND_MESSAGE;
//                    msg.obj = result;
//                    handler.sendMessage(msg);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    @Override
    protected String requestService(JSONObject requestContent) {
        return MessagePostUtil.post(Constant.PHP_SEARCH_URL, requestContent.toString());
    }

    @Override
    protected JSONObject getRequestContent(String content) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("SERVICE_NAME", "SERACH_PHP");
        json.put("SEARCH_KEY", content);
        return json;
    }

    @Override
    protected void responseCallBack(String result) {
        refreshMessageList(TuLingParseUtils.parseInfo(result));
    }

}
