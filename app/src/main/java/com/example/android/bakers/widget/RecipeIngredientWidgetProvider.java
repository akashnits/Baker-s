package com.example.android.bakers.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakers.R;
import com.example.android.bakers.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientWidgetProvider extends AppWidgetProvider {

    private static RemoteViews getIngredientListRemoteView(Context context, List<String> ingredientName){

        RemoteViews views= new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        Intent intent= new Intent(context, ListWidgetService.class);
        intent.putStringArrayListExtra("ingredientName", (ArrayList<String>) ingredientName);
        views.setRemoteAdapter(R.id.widget_list_view, intent);
        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);
        return views;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, List<String> ingredientName,
                                int[] appWidgetIds) {

        for(int appWidgetId: appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = getIngredientListRemoteView(context, ingredientName);
            Log.v("", "updating widget");

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
       // UpdateWidgetService.startUpdatingWidget(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

