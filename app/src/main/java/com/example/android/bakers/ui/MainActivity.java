package com.example.android.bakers.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakers.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            RecipeFragment recipeFragment= new RecipeFragment();
            FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, recipeFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }

    public void showRecipeDetailsFragment(Bundle b){
        RecipeDetailsFragment recipeDetailsFragment= new RecipeDetailsFragment();
        recipeDetailsFragment.setArguments(b);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recipeDetailsFragment)
        .addToBackStack(null)
        .commit();
    }

    public void showRecipeStepDetailsFragment(Bundle b, boolean addToBackStack){
        RecipeStepDetailsFragment recipeStepDetailsFragment= new RecipeStepDetailsFragment();
        recipeStepDetailsFragment.setArguments(b);


        if(addToBackStack) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recipeStepDetailsFragment, "recipeStepDetailsFragment")
                    .addToBackStack("recipeStepDetailsFragment")
                    .commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recipeStepDetailsFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList= getSupportFragmentManager().getFragments();
        boolean flag= false;
        if(fragmentList.size() > 0) {
            for (Fragment fragment : fragmentList) {
                if (fragment != null && fragment.getTag() != null) {
                        if (fragment.getTag().equals("recipeStepDetailsFragment")) {
                            getSupportFragmentManager().popBackStack("recipeStepDetailsFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            flag = true;
                    }
                }
            }
        }


        if(!flag)
            super.onBackPressed();
    }
}
