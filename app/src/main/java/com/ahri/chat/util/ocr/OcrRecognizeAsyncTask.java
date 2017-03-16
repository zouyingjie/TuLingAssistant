/*
 * Copyright 2011 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ahri.chat.util.ocr;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.ahri.chat.base.BaseChatFragment;
import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Class to send OCR requests to the OCR engine in a separate thread, send a success/failure message,
 * and dismiss the indeterminate progress dialog box. Used for non-continuous mode OCR only.
 */
public final class OcrRecognizeAsyncTask extends AsyncTask<Bitmap, Void, String> {


    private TessBaseAPI baseApi;
    private BaseChatFragment fragment;

    public OcrRecognizeAsyncTask(TessBaseAPI baseApi, BaseChatFragment fragment) {
        this.fragment = fragment;
        this.baseApi = baseApi;
    }

    @Override
    protected String doInBackground(Bitmap... arg0) {
        String textResult;
        Bitmap bitmap = arg0[0];
        try {
            baseApi.setImage(bitmap);
            textResult = baseApi.getUTF8Text();
            if (textResult == null || textResult.equals("")) {
                return "";
            } else {
                return textResult;
            }
        } catch (RuntimeException e) {
            Log.e("OcrRecognizeAsyncTask", "Caught RuntimeException in request to Tesseract. Setting state to CONTINUOUS_STOPPED.");
            e.printStackTrace();
            try {
                baseApi.clear();
            } catch (NullPointerException e1) {
            }
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        fragment.insertText(result);
    }
}
