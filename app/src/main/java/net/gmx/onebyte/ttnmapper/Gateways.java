package net.gmx.onebyte.ttnmapper;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kotlin.io.FilesKt;
import kotlin.text.Charsets;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Gateways {
    private static final String TAG = "Gateways";
    private static final String ENDPOINT = "https://www.thethingsnetwork.org/gateway-data/";
    private static Gateways instance;
    private File file;
    private JSONObject data;

    protected Gateways(File file) {
        this.file = file;
        if (file.exists()) {
            try {
                Log.i(TAG, "Load " + file);
                data = new JSONObject(FilesKt.readText(file, Charsets.UTF_8));
            } catch (JSONException e) {
                e.printStackTrace();
                data = new JSONObject();
            }
        } else {
            update();
        }
    }

    public static synchronized Gateways load(Context context) {
        if (instance == null) {
            instance = new Gateways(context.getFileStreamPath("gateways.json"));
        }
        return instance;
    }

    public Gateway get(String id) {
        try {
            String euiId = "eui-" + id.toLowerCase();
            return new Gateway(data.getJSONObject(data.has(euiId) ? euiId : id));
        } catch (JSONException e) {
            return null;
        }
    }

    public void update() {
        OkHttpClient client = new OkHttpClient();
        try {
            Log.i(TAG, "Download " + ENDPOINT);
            Response response = client.newCall(new Request.Builder().url(ENDPOINT).build()).execute();
            byte[] json = response.body().bytes();
            data = new JSONObject(new String(json));
            try (FileOutputStream fileStream = new FileOutputStream(file)) {
                fileStream.write(json);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            data = new JSONObject();
        }
    }
}
