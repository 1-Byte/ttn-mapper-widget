package net.gmx.onebyte.ttnmapper.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import net.gmx.onebyte.ttnmapper.Gateway;
import net.gmx.onebyte.ttnmapper.Gateways;
import net.gmx.onebyte.ttnmapper.Location;
import net.gmx.onebyte.ttnmapper.Logs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateService extends IntentService {
    private static final String TAG = "UpdateService";
    private static final String ENDPOINT = "https://ttnmapper.org/csv_node_date.php?gateways=on&csv=";

    private OkHttpClient mClient;

    public UpdateService() {
        super(TAG);
        mClient = new OkHttpClient();
    }

    public static Intent getUpdateIntent(Context context, int widget, String device) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction("update");
        intent.putExtra("widget", widget);
        intent.putExtra("device", device);
        return intent;
    }

    public static PendingIntent getUpdatePendingIntent(Context context, int widget, String device) {
        return PendingIntent.getService(context, widget, UpdateService.getUpdateIntent(context, widget, device), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int widget = intent.getIntExtra("widget", 0);
        Log.w(TAG, "Update " + widget);
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget);
        view.setInt(R.id.status, "setText", R.string.updating);
        AppWidgetManager.getInstance(this).updateAppWidget(widget, view);

        String device = intent.getStringExtra("device");
        Request request = buildRequest(device, new Date());
        Log.i(TAG, request.url().toString());
        try {
            Response response = mClient.newCall(request).execute();
            String csv = response.body().string();
            Log.i(TAG, "Response Length: " + csv.length());
            List<net.gmx.onebyte.ttnmapper.Log> logs = Logs.parse(csv);
            net.gmx.onebyte.ttnmapper.Log last = !logs.isEmpty() ? logs.get(logs.size() - 1) : null;
            view.setCharSequence(R.id.count, "setText", Integer.toString(logs.size()));
            view.setCharSequence(R.id.status, "setText", last != null ? last.getTime() : "");
            view.setCharSequence(R.id.position, "setText", last != null ? formatLocation(last.getLocation()) : "");
            view.setCharSequence(R.id.gateways, "setText", last != null ? formatGateway(last.getGateway(), last.getLocation()) : "");
            view.setCharSequence(R.id.signal, "setText", last != null ? String.format("SNR:\u00A0%.1f, RSSI:\u00A0%.1f, HDOP:\u00A0%.1f, Satellites:\u00A0%d", last.getSnr(), last.getRssi(), last.getHdop(), last.getSatellites()) : "");
            AppWidgetManager.getInstance(this).updateAppWidget(widget, view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Request buildRequest(String node, Date date) {
        HttpUrl url = HttpUrl.parse(ENDPOINT).newBuilder()
                .setQueryParameter("node", node)
                .setQueryParameter("date", new SimpleDateFormat("yyyy-MM-dd").format(date))
                .build();
        return new Request.Builder().url(url).build();
    }

    private String formatLocation(Location location) {
        return String.format("%.6f° N, %.6f° E, %.1fm", location.getLatitude(), location.getLongitude(), location.getAltitude());
    }

    private String formatGateway(String gatewayId, Location nodeLocation) {
        String result = gatewayId;
        Gateway gateway = Gateways.load(this).get(gatewayId);
        if (gateway != null && gateway.getLocation() != null && nodeLocation != null) {
            result += String.format(" (%.0fm)", nodeLocation.distanceTo(gateway.getLocation()));
        }
        return result;
    }
}
