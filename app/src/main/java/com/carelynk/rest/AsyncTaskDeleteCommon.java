package com.carelynk.rest;

import android.content.Context;
import android.os.AsyncTask;


public class AsyncTaskDeleteCommon extends AsyncTask<String, Void, String> {

    private AsyncTaskCompleteListener callback;
    private Context context;

    public AsyncTaskDeleteCommon(Context context, AsyncTaskCompleteListener cb) {
        this.context = context;
        this.callback = cb;
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            JsonParser jsonParser = new JsonParser();
            return jsonParser.deleteResponse(params[0]);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    protected void onPostExecute(String result) {
       callback.onTaskComplete(result);
   }

    public interface AsyncTaskCompleteListener {
        void onTaskComplete(String result);
    }
}