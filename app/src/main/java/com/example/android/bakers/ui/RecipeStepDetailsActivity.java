package com.example.android.bakers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.bakers.R;
import com.example.android.bakers.model.Step;
import com.google.android.exoplayer2.ExoPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailsActivity extends FragmentActivity {

    ViewPagerAdapter mAdapter;
    ViewPager mPager;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.btPrevious)
    Button btPrevious;
    @BindView(R.id.btNext)
    Button btNext;

    private ExoPlayer mExoPlayer;
    private int mStepId;
    private ArrayList<Step> mStepArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);
        ButterKnife.bind(this);

        Intent intent= getIntent();
        if(intent != null){
            Bundle b= intent.getBundleExtra("data");
            mStepId= b.getInt("stepPosition");
            mStepArrayList= b.getParcelableArrayList("stepArrayList");
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
                mStepId= position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExoPlayer != null && mStepId != 0) {
                    mExoPlayer.stop();
                }
                if(mStepId != 0)
                {
                    mPager.setCurrentItem(mStepId -1);
                }

            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExoPlayer != null && mStepId != mStepArrayList.size()-1)
                    mExoPlayer.stop();
                if(mStepArrayList != null && mStepId != mStepArrayList.size()-1){

                    mPager.setCurrentItem(mStepId + 1);
                }
            }
        });
    }


   class ViewPagerAdapter extends FragmentStatePagerAdapter{
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
                RecipeStepDetailsFragment recipeStepDetailsFragment= RecipeStepDetailsFragment.newInstance(position, mStepArrayList);
                return recipeStepDetailsFragment;
        }

        @Override
        public int getCount() {
            if(mStepArrayList == null)
                return 0;
            return mStepArrayList.size();
        }
    }

}
