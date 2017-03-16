package com.ahri.chat.ui.fragment;

import android.content.Intent;

import com.ahri.chat.base.BaseChatFragment;
import com.ahri.chat.constant.Constant;
import com.ahri.chat.util.DBUtils;
import com.ahri.chat.util.MessagePostUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zouyingjie on 2017/3/1.
 */

public class PHPSearchFragment extends BaseChatFragment {

    @Override
    protected void sendTextMessage(final String content) {
        super.sendTextMessage(content);
    }

    @Override
    protected String requestService(JSONObject requestContent) {
        return MessagePostUtil.post(Constant.QUESTION_SERACH_URL, requestContent.toString());
    }

    @Override
    protected JSONObject getRequestContent(String content) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("SERVICE_NAME", Constant.SERVICE_SERACH_PHP);
        json.put("SEARCH_KEY", content);
        return json;
    }

    @Override
    protected void responseCallBack(String result) {
        refreshMessageList(DBUtils.parsePHPInfo(result));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void selectPicFromCamera() {
        super.selectPicFromCamera();
    }

    @Override
    protected void selectPicFromLocal() {
        super.selectPicFromLocal();
    }

//    private void sendTipMessage(){
//        sendTextMessage("Please Select your service: \n 1.PHP手册查询 \n 2.StackOverflow Python模块问题搜索" );
//
//    }

}
