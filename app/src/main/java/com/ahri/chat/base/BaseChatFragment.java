package com.ahri.chat.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahri.chat.util.ocr.OcrInitAsyncTask;
import com.ahri.chat.util.ocr.OcrRecognizeAsyncTask;
import com.ahri.chat.util.BasicUtils;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;

import org.json.JSONException;
import org.json.JSONObject;

import edu.sfsu.cs.orange.ocr.CaptureActivity;

/**
 * Created by zouyingjie on 2017/3/8.
 */

public abstract class BaseChatFragment extends EaseChatFragment {


    protected static final int SEND_TEXT_MESSAGE = 11000;
    protected static final int TAKE_PHOTO_OCR = 11001;
    protected static final int TAKE_SCAN_CAMERA = 11002;
    protected boolean initSuccess;

    private String languageCode = "eng";
    private String languageName = "English";



    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_TEXT_MESSAGE:
                    responseCallBack((String) msg.obj);
                    break;
                case TAKE_PHOTO_OCR:
                    insertText((String) msg.obj);
                    break;
                case TAKE_SCAN_CAMERA:
                    insertText((String) msg.obj);
                    break;
            }

        }
    };
    private ProgressDialog dialog;
    protected TessBaseAPI baseApi;



    public void setInitSuccess(boolean initSuccess) {
        this.initSuccess = initSuccess;
    }

    @Override
    protected void selectPicFromLocal() {
        Intent intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT);
        intentFromGallery.addCategory(Intent.CATEGORY_OPENABLE);
        intentFromGallery.setType("image/**"); // 设置文件类型
        startActivityForResult(intentFromGallery, TAKE_PHOTO_OCR);
    }

    @Override
    protected void selectPicFromCamera() {
        startActivityForResult(new Intent(getActivity(), CaptureActivity.class), TAKE_SCAN_CAMERA);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!initSuccess) {
            dialog = new ProgressDialog(getActivity());

            // If we have a language that only runs using Cube, then set the ocrEngineMode to Cube
            dialog.setTitle("Please wait");
            dialog.setMessage("Initializing  Tesseract OCR engines for " + languageName + "...");
            dialog.setCancelable(false);
            dialog.show();

            baseApi = new TessBaseAPI();
            String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();

            new OcrInitAsyncTask(this, getActivity(), baseApi, dialog, null, languageCode, languageName, TessBaseAPI.OEM_DEFAULT)
                    .execute();
        }

    }
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
                        msg.what = SEND_TEXT_MESSAGE;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO_OCR:
                Uri uri = data.getData();
                String textPre = uri.toString();
                String text = "";
                if (uri.toString().contains("content://")) { //如果包含有content开头，需要转化为其实际路径，不能用content开头
                    text = BasicUtils.getRealPathFromURI(getContext(), uri);
                } else {
                    text = textPre;       //如果用file开头，不用转化
                }
                Bitmap bitmap = BitmapFactory.decodeFile(text);
                new OcrRecognizeAsyncTask(baseApi, this).execute(bitmap);
                break;
            case TAKE_SCAN_CAMERA:
                String result = data.getStringExtra("result");
                Message message = Message.obtain();
                message.what = TAKE_SCAN_CAMERA;
                message.obj = result;
                handler.sendMessage(message);
                break;

        }
    }

    public void insertText(String content){
        inputMenu.insertText(content);
    }

    protected abstract String requestService(JSONObject requestContent);

    protected abstract JSONObject getRequestContent(String content) throws JSONException;

    protected abstract void responseCallBack(String result);
}
