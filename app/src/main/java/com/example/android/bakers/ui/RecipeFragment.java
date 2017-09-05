package com.example.android.bakers.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.bakers.R;
import com.example.android.bakers.adapters.RecipeAdapter;
import com.example.android.bakers.model.Ingredient;
import com.example.android.bakers.model.Recipe;
import com.example.android.bakers.utils.ApiService;
import com.example.android.bakers.widget.UpdateWidgetService;
import com.jakewharton.espresso.OkHttp3IdlingResource;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;
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
    private static IdlingResource mResource;



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

        OkHttpClient client= new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        mResource= OkHttp3IdlingResource.create("OkHttp", client);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .client(client)
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
        pbLoadingIndicator.setVisibility(View.VISIBLE);

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
                pbLoadingIndicator.setVisibility(View.GONE);
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
        Recipe recipe= mRecipeList.get(position);
        b.putParcelable("recipeData",recipe);
        ((MainActivity) getActivity()).showRecipeDetailsFragment(b);
        List<Ingredient> ingredientList= recipe.getIngredients();
        List<String> ingredientNameList= new ArrayList<>();

        for(int i=0; i < ingredientList.size(); i++){
            ingredientNameList.add(ingredientList.get(i).getIngredient());
        }
        UpdateWidgetService.startUpdatingWidget(getContext(), ingredientNameList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static IdlingResource getmResource(){
        return mResource;
    }
}
