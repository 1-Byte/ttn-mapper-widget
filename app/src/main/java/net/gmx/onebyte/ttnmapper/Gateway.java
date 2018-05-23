package net.gmx.onebyte.ttnmapper;

import org.json.JSONException;
import org.json.JSONObject;

public class Gateway {
    private JSONObject data;

    public Gateway(JSONObject data) {
        this.data = data;
    }

    public Location getLocation() {
        try {
            JSONObject obj = data.getJSONObject("location");
            return new Location(
                    obj.getDouble("latitude"),
                    obj.getDouble("longitude"),
                    obj.getDouble("altitude")
            );
        } catch (JSONException e) {
            return null;
        }
    }
}
