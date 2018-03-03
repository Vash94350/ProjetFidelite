package esgi.jwm.project.loyalty.serverhandler;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by wmorgado on 23/02/2018.
 */

public interface APICallback {
    // Handling the response from server
    void onSuccessResponse(JSONObject result);

    // Handling of error request and action on the activity
    void onErrorResponse(VolleyError error);
}