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

//    private final String ipServ = "10.0.2.2";
    private final String ipServ = "192.168.0.46";
    private final String portNodeJs = ":3000";
    private final String company = "companies";
    private final String urlNodeJs = "http://" +ipServ + portNodeJs + "/" + company;

    private final Context context;
    private final RequestQueue requestQueue;
    private HashMap<String, Object> listParams;

    public ServerHandlerCompanyTest(Context context) {
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
    public void register(String mail, String password, String telephone, String firstname, String lastname, String sex, String birthDate, String streetNumber, String route, String zipCode, String city, String Country, APICallback callback) {

//        open browser
    }

    @Override
    public void resendMail(String mail, APICallback callback) {
        listParams = prepareHashMap("userEmail", mail, "userType", company);
        JSONObject parameters = new JSONObject(listParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ipServ + portNodeJs + "/email/resend",parameters,
                callback::onSuccessResponse,
                callback::onErrorResponse
        );

        requestQueue.add(jsonObjectRequest);
    }

}
