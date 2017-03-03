package com.carelynk.search;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.databinding.ActivityMySearchBinding;
import com.carelynk.utilz.DialogUtils;

/**
 * Created by Admin on 15-Sep-16.
 */
public class MySearchActivity extends BaseActivity {

    private ActivityMySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupSlideWindowAnimationSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_search);
        init();
    }

    void init(){
        setSupportActionBar(binding.includeToolbar.toolbar);
        binding.includeToolbar.toolbarTitle.setText(getString(R.string.my_search));
        setTitle("");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_categoty){
            openCategoryDialog();
        }
        return true;
    }

    public void openCategoryDialog() {
        final Dialog dialog = new DialogUtils(this).setupCustomeDialogFromBottom(R.layout.dialog_search_category);
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
