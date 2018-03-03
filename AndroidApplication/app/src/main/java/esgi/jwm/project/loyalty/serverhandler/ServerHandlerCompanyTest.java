package esgi.jwm.project.loyalty.serverhandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import esgi.jwm.project.loyalty.R;

import static esgi.jwm.project.loyalty.serverhandler.ServerHandler.prepareHashMap;

/**
 * Created by wmorgado on 15/02/2018.
 */

public class ServerHandlerCompanyTest implements IServerHandlerConnection{

    private final String ipServ = "10.0.2.2";
    private final String portNodeJs = ":3000";
    private final String company = "companies";
    private final String urlNodeJs = "http://" +ipServ + portNodeJs + "/" + company;

    private final Context context;
    private final RequestQueue requestQueue;
    private final SharedPreferences cache;
    private final SharedPreferences.Editor editor;
    private HashMap<String, Object> listParams;

    public ServerHandlerCompanyTest(Context context, SharedPreferences cache) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.cache = cache;
        this.editor = this.cache.edit();
    }

    @Override
    public void login(String mail, String password, APICallback apiCallback) {


        listParams = prepareHashMap("email", mail, "password", password);
        JSONObject parameters = new JSONObject(listParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlNodeJs + "/login",parameters,
                response -> {

                    try {
                        editor.putInt(context.getString(R.string.company_id), response.getInt(context.getString(R.string.company_id)));
                        editor.putString(context.getString(R.string.token), response.getString(context.getString(R.string.token)));

                        if(editor.commit()){
                            apiCallback.onSuccessResponse(response);
                        } else {
                            Toast.makeText(context, "cache NOT updated", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },

                error -> {
                    editor.putInt(context.getString(R.string.company_id), 0);
                    editor.putInt(context.getString(R.string.error_code), error.networkResponse.statusCode);
                    editor.putString(context.getString(R.string.error_message), error.getMessage());

                    if(editor.commit()){
                        apiCallback.onErrorResponse(error);
                    } else {
                        Toast.makeText(context, "cache NOT updated", Toast.LENGTH_LONG).show();
                    }

                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean register() {
        return false;
    }
}
