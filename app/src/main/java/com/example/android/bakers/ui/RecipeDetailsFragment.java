package com.example.android.bakers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.bakers.R;
import com.example.android.bakers.adapters.RecipeDetailsAdapter;
import com.example.android.bakers.model.Recipe;
import com.example.android.bakers.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.OnRecipeStepClickHandler{

    public static final String TAG = RecipeDetailsFragment.class.getSimpleName();
    @BindView(R.id.rvRecipeDetails)
    RecyclerView rvRecipeDetails;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar pbLoadingIndicator;
    Unbinder unbinder;

    private RecipeDetailsAdapter mAdapter;
    private Recipe mRecipe;
    private boolean mTwoPane= false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ActionBar actionBar= ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_details_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        mTwoPane= getResources().getBoolean(R.bool.twoPaneMode);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args= getArguments();
        try{
            mRecipe= args.getParcelable("recipeData");
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        mAdapter= new RecipeDetailsAdapter(getContext(), mRecipe, RecipeDetailsFragment.this);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvRecipeDetails.setLayoutManager(linearLayoutManager);
        rvRecipeDetails.hasFixedSize();

        rvRecipeDetails.setAdapter(mAdapter);
        if(mTwoPane){
        getActivity().findViewById(R.id.videoDescriptionFragment_container).setVisibility(View.VISIBLE);

        if(savedInstanceState == null) {
            RecipeStepDetailsFragment recipeStepDetailsFragment = RecipeStepDetailsFragment.newInstance(0,
                    (ArrayList<Step>) mRecipe.getSteps());

            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.videoDescriptionFragment_container,
                    recipeStepDetailsFragment).commit();
        }
        }
    }

    @Override
    public void onRecipeStepClickListener(int position, int numberOfIngredients) {
        Bundle b= new Bundle();
        ArrayList<Step> stepArrayList= (ArrayList<Step>) mRecipe.getSteps();
        int actualPosition= position- numberOfIngredients;
        b.putParcelableArrayList("stepArrayList", stepArrayList);
        b.putInt("stepPosition", actualPosition);
        if(!mTwoPane) {
            Intent intent = new Intent(getActivity(), RecipeStepDetailsActivity.class);
            intent.putExtra("data", b);
            startActivity(intent);
        }else {
            RecipeStepDetailsFragment recipeStepDetailsFragment= RecipeStepDetailsFragment.newInstance(actualPosition,
                    stepArrayList);

            FragmentManager fm= getFragmentManager();
            fm.beginTransaction().replace(R.id.videoDescriptionFragment_container,
                    recipeStepDetailsFragment).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ActionBar actionBar= ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.show();
        }
        unbinder.unbind();
    }
}
