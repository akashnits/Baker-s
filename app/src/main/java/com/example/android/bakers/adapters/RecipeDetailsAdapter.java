package com.example.android.bakers.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakers.R;
import com.example.android.bakers.model.Ingredient;
import com.example.android.bakers.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<Recipe> mRecipeList;

    public RecipeDetailsAdapter(Context mContext, List<Recipe> mRecipeList) {
        this.mContext = mContext;
        this.mRecipeList = mRecipeList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View viewIngredients = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredients, parent, false);
                return new IngredientsViewHolder(viewIngredients);
            case 1:
                View viewSteps = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_steps, parent, false);
                return new StepsViewHolder(viewSteps);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Recipe recipe = mRecipeList.get(position);
        int numberOfRowsOfIngredients = 0;
        if (recipe != null) {
            List<Ingredient> ingredientList = recipe.getIngredients();
            if (ingredientList != null) {
                numberOfRowsOfIngredients = ingredientList.size();
            }
        }
        if (position < numberOfRowsOfIngredients) {
            return 0;
        } else {
            return 1;
        }
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvIngredientName)
        TextView tvIngredientName;
        @BindView(R.id.tvQuantity)
        TextView tvQuantity;
        @BindView(R.id.tvMeasure)
        TextView tvMeasure;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class StepsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.idStepShortDescription)
        TextView idStepShortDescription;
        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
