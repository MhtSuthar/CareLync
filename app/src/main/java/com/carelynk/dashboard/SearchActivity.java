package com.carelynk.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.adapter.SearchRecyclerAdapter;
import com.carelynk.dashboard.model.SearchData;
import com.carelynk.profile.DashboardActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.google.gson.Gson;

/**
 * Created by admin on 09/02/17.
 */

public class SearchActivity extends BaseActivity {

    private static final String TAG = "SearchActivity";
    private EditText mEditSearch;
    private RecyclerView mRecyclerViewGroup, mRecyclerViewFeeds, mRecyclerViewUsers;
    private ProgressBar mProgressBar;
    private SearchRecyclerAdapter mSearchGroupRecyclerAdapter, mSearchFeedRecyclerAdapter, mSearchUserRecyclerAdapter;
    private TextView mEmpty;
    private SearchData mSearchData;
    private TextView mTxtGroup, mTxtUser, mTxtFeed;
    private AsyncTaskGetCommon asyncTaskGetCommon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setContentView(R.layout.activity_search);

        initView();

        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isOnline(SearchActivity.this)) {
                    if(asyncTaskGetCommon != null)
                        asyncTaskGetCommon.cancel(true);
                    onSearchAttempt();
                } else {
                    showSnackbar(mRecyclerViewGroup, getString(R.string.no_internet));
                }
            }
        });
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    private void onSearchAttempt() {
        if (mEditSearch.getText().length() > 0) {
            mProgressBar.setVisibility(View.VISIBLE);
            asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    mProgressBar.setVisibility(View.GONE);
                    mSearchData = new Gson().fromJson(result, SearchData.class);
                    if (mSearchData != null) {
                        mEmpty.setVisibility(View.GONE);
                        setAdapter();
                        if (mSearchData.getResult().getGroups().size() == 0 &&
                                mSearchData.getResult().getGoals().size() == 0 &&
                                mSearchData.getResult().getUserProfiles().size() == 0) {
                            mEmpty.setVisibility(View.VISIBLE);
                            mTxtFeed.setVisibility(View.GONE);
                            mTxtGroup.setVisibility(View.GONE);
                            mTxtUser.setVisibility(View.GONE);
                        }else{
                            mTxtFeed.setVisibility(View.VISIBLE);
                            mTxtGroup.setVisibility(View.VISIBLE);
                            mTxtUser.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL + "" + Urls.GET_SEARCH + "?searchText=" + mEditSearch.getText().toString());
        }
    }

    private void initView() {
        mEditSearch = (EditText) findViewById(R.id.edtSearch);
        mRecyclerViewGroup = (RecyclerView) findViewById(R.id.recyclerViewGroup);
        mRecyclerViewFeeds = (RecyclerView) findViewById(R.id.recyclerViewFeed);
        mRecyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUser);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mEmpty = (TextView) findViewById(R.id.txt_empty);
        mTxtGroup = (TextView) findViewById(R.id.txt_group);
        mTxtUser = (TextView) findViewById(R.id.txt_user);
        mTxtFeed = (TextView) findViewById(R.id.txt_feed);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, 0);
    }

    void setAdapter() {
        mSearchGroupRecyclerAdapter = new SearchRecyclerAdapter(this, mSearchData.getResult().getGroups(), SearchActivity.this);
        mRecyclerViewGroup.setHasFixedSize(true);
        LinearLayoutManager mLayoutManagerGroup = new LinearLayoutManager(this);
        mLayoutManagerGroup.setAutoMeasureEnabled(true);
        mRecyclerViewGroup.setNestedScrollingEnabled(false);
        mRecyclerViewGroup.setLayoutManager(mLayoutManagerGroup);
        mRecyclerViewGroup.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewGroup.setAdapter(mSearchGroupRecyclerAdapter);

        mSearchFeedRecyclerAdapter = new SearchRecyclerAdapter(this, mSearchData.getResult().getGoals(), SearchActivity.this, 0);
        mRecyclerViewFeeds.setNestedScrollingEnabled(false);
        LinearLayoutManager mLayoutManagerFeed = new LinearLayoutManager(this);
        mLayoutManagerFeed.setAutoMeasureEnabled(true);
        mRecyclerViewFeeds.setLayoutManager(mLayoutManagerFeed);
        mRecyclerViewFeeds.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewFeeds.setAdapter(mSearchFeedRecyclerAdapter);

        mSearchUserRecyclerAdapter = new SearchRecyclerAdapter(this, mSearchData.getResult().getUserProfiles(), SearchActivity.this, "");
        mRecyclerViewUsers.setNestedScrollingEnabled(false);
        LinearLayoutManager mLayoutManagerUser = new LinearLayoutManager(this);
        mLayoutManagerUser.setAutoMeasureEnabled(true);
        mRecyclerViewUsers.setLayoutManager(mLayoutManagerUser);
        mRecyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewUsers.setAdapter(mSearchUserRecyclerAdapter);
    }

    /**
     * @param id
     * @param type for 0 means Group
     *             1 = Feed
     *             2 = User
     */
    public void onClick(String id, int type) {
        switch (type) {
            case 0:
                Intent intentG = new Intent(this, GroupDetailActivity.class);
                intentG.putExtra(Constants.EXTRA_GROUP_ID, "" + id);
                moveActivity(intentG, this);
                break;
            case 1:
                Intent intent = new Intent(this, FeedDetailActivity.class);
                intent.putExtra(AppUtils.Extra_Goal_Id, "" + id);
                moveActivity(intent, this);
                break;
            case 2:
                Intent intentDash = new Intent(this, DashboardActivity.class);
                intentDash.putExtra(Constants.EXTRA_USERID, id);
                startActivity(intentDash);
                break;
        }
    }
}
