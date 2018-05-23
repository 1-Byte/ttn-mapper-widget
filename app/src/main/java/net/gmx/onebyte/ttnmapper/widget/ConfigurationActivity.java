package net.gmx.onebyte.ttnmapper.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

public class ConfigurationActivity extends Activity {
    private int mAppWidgetId;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            finish();
            return;
        }
        mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        setContentView(R.layout.configuration);

        this.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String device = ((EditText) findViewById(R.id.device)).getText().toString();
                if (device.isEmpty()) {
                    return;
                }
                configured(device);
            }
        });
    }

    private void configured(String device) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ConfigurationActivity.this);

        RemoteViews views = new RemoteViews(ConfigurationActivity.this.getPackageName(),
                R.layout.widget);
        views.setCharSequence(R.id.device, "setText", device);
        PendingIntent click = PendingIntent.getService(this, mAppWidgetId, UpdateService.getUpdateIntent(this, mAppWidgetId, device), PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.layout, click);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
        Bundle options = new Bundle();
        options.putString("device", device);
        appWidgetManager.updateAppWidgetOptions(mAppWidgetId, options);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);

        finish();
    }
}
