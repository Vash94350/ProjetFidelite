package esgi.jwm.project.loyalty.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

import esgi.jwm.project.loyalty.R;
import esgi.jwm.project.loyalty.activities.CoreActivity;
import esgi.jwm.project.loyalty.serverhandler.APICallback;
import esgi.jwm.project.loyalty.serverhandler.ServerHandler;
import esgi.jwm.project.loyalty.serverhandler.ServerHandlerCompanyTest;
import esgi.jwm.project.loyalty.serverhandler.ServerHandlerPersonTest;


public class LoginFragment extends Fragment {

    private View fragment;
    private ServerHandler serverHandler;
    private Typeface typeface;
    private AutoCompleteTextView email;
    private EditText pwd;
    private TextView textViewResult;
    private SharedPreferences cache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment=  inflater.inflate(R.layout.fragment_login, container, false);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");

        Button buttonLogin = fragment.findViewById(R.id.button_login);
        email = fragment.findViewById(R.id.email);
        pwd = fragment.findViewById(R.id.password);
        textViewResult = fragment.findViewById(R.id.result);

//        get the cache of the application
        this.cache = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());

        if(typeface != null){

            buttonLogin.setTypeface(typeface);
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        onClickLogin(v);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return fragment;
    }

    public void onClickLogin(View v) throws InterruptedException, UnirestException {

//        serverHandler is a TESTING object which simulate a serverHandlerPerson
        //validating inputs

        String mail = String.valueOf(email.getText());
        String password = String.valueOf(pwd.getText());

        if (TextUtils.isEmpty(mail)) {
            email.setError("Please enter your mail");
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pwd.setError("Please enter your password");
            pwd.requestFocus();
            return;
        }

        ServerHandlerPersonTest serverHandlerPersonTest;
        if(((CoreActivity) getActivity()).getModeLogged()){
            serverHandler = new ServerHandler(new ServerHandlerPersonTest(getContext(),cache));

        } else {
            serverHandler = new ServerHandler(new ServerHandlerCompanyTest(getContext(), cache));
        }

//        serverHandler.logIn(mail, password);

        serverHandler.logIn(mail, password, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                if(((CoreActivity) getActivity()).getModeLogged()){
                    if(cache.getInt(getContext().getString(R.string.person_id),0) > 0){
                        ((CoreActivity) getActivity()).setupFragment(new ConnectedPersonFragment());
                    }
                } else {
                    if(cache.getInt(getContext().getString(R.string.company_id),0) > 0){
                        ((CoreActivity) getActivity()).setupFragment(new ConnectedPersonFragment());
                    }
                }

            }

            @Override
            public void onErrorResponse(VolleyError error) {
                String errorString = "";
                try {
                    errorString = "ERROR " + error.networkResponse.statusCode + " : " + new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                textViewResult.setVisibility(View.VISIBLE);
                textViewResult.setText(errorString);
            }
        });

    }
}
