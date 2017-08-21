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

import java.util.ArrayList;
import java.util.List;


public class UpdateWidgetService extends IntentService {

    public static String ACTION_UPDATE_WIDGET= "com.example.android.bakers.widget.action.update_widget";


    private int[] mAppWidgetIds;
    private AppWidgetManager mAppWidgetManager;

    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }

    public static void startUpdatingWidget(Context context, List<String> ingredientNameList){
        Intent intent= new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        intent.putStringArrayListExtra("ingredientNameList", (ArrayList<String>) ingredientNameList);
        context.startService(intent);
}

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals(ACTION_UPDATE_WIDGET)) {
                mAppWidgetManager = AppWidgetManager.getInstance(this);
                mAppWidgetIds = mAppWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeIngredientWidgetProvider.class));
                mAppWidgetManager.notifyAppWidgetViewDataChanged(mAppWidgetIds, R.id.widget_list_view);
                List<String> ingredientNameList= intent.getStringArrayListExtra("ingredientNameList");
                RecipeIngredientWidgetProvider.updateAppWidget(this, mAppWidgetManager,ingredientNameList, mAppWidgetIds);
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent intent) {

        RecipeIngredientWidgetProvider.updateAppWidget(this, mAppWidgetManager, null, mAppWidgetIds);
        stopSelf();
        super.onTaskRemoved(intent);
    }
}
