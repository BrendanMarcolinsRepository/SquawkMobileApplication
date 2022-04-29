package com.example.a321projectprototype.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;

public class Widget extends AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for(int id: appWidgetIds){
            boolean open = true;
            Intent intent = new Intent(context, HomePage.class);
            intent.putExtra("widget",open);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.widgetRecord,pendingIntent);

            appWidgetManager.updateAppWidget(id, views);

        }
    }
}
