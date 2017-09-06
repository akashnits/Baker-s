package com.example.android.bakers.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.android.bakers.R;
import com.example.android.bakers.widget.UpdateWidgetService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener{

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTwoPane = getResources().getBoolean(R.bool.twoPaneMode);

        if(mTwoPane){
            getSupportFragmentManager().addOnBackStackChangedListener(this);
            shouldDisplayHomeUp();
        }
        if (savedInstanceState == null) {
            RecipeFragment recipeFragment = new RecipeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, recipeFragment);
            fragmentTransaction.addToBackStack(getString(R.string.recipeFragment));
            fragmentTransaction.commit();

        }
    }

    public void showRecipeDetailsFragment(Bundle b) {
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        recipeDetailsFragment.setArguments(b);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recipeDetailsFragment)
                .addToBackStack(getString(R.string.recipeDetailsFragment))
                .commit();
    }



    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        FragmentManager fm = getSupportFragmentManager();
        if (!mTwoPane) {
            if (count == 0) {
                super.onBackPressed();
                UpdateWidgetService.startUpdatingWidget(this, new ArrayList<String>());
                if (fm.findFragmentById(R.id.fragment_container) == null) {
                    finish();
                }
            } else {
                getFragmentManager().popBackStack();
            }
        } else {
            Fragment f = fm.findFragmentById(R.id.videoDescriptionFragment_container);

            if (f instanceof RecipeStepDetailsFragment) {
                this.findViewById(R.id.videoDescriptionFragment_container).setVisibility(View.GONE);
            if (count == 0) {
                super.onBackPressed();
                UpdateWidgetService.startUpdatingWidget(this, new ArrayList<String>());
                if (fm.findFragmentById(R.id.fragment_container) == null) {
                    finish();
                }
            } else {
                    getFragmentManager().popBackStack();
                }
            }
        }
    }

    public void shouldDisplayHomeUp(){
        FragmentManager fm= getSupportFragmentManager();
        Fragment f= fm.findFragmentById(R.id.fragment_container);
        if(f instanceof RecipeDetailsFragment){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
