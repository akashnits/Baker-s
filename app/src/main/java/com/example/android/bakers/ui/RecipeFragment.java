package com.example.android.bakers.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.bakers.R;
import com.example.android.bakers.adapters.RecipeAdapter;
import com.example.android.bakers.model.Recipe;
import com.example.android.bakers.utils.ApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecipeFragment extends Fragment implements RecipeAdapter.OnRecipeClickHandler {

    public static final String TAG = RecipeFragment.class.getSimpleName();
    @BindView(R.id.rvRecipe)
    RecyclerView rvRecipe;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar pbLoadingIndicator;
    Unbinder unbinder;

    private ApiService apiService;
    private List<Recipe> mRecipeList;
    private boolean twoPaneLayout;
    private boolean mTwoPane= false;


    public RecipeFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mTwoPane= getResources().getBoolean(R.bool.twoPaneMode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        twoPaneLayout = getResources().getBoolean(R.bool.twoPaneMode);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Call<List<Recipe>> call = apiService.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    mRecipeList = response.body();
                    RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(), mRecipeList, RecipeFragment.this);

                    if (twoPaneLayout) {
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                        rvRecipe.setLayoutManager(gridLayoutManager);
                    } else {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rvRecipe.setLayoutManager(linearLayoutManager);
                    }
                    rvRecipe.setAdapter(recipeAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.v(TAG, "Failed to load");
            }
        });
    }

    @Override
    public void onRecipeClickListener(int position) {
        Bundle b = new Bundle();
        b.putParcelable("recipeData", mRecipeList.get(position));
        ((MainActivity) getActivity()).showRecipeDetailsFragment(b);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
