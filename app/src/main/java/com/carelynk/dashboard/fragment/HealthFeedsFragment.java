package com.carelynk.dashboard.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.base.BaseFragment;
import com.carelynk.chat.ChatListActivity;
import com.carelynk.dashboard.PreviewImageActivity;
import com.carelynk.dashboard.WriteArticleActivity;
import com.carelynk.dashboard.model.AutoCompliteArticle;
import com.carelynk.event.fragment.EventViewMemberDialogFragment;
import com.carelynk.profile.DashboardActivity;
import com.carelynk.dashboard.FeedDetailActivity;
import com.carelynk.dashboard.adapter.HealthFeedRecyclerAdapter;
import com.carelynk.dashboard.model.HighlightModel;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.databinding.FragmentTimelineBinding;
import com.carelynk.event.EventListActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.AsyncTaskPostCommon;
import com.carelynk.rest.Urls;
import com.carelynk.search.DiscoverActivity;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.trending.TrendingListActivity;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.carelynk.utilz.camera.BitmapHelper;
import com.carelynk.utilz.camera.CameraIntentHelper;
import com.carelynk.utilz.camera.CameraIntentHelperCallback;
import com.carelynk.utilz.camera.ImageFilePath;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12-Sep-16.
 */
public class HealthFeedsFragment extends BaseFragment {

    FragmentTimelineBinding binding;
    private List<HighlightModel> mHealthFeedList = new ArrayList<>();;
    private HealthFeedRecyclerAdapter myHealthFeedRecyclerAdapter;
    public AsyncTaskGetCommon asyncTaskGetCommon;
    private static final String TAG = "HealthFeedsFragment";
    private ProgressBar mProgressBarHeader;
    private CardView askAQuestion, writeArticle;
    private EditText  edtTopicArticle;
    private AutoCompleteTextView edtTopic;
    private LinearLayout linAskQuestions, linArticle;
    private TextView txtRequestCount;
    private RichEditor richEditor;
    private CameraIntentHelper mCameraIntentHelper;
    private String imagePath = "";

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mHealthFeedList == null || mHealthFeedList.size() == 0) {
                        getHealthFeed();
                        getMessageCount();
                        isUpdate();
                    }
                    ((HomeActivity) getActivity()).hideFab();
                }
            }, 500);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermission();

        setupCameraIntentHelper();

        setRecyclerAdapter();
        binding.swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorTrasparentLightRed,
                R.color.colorTrasparentYellow);
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(true);
                getHealthFeed();
                getMessageCount();
            }
        });
    }

    void getHealthFeed() {
        if (isOnline(getContext())) {
                mProgressBarHeader.setVisibility(View.VISIBLE);
                ApiInterface apiInterface = ApiFactory.provideInterface();
                Call<JsonObject> call = apiInterface.getHealthFeed(ApiFactory.API_BASE_URL+""+ Urls.GET_HELTH_FEED+"?FromUserId="+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            try{
                                binding.swipeRefreshLayout.setRefreshing(false);
                                mHealthFeedList.clear();
                                Log.e(TAG, "onResponse: "+response.body().toString());
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONArray peopleJsonArray = jsonObject.getJSONArray("PeopleData");
                                //JSONArray jsonArray = new JSONArray(response.body().toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //[{"mMainGroupID":1,"MainGroupName":"Heath"},
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    HighlightModel highlightModel = new HighlightModel();
                                    highlightModel.AnswerCount = object.getInt("AnswerCount");
                                    highlightModel.CreatedDate = object.getString("CreatedDate");
                                    highlightModel.Desc = object.getString("Desc");
                                    highlightModel.GoalId = object.getInt("GoalId");
                                    highlightModel.GoalName = object.getString("GoalName");
                                    highlightModel.GoalStatusId = object.getInt("GoalStatusId");
                                    highlightModel.PhotoURL = object.getString("PhotoURL");
                                    highlightModel.ProfilePicUrl = object.getString("ProfilePicUrl");
                                    highlightModel.Expertise = object.getString("Expertise");
                                    highlightModel.PostType = object.getString("PostType");
                                    highlightModel.SupportCount = object.getInt("SupportCount");
                                    highlightModel.UserId = object.getString("UserId");
                                    highlightModel.UserName = object.getString("UserName");
                                    highlightModel.VideoUrl = object.getString("VideoURL");
                                    highlightModel.PepPleMayKnow = peopleJsonArray;
                                    mHealthFeedList.add(highlightModel);
                                }
                               /*for (int i = 0; i < peopleJsonArray.length(); i++) {
                                    JSONObject object = peopleJsonArray.getJSONObject(i);

                                }*/
                                Log.e(TAG, "Size: "+mHealthFeedList.size());
                                if(myHealthFeedRecyclerAdapter != null){
                                    mProgressBarHeader.setVisibility(View.GONE);
                                    myHealthFeedRecyclerAdapter.notifyDataSetChanged();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject>call, Throwable t) {
                        Log.e(TAG, t.toString());
                        mProgressBarHeader.setVisibility(View.GONE);
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                });
        } else {
            mProgressBarHeader.setVisibility(View.GONE);
            binding.swipeRefreshLayout.setRefreshing(false);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }


    private void setRecyclerAdapter() {
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        /**
         * Set Header
         */
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_health_feed, binding.recyclerView, false);

        mProgressBarHeader = (ProgressBar) header.findViewById(R.id.progressBar);
        mProgressBarHeader.setVisibility(View.GONE);
        askAQuestion = (CardView) header.findViewById(R.id.cardViewAskQuestion);
        writeArticle = (CardView) header.findViewById(R.id.cardViewWriteArticle);
        linAskQuestions = (LinearLayout) header.findViewById(R.id.linQuestions);
        linArticle =(LinearLayout) header.findViewById(R.id.linArticle);
        txtRequestCount = (TextView) header.findViewById(R.id.txtRequestCount);

        /**
         * RichEditor initalize
         */
        richEditor = (RichEditor) header.findViewById(R.id.richEditor);
        //richEditor.setBackgroundResource(R.drawable.round_corner_grey);
        richEditor.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        richEditor.setEditorFontSize(22);
        richEditor.setEditorFontColor(Color.BLACK);
        richEditor.setPadding(10, 10, 10, 10);
        richEditor.setPlaceholder("Insert Article here...");
        edtTopicArticle = (EditText) header.findViewById(R.id.edtTopicArticle);

        edtTopic = (AutoCompleteTextView) header.findViewById(R.id.edtTopic);

        edtTopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (isOnline(getActivity())) {
                    if(asyncTaskGetCommon != null)
                        asyncTaskGetCommon.cancel(true);
                    onSearchAttempt();
                } else {
                    showSnackbar(binding.getRoot(), getString(R.string.no_internet));
                }
            }
        });


        askAQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linArticle.setVisibility(View.GONE);
                if(linAskQuestions.isShown()){
                    linAskQuestions.setVisibility(View.GONE);
                }else {
                    linAskQuestions.setVisibility(View.VISIBLE);
                }
            }
        });
        header.findViewById(R.id.btnPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edtTopic.getText().toString())){
                    createPost(edtTopic.getText().toString(), "");
                }else{
                    showSnackbar(binding.getRoot(), "Please Enter Share Text");
                }
            }
        });

        header.findViewById(R.id.img_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.undo();
            }
        });
        header.findViewById(R.id.img_redu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.redo();
            }
        });
        header.findViewById(R.id.img_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBold();
            }
        });
        header.findViewById(R.id.img_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setItalic();
            }
        });
        header.findViewById(R.id.img_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setUnderline();
            }
        });
        header.findViewById(R.id.img_header_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(1);
            }
        });
        header.findViewById(R.id.img_header_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(2);
            }
        });
        header.findViewById(R.id.img_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignCenter();
            }
        });
        header.findViewById(R.id.img_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignLeft();
            }
        });
        header.findViewById(R.id.img_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignRight();
            }
        });
        header.findViewById(R.id.imgSelectArticle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryDialog();
            }
        });
        header.findViewById(R.id.txtMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatListActivity.class));
            }
        });

        header.findViewById(R.id.btnPostArticle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!TextUtils.isEmpty(richEditor.getHtml().toString()) && !TextUtils.isEmpty(edtTopicArticle.getText().toString())) {
                        createPost(edtTopicArticle.getText().toString() ,richEditor.getHtml().toString());
                    } else {
                        showSnackbar(binding.getRoot(), "Please Enter Share Text");
                    }
                }catch (Exception e){
                    showSnackbar(binding.getRoot(), "Please Enter Share Text");
                    e.printStackTrace();
                }
            }
        });

        header.findViewById(R.id.txtGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(new Intent(getActivity(), DiscoverActivity.class), getActivity(), false);
            }
        });

        header.findViewById(R.id.txtEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(new Intent(getActivity(), EventListActivity.class), getActivity(), false);
            }
        });

        header.findViewById(R.id.txtTrending).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(new Intent(getActivity(), TrendingListActivity.class), getActivity(), false);
            }
        });

        header.findViewById(R.id.txtDashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(new Intent(getActivity(), DashboardActivity.class), getActivity(), false);
            }
        });
        writeArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //linAskQuestions.setVisibility(View.GONE);
                //linArticle.setVisibility(View.VISIBLE);
                startActivityForResult(new Intent(getActivity(), WriteArticleActivity.class), Constants.REQUEST_CODE_CHANGE_FEED);
            }
        });


        myHealthFeedRecyclerAdapter = new HealthFeedRecyclerAdapter(header, getActivity(), mHealthFeedList,
                HealthFeedsFragment.this, binding.recyclerView);
        binding.recyclerView.setAdapter(myHealthFeedRecyclerAdapter);
    }

    private void onSearchAttempt() {
        if (edtTopic.getText().length() > 0) {
            asyncTaskGetCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    AutoCompliteArticle autoCompliteArticle = new Gson().fromJson(result, AutoCompliteArticle.class);
                    if (autoCompliteArticle != null) {
                        edtTopic.setAdapter(getList(autoCompliteArticle.getResult().getGoals()));
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL + "" + Urls.GET_SEARCH_ARTICLE + edtTopicArticle.getText().toString());
        }
    }

    private ArrayAdapter<String> getList(List<AutoCompliteArticle.Goal> mList) {
        String[] addresses = new String[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            addresses[i] = mList.get(i).getName();
        }
        return new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, addresses);
    }

    public void onClickUserDetail(String userid) {
        Intent intentDash = new Intent(getActivity(), DashboardActivity.class);
        intentDash.putExtra(Constants.EXTRA_USERID, userid);
        startActivity(intentDash);
    }

    void createPost(String title, String desc){
        if(isOnline(getContext())){
            DialogUtils.showProgressDialog(getContext());
            /*ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("GoalName", title);
            payerReg.addProperty("Desc", desc);
            payerReg.addProperty("UserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Log.e(TAG, "create post: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.createPost(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONObject object = jsonObject.getJSONObject("result");
                            if (object.getBoolean("IsSuccess")) {
                                edtTopic.setText("");
                                AppUtils.closeKeyBoard(getActivity());
                                getHealthFeed();
                            }else{
                                showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject>call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });*/
            ApiInterface apiInterface = ApiFactory.provideInterface();

            MultipartBody.Part bodyImage = null;
            MultipartBody.Part bodyVideo = null;

            RequestBody goalD = RequestBody.create(MediaType.parse("text/plain"), "0");
            RequestBody goalName = RequestBody.create(MediaType.parse("text/plain"), title);
            RequestBody Description = RequestBody.create(MediaType.parse("text/plain"), desc);
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "False");

            Call<JsonObject> call = apiInterface.createPostGoal(goalD, goalName, Description, type, userId, bodyImage, bodyVideo);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONObject object = jsonObject.getJSONObject("result");
                            if (object.getBoolean("IsSuccess")) {
                                edtTopic.setText("");
                                AppUtils.closeKeyBoard(getActivity());
                                getHealthFeed();
                            }else{
                                showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject>call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Goal_Id, ""+mHealthFeedList.get(position).GoalId);
        //moveActivityResult(intent, getActivity(), Constants.REQUEST_CODE_CHANGE_FEED);
        startActivityForResult(intent, Constants.REQUEST_CODE_CHANGE_FEED);
    }

    void getMessageCount(){
        if(isOnline(getActivity())){
                AsyncTaskGetCommon asyncTaskCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                    @Override
                    public void onTaskComplete(String result) {
                        if (result.length() > 0) {
                            //{"result":[]}  {"result":[{"CountMsg":"1"}]}
                            try{
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                if(jsonArray.length() > 0){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                    txtRequestCount.setVisibility(View.VISIBLE);
                                    txtRequestCount.setText(jsonObject1.getString("CountMsg"));
                                }else{
                                    txtRequestCount.setVisibility(View.GONE);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                    }
                });
                asyncTaskCommon.execute(ApiFactory.API_BASE_URL+""+Urls.GET_MESSAGE_COUNT+""+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }

    void isUpdate(){
        if(isOnline(getActivity())){
            AsyncTaskGetCommon asyncTaskCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    if (result.length() > 0) {
                        try{
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("UpdateData");
                            if(jsonArray.length() > 0){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String mWhatsNew = jsonObject1.getString("WhatsNew");
                                String mForce = jsonObject1.getString("Forceupdate");
                                float mVersion = Float.parseFloat(jsonObject1.getString("Version"));
                                //float mVersion = 1.03f;
                                PackageInfo pInfo = getActivity().getPackageManager().
                                        getPackageInfo(getActivity().getPackageName(), 0);
                                String version = pInfo.versionName;
                                if(mVersion > Float.parseFloat(version)){
                                    if(mForce.equalsIgnoreCase("false")){
                                        showUpdateDialog(mWhatsNew, true);
                                    }else{
                                        showUpdateDialog(mWhatsNew, false);
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                }
            });
            asyncTaskCommon.execute(ApiFactory.API_BASE_URL+""+Urls.GET_UPDATE_APP);
        }
    }

    void showUpdateDialog(String msg, boolean isCancel){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Update Available!");
        builder.setCancelable(isCancel);
        builder.setMessage(msg);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String appPackageName = getActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        }).create();
        builder.show();
    }

    private String getPostValue() {
        JSONObject jsonObject = new JSONObject();
        try{
            //jsonObject.put("GoalName", edtTopic.getText().toString());
            //jsonObject.put("Desc", edtDescription.getText().toString());
            jsonObject.put("Photourl", "");
            jsonObject.put("user_id", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private boolean isValid() {
        if(TextUtils.isEmpty(edtTopic.getText().toString())){
            showSnackbar(binding.getRoot(), "Please enter text");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Constants.REQUEST_CODE_CHANGE_FEED){
                getHealthFeed();
            }else if (requestCode == Constants.REQUEST_OPEN_CAMERA) {
                mCameraIntentHelper.onActivityResult(requestCode, resultCode, data);
            } else if (requestCode == Constants.REQUEST_OPEN_GALLERY) {
                getGalleryImageUri(data);
            }
        }
    }

    /**
     * Start Camera Intent handler
     */

    void openGalleryDialog() {
        final Dialog dialog = new DialogUtils(getContext()).setupCustomeDialogFromBottom(R.layout.dialog_gallery);
        ImageView imgCamera = (ImageView) dialog.findViewById(R.id.imgCamera);
        ImageView imgGallery = (ImageView) dialog.findViewById(R.id.imgGallery);
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openCamera();
            }
        });
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openGallery();
            }
        });
        dialog.show();
    }

    public void checkPermission() {
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getActivity()) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getActivity()) && checkPermission(Manifest.permission.CAMERA, getActivity())) {
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_PERMISSION_WRITE_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constants.REQUEST_OPEN_GALLERY);
    }

    void openCamera() {
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getActivity()) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getActivity()) && checkPermission(Manifest.permission.CAMERA, getActivity())) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                File photoFile = createImageFile();
                imagePath = photoFile.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mCameraIntentHelper != null) {
                mCameraIntentHelper.startCameraIntent();
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Carelync_" + timeStamp + "_";
        File sdCard = new File(Environment.getExternalStorageDirectory() + "/Carelync/Images");
        if (!sdCard.exists())
            sdCard.mkdirs();
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    sdCard      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mCameraIntentHelper.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCameraIntentHelper.onRestoreInstanceState(savedInstanceState);
    }

    private void setupCameraIntentHelper() {
        mCameraIntentHelper = new CameraIntentHelper(getActivity(), new CameraIntentHelperCallback() {
            @Override
            public void onPhotoUriFound(Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees) {
                if (photoUri != null) {
                    displayImage(photoUri);
                    imagePath = ImageFilePath.getPath(getActivity(), photoUri);
                    Log.e(TAG, "Image Camera" + imagePath);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, getActivity());
            }

            @Override
            public void onSdCardNotMounted() {
            }

            @Override
            public void onCanceled() {
            }

            @Override
            public void onCouldNotTakePhoto() {
            }

            @Override
            public void onPhotoUriNotFound() {
            }

            @Override
            public void logException(Exception e) {
                Log.e(getClass().getName(), e.getMessage());
            }

            @Override
            public void onActivityResult(Intent intent, int requestCode) {
                startActivityForResult(intent, requestCode);
            }
        });
    }

    /**
     * End  Camera Intent handler
     */

    private Uri getGalleryImageUri(Intent data) {
        Uri uri = null;
        try {
            Uri imageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getActivity().getContentResolver().query(imageUri, projection, null, null,
                    null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            imagePath = selectedImagePath;
            uri = Uri.fromFile(new File(imagePath));
            Log.e(TAG, "Image Gallery" + imagePath);
            //bitmap = galleryCameraDialog.decodeUri(imageUri);
            displayImage(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void displayImage(Uri photoUri) {
        //binding.imgPhotoSelection.setVisibility(View.GONE);
        //binding.imgPhoto.setVisibility(View.VISIBLE);
        //Glide.with(getActivity()).load(photoUri).apply(RequestOptions.circleCropTransform()).into(binding.imgPhoto);
    }

    public void onPlayVideo(String videoUrl) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(AppUtils.getImagePath(videoUrl)), "video/mp4");
        startActivity(intent);
    }

    public void onSeeAllClick(HighlightModel feedModel) {
        DialogFragment newFragment = new PeopleMayKnowDialogFragment();
        newFragment.show(getActivity().getSupportFragmentManager().beginTransaction(), "dialog");
    }

    public void onShareClick(HighlightModel feedModel) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                feedModel.GoalName+"\nhttps://carelynk.com/Goal.aspx?GoalId="+feedModel.GoalId);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void onSpamCall(final HighlightModel feedModel) {
        showAlertDialog(new BaseActivity.OnDialogClick() {
            @Override
            public void onPositiveBtnClick() {
                spamGoal(feedModel.GoalId);
            }

            @Override
            public void onNegativeBtnClick() {

            }
        }, "Confirm Your Report", "You're responding that this is inappropriate for Carelynk", true);
    }

    private void spamGoal(int goalId) {
        if(isOnline(getActivity())){
            DialogUtils.showProgressDialog(getActivity());
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    DialogUtils.stopProgressDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getJSONObject("result").getBoolean("IsSuccess")) {
                            showSnackbar(binding.getRoot(), "Report Spam Send Successfully");
                        } else {
                            showSnackbar(binding.getRoot(), jsonObject.getJSONObject("result").getString("ErrorMessage"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.SPAM_GOAL+"?id="+goalId+"&FromUserId="+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        }else{
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    public void onItemClickImage(String imgPath, ImageView imgView) {
        Intent intent = new Intent(getActivity(), PreviewImageActivity.class);
        intent.putExtra(Constants.EXTRA_IMAGE, imgPath);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), imgView, "profile");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
        }else{
            startActivity(intent);
        }
    }
}
