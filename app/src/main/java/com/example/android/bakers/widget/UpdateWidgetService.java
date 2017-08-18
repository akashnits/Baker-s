package com.example.android.bakers.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakers.R;
import com.example.android.bakers.model.Recipe;


public class UpdateWidgetService extends IntentService {

    public static String ACTION_UPDATE_WIDGET= "com.example.android.bakers.widget.action.update_widget";

    private static Recipe mRecipe;


    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }

    public static void startUpdatingWidget(Context context, Recipe recipe){
        mRecipe= recipe;
        Intent intent= new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
}

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals(ACTION_UPDATE_WIDGET)) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeIngredientWidgetProvider.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
                RecipeIngredientWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetIds);
            }
        }
    }

    public static Recipe getmRecipe() {
        return mRecipe;
    }
}
