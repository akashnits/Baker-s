package com.example.android.bakers.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_details_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

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

    }

    @Override
    public void onRecipeStepClickListener(int position, int numberOfIngredients) {
        int actualPosition= position- numberOfIngredients;
        Bundle b= new Bundle();
        b.putParcelableArrayList("stepArrayList", (ArrayList<? extends Parcelable>) mRecipe.getSteps());
        b.putInt("stepPosition", actualPosition);

        ((MainActivity) getActivity()).showRecipeStepDetailsFragment(b, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
