package com.carelynk.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

public class AsyncTaskPostCommon extends AsyncTask<Object, Void, String> {

    private AsyncTaskCompleteListener callback;
    private Context context;
    private static final String TAG = "AsyncTaskPostCommon";

    public AsyncTaskPostCommon(Context context, AsyncTaskCompleteListener cb) {
        this.context = context;
        this.callback = cb;
    }

    @Override
    protected String doInBackground(Object... params) {
        try{
            String url = (String) params[0];
            String jsonObject = (String) params[1];
            Log.e("param", ""+jsonObject.toString());
            JsonParser jsonParser = new JsonParser();
            return jsonParser.postToResponse(url, jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    protected void onPostExecute(String result) {
        try {
            Log.e(TAG, "onPostExecute: "+result);
            callback.onTaskComplete(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface AsyncTaskCompleteListener {
        void onTaskComplete(String result) throws JSONException;
    }
}