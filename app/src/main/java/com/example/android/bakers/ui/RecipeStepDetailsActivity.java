package com.example.android.bakers.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.bakers.R;
import com.example.android.bakers.model.Step;
import com.google.android.exoplayer2.ExoPlayer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailsActivity extends FragmentActivity {

    ViewPagerAdapter mAdapter;
    ViewPager mPager;
    @BindView(R.id.pager)
    ViewPager pager;
    @Nullable
    @BindView(R.id.btPrevious)
    Button btPrevious;
    @Nullable
    @BindView(R.id.btNext)
    Button btNext;

    private int mStepId;
    private ArrayList<Step> mStepArrayList;




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle b = intent.getBundleExtra("data");
            mStepId = b.getInt("stepPosition");
            mStepArrayList = b.getParcelableArrayList("stepArrayList");
        }

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(mStepId);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mStepId = position;

                if (mStepArrayList != null) {
                    Step step = mStepArrayList.get(mStepId);
                    if (step != null) {
                        if (!(step.getVideoURL().length() > 0 || step.getThumbnailURL().length() > 0)) {
                            Toast.makeText(RecipeStepDetailsActivity.this,
                                    "No video available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            btPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStepId != 0) {
                        mPager.setCurrentItem(--mStepId);
                    }

                }
            });
            btNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStepArrayList != null && mStepId != mStepArrayList.size() - 1) {

                        mPager.setCurrentItem(++mStepId);

                    }
                }
            });
        }
    }


   class ViewPagerAdapter extends FragmentStatePagerAdapter{
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
                return RecipeStepDetailsFragment.newInstance(position, mStepArrayList);
        }

        @Override
        public int getCount() {
            if(mStepArrayList == null)
                return 0;
            return mStepArrayList.size();
        }
    }

}
