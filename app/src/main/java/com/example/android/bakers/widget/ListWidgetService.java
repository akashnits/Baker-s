package com.example.android.bakers.widget;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakers.R;
import com.example.android.bakers.model.Ingredient;
import com.example.android.bakers.model.Recipe;

import java.util.List;

public class ListWidgetService extends RemoteViewsService {

    static Intent mIntent;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        mIntent= intent;
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private Context mContext;
    private List<String> mIngredientName;

    public ListRemoteViewsFactory(Context context) {

        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mIngredientName= ListWidgetService.mIntent.getStringArrayListExtra("ingredientName");
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mIngredientName == null)
            return 0;
        return mIngredientName.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_ingredient_widget);

        views.setTextViewText(R.id.appwidget_text, mIngredientName.get(position));
        Log.v("", "getViewAtCalled");
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
