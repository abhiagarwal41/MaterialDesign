package com.abhishek.materialdesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle args = new Bundle();
        args.putString(DetailFragment.KEY_NAME, getIntent().getStringExtra(DetailFragment.KEY_NAME));
        args.putInt(DetailFragment.KEY_POSITION,getIntent().getIntExtra(DetailFragment.KEY_POSITION,0));
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, detailFragment)
                .commit();
    }
}
