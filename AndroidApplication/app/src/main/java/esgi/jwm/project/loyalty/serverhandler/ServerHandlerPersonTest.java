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

    //    private final String ipServ = "10.0.2.2";
    private final String ipServ = "192.168.0.46";
    private final String portNodeJs = ":3000";
    private final String person = "persons";
    private final String urlNodeJs = "http://" +ipServ + portNodeJs + "/" + person;

    private HashMap<String, Object> listParams;
    private RequestQueue requestQueue;
    private Context context;

    public ServerHandlerPersonTest(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);

    }

    @Override
    public void login(String mail, String password, APICallback apiCallback) {

        listParams = prepareHashMap("email", mail, "password", password);
        JSONObject parameters = new JSONObject(listParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlNodeJs + "/login",parameters,
                apiCallback::onSuccessResponse,
                apiCallback::onErrorResponse
        );

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void register(String mail, String password, String telephone, String firstname,
                         String lastname, String sex, String birthDate, String streetNumber,
                         String route, String zipCode, String city, String country, APICallback callback) {

        listParams = prepareHashMap(context.getString(R.string.email_data_label), mail,
                context.getString(R.string.password_data_label), password,
                context.getString(R.string.telephone_data_label), telephone,
                context.getString(R.string.firstname_data_label), firstname,
                context.getString(R.string.lastname_data_label), lastname,
                context.getString(R.string.sex_data_label), sex,
                context.getString(R.string.birthdate_data_label), birthDate,
                context.getString(R.string.streetNumber_data_label), streetNumber,
                context.getString(R.string.route_data_label), route,
                context.getString(R.string.zipCode_data_label), zipCode,
                context.getString(R.string.city_data_label), city,
                context.getString(R.string.country_data_label), country);

        JSONObject parameters = new JSONObject(listParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlNodeJs + "/register",parameters,
                callback::onSuccessResponse,
                callback::onErrorResponse
        );

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void resendMail(String mail, APICallback callback) {
        listParams = prepareHashMap("userEmail", mail, "userType", person);
        JSONObject parameters = new JSONObject(listParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://" + ipServ + portNodeJs + "/email/resend",parameters,
                callback::onSuccessResponse,
                callback::onErrorResponse
        );

        requestQueue.add(jsonObjectRequest);

    }

}
