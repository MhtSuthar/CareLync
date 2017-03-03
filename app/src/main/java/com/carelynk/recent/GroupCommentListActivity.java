package com.carelynk.recent;

import android.app.Activity;
import android.os.Bundle;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;

import org.json.JSONArray;

/**
 * Created by Admin on 15-Dec-16.
 */

public class GroupCommentListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void getComments(String groupUpdateId) {
        if(isOnline(this)){
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    try{
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(Urls.GET_GROUP_POST_COMMENT + "/"+groupUpdateId);
        }else{
           // showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }
}
