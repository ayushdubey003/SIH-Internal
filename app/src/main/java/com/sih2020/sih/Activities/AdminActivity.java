package com.sih2020.sih.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.sih2020.sih.Fragments.JudgesFragment;
import com.sih2020.sih.Fragments.ParametersFragment;
import com.sih2020.sih.Fragments.TeamFragment;
import com.sih2020.sih.R;

public class AdminActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private ViewPager mViewpager;
    private TabLayout mTabLayout;
    private ImageView mLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mViewpager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewpager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setOnTabSelectedListener(this);
        mViewpager.setAdapter(new SomeAdapter(getSupportFragmentManager()));
        mViewpager.setOffscreenPageLimit(3);
        mLogOut = findViewById(R.id.log_out);
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                intent.putExtra("First Time", 1);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public static class SomeAdapter extends FragmentPagerAdapter {
        private static int mFragmentCount = 3;

        public SomeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return JudgesFragment.newInstance();
                case 1:
                    return ParametersFragment.newInstance();
                case 2:
                    return TeamFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return mFragmentCount;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Add/Update Judges";
                case 1:
                    return "Add/Update Parameters";
                case 2:
                    return "Add/Update Teams";
            }
            return super.getPageTitle(position);
        }
    }
}
