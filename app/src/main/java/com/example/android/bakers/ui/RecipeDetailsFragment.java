package com.example.android.bakers.ui;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.android.bakers.R;
import com.example.android.bakers.adapters.RecipeDetailsAdapter;
import com.example.android.bakers.model.Recipe;
import com.example.android.bakers.model.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.OnRecipeStepClickHandler {

    public static final String TAG = RecipeDetailsFragment.class.getSimpleName();
    @BindView(R.id.rvRecipeDetails)
    RecyclerView rvRecipeDetails;

    Unbinder unbinder;


    private RecipeDetailsAdapter mAdapter;
    private Recipe mRecipe;
    private boolean mTwoPane = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_details_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        mTwoPane = getResources().getBoolean(R.bool.twoPaneMode);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            if(!mTwoPane) {
                Toolbar toolbar= (Toolbar) view.findViewById(R.id.toolbar);
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

        Bundle args = getArguments();
        try {
            mRecipe = args.getParcelable(getString(R.string.recipeData));
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        mAdapter = new RecipeDetailsAdapter(getContext(), mRecipe, RecipeDetailsFragment.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvRecipeDetails.setLayoutManager(linearLayoutManager);
        rvRecipeDetails.hasFixedSize();

        rvRecipeDetails.setAdapter(mAdapter);
        if (mTwoPane) {
            getActivity().findViewById(R.id.videoDescriptionFragment_container).setVisibility(View.VISIBLE);
            Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setTitle(mRecipe.getName());
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (savedInstanceState == null) {
                RecipeStepDetailsFragment recipeStepDetailsFragment = RecipeStepDetailsFragment.newInstance(0,
                        (ArrayList<Step>) mRecipe.getSteps(), mRecipe.getName(), getActivity());

                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.videoDescriptionFragment_container,
                        recipeStepDetailsFragment).commit();
            }
        }
        else{
            CollapsingToolbarLayout collapsingToolbarLayout= (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar_layout);

            collapsingToolbarLayout.setTitle(mRecipe.getName());
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        }
    }

    @Override
    public void onRecipeStepClickListener(int position, int numberOfIngredients, View view) {
        Bundle b = new Bundle();
        ArrayList<Step> stepArrayList = (ArrayList<Step>) mRecipe.getSteps();
        int actualPosition = position - numberOfIngredients;
        b.putParcelableArrayList(getString(R.string.stepArrayList), stepArrayList);
        b.putInt(getString(R.string.stepPosition), actualPosition);
        b.putString(getString(R.string.recipeName), mRecipe.getName());
        int finalRadius= (int) Math.hypot(view.getWidth()/2, view.getHeight()/2);
        Animator anim= ViewAnimationUtils.createCircularReveal(view,  view.getWidth()/2,  view.getHeight()/2, 0, finalRadius);
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        anim.start();
        if (!mTwoPane) {
            Intent intent = new Intent(getActivity(), RecipeStepDetailsActivity.class);
            intent.putExtra(getString(R.string.data), b);
            startActivity(intent);
        } else {
            RecipeStepDetailsFragment recipeStepDetailsFragment = RecipeStepDetailsFragment.newInstance(actualPosition,
                    stepArrayList, mRecipe.getName(), getActivity());

            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.videoDescriptionFragment_container,
                    recipeStepDetailsFragment).commit();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
            if (mTwoPane && ((AppCompatActivity) getActivity()).getSupportActionBar() != null ) {
                Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);
                toolbar.setVisibility(View.INVISIBLE);
            }
        unbinder.unbind();
    }


}
