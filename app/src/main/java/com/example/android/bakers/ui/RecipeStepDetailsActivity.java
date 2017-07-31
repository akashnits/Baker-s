package com.example.android.bakers.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.example.android.bakers.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);
        ButterKnife.bind(this);

        myAdapter = new ViewPagerAdapter();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    mStepId--;
                    ((MainActivity) getActivity()).showRecipeStepDetailsFragment(getBundle());
                }

            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExoPlayer != null && mStepId != mStepArrayList.size()-1)
                    mExoPlayer.stop();
                if(mStepArrayList != null && mStepId != mStepArrayList.size()-1){
                    mStepId++;
                    ((MainActivity) getActivity()).showRecipeStepDetailsFragment(getBundle());
                }
            }
        });
    }

    private Bundle getBundle(){
        Bundle bundle= new Bundle();
        bundle.putParcelableArrayList("stepArrayList", mStepArrayList);
        bundle.putInt("stepPosition", mStepId);
        return bundle;
    }
    
}
