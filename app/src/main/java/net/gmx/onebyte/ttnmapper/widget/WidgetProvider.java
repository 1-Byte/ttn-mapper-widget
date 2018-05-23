package net.gmx.onebyte.ttnmapper.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, intent.getAction());
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int id : appWidgetIds) {
            String device = appWidgetManager.getAppWidgetOptions(id).getString("device");
            Log.i(TAG, String.format("onUpdate(%d): %s", id, device));
            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget);
            if (device != null) {
                view.setCharSequence(R.id.device, "setText", device);
                view.setOnClickPendingIntent(R.id.layout, UpdateService.getUpdatePendingIntent(context, id, device));
            }
            appWidgetManager.updateAppWidget(id, view);
        }
    }
}
