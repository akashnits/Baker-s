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
import com.example.android.bakers.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private Recipe mRecipe;
    private OnRecipeStepClickHandler mHandler;
    private int mNumberOfIngredients;

    public RecipeDetailsAdapter(Context mContext, Recipe recipe, OnRecipeStepClickHandler handler) {
        this.mContext = mContext;
        this.mRecipe = recipe;
        this.mHandler= handler;
    }

    public interface OnRecipeStepClickHandler{
        void onRecipeStepClickListener(int position, int numberOfIngredients);
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
        switch (holder.getItemViewType()){
            case 0:
                IngredientsViewHolder ingredientsViewHolder= (IngredientsViewHolder) holder;
                List<Ingredient> ingredientList= null;
                ingredientList= mRecipe.getIngredients();
                Ingredient ingredient= null;
                if(ingredientList != null && ingredientList.size() > 0){
                    ingredient= ingredientList.get(position);
                }
                if(ingredient != null){
                    ingredientsViewHolder.tvIngredientName.setText(ingredient.getIngredient());
                    ingredientsViewHolder.tvQuantity.setText(String.valueOf(ingredient.getQuantity()));
                    ingredientsViewHolder.tvMeasure.setText(ingredient.getMeasure());
                }
                break;
            case 1:
                StepsViewHolder stepsViewHolder= (StepsViewHolder) holder;
                List<Ingredient> ingredientLists= null;
                ingredientLists= mRecipe.getIngredients();
                List<Step> stepList= null;
                stepList= mRecipe.getSteps();
                Step step= null;
                if(stepList != null && stepList.size() > 0){
                    step= stepList.get(position- ingredientLists.size());
                }
                if(step != null){
                    stepsViewHolder.tvStepShortDescription.setText(step.getShortDescription());
                }
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        if(mRecipe == null)
            return 0;
        return (mRecipe.getIngredients().size() + mRecipe.getSteps().size());
    }

    @Override
    public int getItemViewType(int position) {
        if (mRecipe != null) {
            List<Ingredient> ingredientList = mRecipe.getIngredients();
            if (ingredientList != null) {
                mNumberOfIngredients = ingredientList.size();
            }
        }
        if (position < mNumberOfIngredients) {
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
        @BindView(R.id.tvStepShortDescription)
        TextView tvStepShortDescription;
        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvStepShortDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandler.onRecipeStepClickListener(getAdapterPosition(), mNumberOfIngredients);
                }
            });
        }
    }
}
