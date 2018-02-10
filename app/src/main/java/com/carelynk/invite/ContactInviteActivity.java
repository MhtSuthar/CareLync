package com.carelynk.invite;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.invite.adapter.ContactInviteListAdapter;
import com.carelynk.utilz.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Admin on 20-Oct-16.
 */

public class ContactInviteActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private Toolbar toolbar;
    private TextView txtToolbar, txtNoData, txtInvite;
    private ProgressBar progressBar;
    private ContactInviteListAdapter inviteListAdapter;
    private static final String TAG = "TrendingListActivity";
    private List<HashMap<String, String>> mListContact = new ArrayList<>();
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        initView();
        checkPermission();
    }

    public void checkPermission() {
        if (checkPermission(Manifest.permission.READ_CONTACTS, this)) {
            new FetchContacts(this).execute();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, Constants.REQUEST_PERMISSION_READ_CONTACT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.REQUEST_PERMISSION_READ_CONTACT){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new FetchContacts(this).execute();
            }
        }
    }

    public void onClickInvite(View view){

    }

    private void sendInvitation() {

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtToolbar = (TextView) toolbar.findViewById(R.id.txtToolbar);
        txtInvite = (TextView) toolbar.findViewById(R.id.txtInvite);
        txtInvite.setVisibility(View.INVISIBLE);
        txtNoData = (TextView) findViewById(R.id.txtNoData);
        txtToolbar.setText("Invite Users");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void setRecyclerAdapter(List<HashMap<String, String>> mList) {
        progressBar.setVisibility(View.GONE);
        inviteListAdapter = new ContactInviteListAdapter(this, mList, ContactInviteActivity.this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(inviteListAdapter);
    }

    private List<HashMap<String, String>> convertList(JSONArray mList) {
        List<HashMap<String, String>> list = new ArrayList<>();
        for (int i = 0; i < mList.length(); i++) {
            try {
                JSONObject jsonObject = mList.getJSONObject(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("IsSelect", "0");
                hashMap.put("UserName", ""+jsonObject.getString(Constants.Name));
                hashMap.put("Mobile", ""+jsonObject.getString(Constants.Mobile));
                list.add(hashMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (TextUtils.isEmpty(newText)){
                        searchAttempt("");
                    }else{
                        searchAttempt(newText);
                    }
                    return false;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.e("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void searchAttempt(String newText) {
            if(inviteListAdapter != null){
                if(TextUtils.isEmpty(newText)){
                    setRecyclerAdapter(mListContact);
                }else{
                    List<HashMap<String, String>> mList = new ArrayList<>();
                    for (int i = 0; i < mListContact.size(); i++) {
                        if(mListContact.get(i).get("UserName").toLowerCase().contains(newText.toLowerCase())){
                            mList.add(mListContact.get(i));
                        }
                    }
                    setRecyclerAdapter(mList);
                }
            }
    }

    public void onClickDetail(String mobile) {
        try {
            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ mobile +"&text=" + URLEncoder.encode(getString(R.string.invite_text), "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
      /*
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, );
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Invite To"));*/
    }

    class FetchContacts extends AsyncTask<Void, Void, JSONArray> {

        private static final String TAG = "FetchContacts";
        private Context context;

        public FetchContacts(Context context) {
            this.context = context;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            return getAllContactNameAndNumber();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(JSONArray list) {
            super.onPostExecute(list);
            Log.e(TAG, "onPostExecute: "+list);
            progressBar.setVisibility(View.GONE);
            mListContact.clear();
            mListContact.addAll(convertList(list));
            setRecyclerAdapter(convertList(list));
        }

        public JSONArray getAllContactNameAndNumber() {
            JSONArray jsonArrayContact = new JSONArray();

            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
            };

            ContentResolver contentResolver = context.getContentResolver();

            if (contentResolver == null) {
                return jsonArrayContact;
            }
            Cursor people = context.getContentResolver().query(uri, projection, null, null, null);

            if (people == null) {
                return jsonArrayContact;
            }

            int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            people.moveToFirst();

            do {
                try {
                    String name = people.getString(indexName);
                    String number = people.getString(indexNumber);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.Name, ""+name);
                    jsonObject.put(Constants.Mobile, ""+number);
                    jsonObject.put(Constants.ContactId, ""+getContactIDFromNumber(number, context));//called for getPhoto()
                    jsonArrayContact.put(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } while (people.moveToNext());
            people.close();
            return jsonArrayContact;
        }

        private String replaceAllCharacterWithLastDigit(String mobileNo) {
            String result = mobileNo.replace("(", "").replace(")", "").replace("-", "").replace(" ", "").replace("+", "");
            if (result.length() > 10)
                return result.substring(result.length() - 10, result.length());
            else
                return result;
        }

        long getContactIDFromNumber(String contactNumber, Context context) {
            String UriContactNumber = Uri.encode(contactNumber);
            long phoneContactID = new Random().nextInt();
            Cursor contactLookupCursor = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, UriContactNumber),
                    new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID}, null, null, null);
            while (contactLookupCursor.moveToNext()) {
                phoneContactID = contactLookupCursor.getLong(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            contactLookupCursor.close();

            return phoneContactID;
        }

    }

}
