package esgi.jwm.project.loyalty.serverhandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import esgi.jwm.project.loyalty.R;

import static esgi.jwm.project.loyalty.serverhandler.ServerHandler.prepareHashMap;

/**
 * Created by wmorgado on 14/02/2018.
 */

public class ServerHandlerPersonTest implements IServerHandlerConnection {

    private final String ipServ = "10.0.2.2";
    private final String portNodeJs = ":3000";
    private final String person = "persons";
    private final String urlNodeJs = "http://" +ipServ + portNodeJs + "/" + person;

    private Gson gson = new GsonBuilder().create();
    private HttpResponse<JsonNode> response = null;
    private JSONArray jsonArray = null;
    private HashMap<String, Object> listParams;
    private JSONObject actualJsonLine = null;
    private RequestQueue requestQueue;
    private Context context;
    private SharedPreferences cache;
    private SharedPreferences.Editor editor;

    public ServerHandlerPersonTest(Context context, SharedPreferences cache) {
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
                        editor.putInt(context.getString(R.string.person_id), response.getInt(context.getString(R.string.person_id)));
                        editor.putString(context.getString(R.string.token), response.getString(context.getString(R.string.token)));

                        if(editor.commit()){
                            Toast.makeText(context, "cache updated", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "cache NOT updated", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    apiCallback.onSuccessResponse(response);
                },

                error -> {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    editor.putInt(context.getString(R.string.person_id), 0);
                    editor.putInt(context.getString(R.string.error_code), error.networkResponse.statusCode);
                    editor.putString(context.getString(R.string.error_message), error.getMessage());

                    if(editor.commit()){
                        Toast.makeText(context, "cache updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "cache NOT updated", Toast.LENGTH_LONG).show();
                    }
                    apiCallback.onErrorResponse(error);

                }
        );

        requestQueue.add(jsonObjectRequest);
//        requestQueue.start();

//        response = Unirest.post(urlNodeJs + "/login").fields(listParams).asJson();
//        Log.d("jsonArray : ", jsonArray.toString());
    }

    @Override
    public boolean register(String mail, String password, String telephone, String firstname, String lastname, String sex, String birthDate, String streetNumber, String route, String zipCode, String city, String Country, APICallback callback) {
        listParams = prepareHashMap("email", mail, "password", password);
        JSONObject parameters = new JSONObject(listParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlNodeJs + "/register",parameters,
                response -> {

                    try {
                        editor.putInt(context.getString(R.string.person_id), response.getInt(context.getString(R.string.person_id)));
                        editor.putString(context.getString(R.string.token), response.getString(context.getString(R.string.token)));

                        if(editor.commit()){
                            Toast.makeText(context, "cache updated", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "cache NOT updated", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callback.onSuccessResponse(response);
                },

                error -> {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    editor.putInt(context.getString(R.string.person_id), 0);
                    editor.putInt(context.getString(R.string.error_code), error.networkResponse.statusCode);
                    editor.putString(context.getString(R.string.error_message), error.getMessage());

                    if(editor.commit()){
                        Toast.makeText(context, "cache updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "cache NOT updated", Toast.LENGTH_LONG).show();
                    }
                    callback.onErrorResponse(error);

                }
        );
    }


}
