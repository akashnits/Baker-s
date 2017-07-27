package com.example.android.bakers.adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakers.R;
import com.example.android.bakers.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {


    private List<Recipe> mRecipe;
    private Context mContext;

    public RecipeAdapter(Context mContext, List<Recipe> mRecipe) {
        this.mRecipe = mRecipe;
        this.mContext = mContext;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.card_recipe, parent);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipe.get(position);
        holder.tvRecipeName.setText(recipe.getName());
        if(recipe.getSteps() != null)
            holder.numberOfSteps.setText(String.valueOf(recipe.getSteps().size()));
        String imageUrl= recipe.getImage();
        if(imageUrl != null && imageUrl.length() > 0) {
            Picasso.with(mContext).load(recipe.getImage()).into(holder.ivCardRecipe);
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipe == null)
            return 0;
        return mRecipe.size();
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvRecipeName)
        TextView tvRecipeName;
        @BindView(R.id.numberOfSteps)
        TextView numberOfSteps;
        @BindView(R.id.cardRecipe)
        CardView cardRecipe;
        @BindView(R.id.ivCardRecipe)
        ImageView ivCardRecipe;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public void setData(List<Recipe> recipe){
        mRecipe= recipe;
        notifyDataSetChanged();
    }
}
