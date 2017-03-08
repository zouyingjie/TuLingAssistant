package com.ahri.chat.ui.fragment;

import com.ahri.chat.base.BaseChatFragment;
import com.ahri.chat.constant.Constant;
import com.ahri.chat.util.MessagePostUtil;
import com.ahri.chat.util.TuLingParseUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zouyingjie on 2017/3/1.
 */

public class ExtendChatFragment extends BaseChatFragment {


    @Override
    protected void sendTextMessage(final String content) {
        super.sendTextMessage(content);
    }

    /**
     * 发起请求
     *
     * @param requestContent
     * @return
     */
    @Override
    protected String requestService(JSONObject requestContent) {
        return MessagePostUtil.post(Constant.TULING_URL, requestContent.toString());
    }

    /**
     * 初始化请求数据
     *
     * @param content
     * @return
     * @throws JSONException
     */
    @Override
    protected JSONObject getRequestContent(String content) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("key", Constant.TULING_KEY);
        json.put("info", content);
        return json;
    }


    /**
     * 处理请求结果
     *
     * @param result
     */
    @Override
    protected void responseCallBack(String result) {
        refreshMessageList(TuLingParseUtils.parseInfo(result));
    }
}
