package com.example.android.bakers.adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    private OnRecipeClickHandler mHandler;
    private String[] imageUrls;

    public interface OnRecipeClickHandler{
         void onRecipeClickListener(int position);
    }

    public RecipeAdapter(Context mContext, List<Recipe> mRecipe, OnRecipeClickHandler onRecipeClickHandler) {
        this.mRecipe = mRecipe;
        this.mContext = mContext;
        this.mHandler= onRecipeClickHandler;
        imageUrls=
                new String[]{mContext.getString(R.string.NutellaPieImage),
                        mContext.getString(R.string.BrowniesImage),
                        mContext.getString(R.string.YellowCakeImage),
                        mContext.getString(R.string.CheeseCakeImage)};

    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.card_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipe.get(position);
        holder.tvRecipeName.setText(recipe.getName());
        if(recipe.getSteps() != null)
            holder.numberOfSteps.setText(mContext.getString(R.string.Steps) + String.valueOf(recipe.getSteps().size()));
        String imageUrl= recipe.getImage();
        if(imageUrl != null && imageUrl.length() > 0) {
            loadImageWithPicasso(imageUrl, holder);
        }else{
            loadImageWithPicasso(imageUrls[position], holder);
        }
    }

    private void loadImageWithPicasso(String imageUrl, RecipeViewHolder holder){
        Picasso.with(mContext).load(imageUrl)
                .fit()
                .centerCrop()
                .into(holder.ivCardRecipe);
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                int position = getAdapterPosition();
                    mHandler.onRecipeClickListener(position);
                }
            });
        }
    }
    public void setData(List<Recipe> recipe){
        mRecipe= recipe;
        notifyDataSetChanged();
    }
}
