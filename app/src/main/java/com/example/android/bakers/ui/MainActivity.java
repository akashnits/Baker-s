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
}
