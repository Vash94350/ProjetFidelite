package esgi.jwm.project.loyalty.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

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
    private SharedPreferences.Editor editor;
    private ProgressBar progressBar;
    private CoreActivity coreActivity;
    private FloatingActionButton resendMailButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment=  inflater.inflate(R.layout.fragment_login, container, false);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");

        coreActivity = (CoreActivity) getActivity();

        //        get the cache of the application
        this.cache = coreActivity.cache;
        this.editor = cache.edit();


        Button buttonLogin = fragment.findViewById(R.id.button_login);
        progressBar = fragment.findViewById(R.id.progressBar);
        resendMailButton = fragment.findViewById(R.id.floatingActionButton);

        email = fragment.findViewById(R.id.email);
        pwd = fragment.findViewById(R.id.password);
        textViewResult = fragment.findViewById(R.id.result);

        if(((CoreActivity) getActivity()).getModeLogged()){
            serverHandler = new ServerHandler(new ServerHandlerPersonTest(getContext()));
        } else {
            serverHandler = new ServerHandler(new ServerHandlerCompanyTest(getContext()));
        }

        if(typeface != null){
            buttonLogin.setTypeface(typeface);
            buttonLogin.setOnClickListener(v -> {
                try {
                    onClickLogin(v);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        resendMailButton.setOnClickListener(v -> {
            onClickResendMail();
        });

        return fragment;
    }

    public void onClickLogin(View v) throws InterruptedException {

        progressBar.setVisibility(View.VISIBLE);
        String mail = String.valueOf(email.getText());
        String password = String.valueOf(pwd.getText());

        if (TextUtils.isEmpty(mail)) {
            email.setError("Please enter your mail");
            email.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pwd.setError("Please enter your password");
            pwd.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }


        serverHandler.logIn(mail, password, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                if (((CoreActivity) getActivity()).getModeLogged()) {
//                    Person connected
                    try {
                        editor.putInt(getContext().getString(R.string.personId_data_label), result.getInt(getContext().getString(R.string.personId_data_label)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
//                    Company connected
                    try {
                        editor.putInt(getContext().getString(R.string.companyId_data_label), result.getInt(getContext().getString(R.string.companyId_data_label)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                try {
                    editor.putString(getContext().getString(R.string.token), result.getString(getContext().getString(R.string.token)));
                    editor.putString(getContext().getString(R.string.QRtoken), result.getString(getContext().getString(R.string.QRtoken)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (editor.commit()) {
                    Toast.makeText(getContext(), "cache updated", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    coreActivity.setupFragment(new ConnectedPersonFragment());

                } else {
                    Toast.makeText(getContext(), "cache NOT updated", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);

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

                if (error.networkResponse.statusCode == 403) {

//                    create a Yes No dialog
                    View v = getLayoutInflater().inflate(R.layout.yes_no_dialog, null);
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

                    TextView dialogTitle = v.findViewById(R.id.titleDialog);
                    dialogTitle.setText("Oops !");

                    TextView dialogMessage = v.findViewById(R.id.dialogMessage);
                    dialogMessage.setText("Your email seem not te be verified ... Send verification ?");
                    Button buttonYes = v.findViewById(R.id.buttonYes);
                    Button buttonNo = v.findViewById(R.id.buttonNo);

                    buttonYes.setOnClickListener(v1 -> serverHandler.resendMail(mail, new APICallback() {
                        @Override
                        public void onSuccessResponse(JSONObject result) {
                            Toast.makeText(getActivity().getApplicationContext(), "Check your Mail", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();

                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onErrorResponse(VolleyError error1) {
                            try {
                                String errorString1 = "ERROR " + error1.networkResponse.statusCode + " : " + new String(error1.networkResponse.data, "UTF-8");
                                Toast.makeText(getActivity().getApplicationContext(), errorString1, Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }));

                    buttonNo.setOnClickListener(v12 -> {
                                alertDialog.dismiss();
                                progressBar.setVisibility(View.INVISIBLE);
                            });

                    alertDialog.setView(v);
                    alertDialog.show();


                }
            }
        });
    }

    public void onClickResendMail(){

        View v = getLayoutInflater().inflate(R.layout.fragment_basic_dialog,null);
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
//        v.findViewById(R.id.button)
        TextView dialogTitle = v.findViewById(R.id.titleDialog);
        dialogTitle.setText("Mail verification");

        TextView dialogMessage = v.findViewById(R.id.dialogMessage);
        dialogMessage.setText("Please enter your mail below");

        TextInputEditText email = v.findViewById(R.id.email);

        Button buttonOk = v.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(v1 -> {
            if(email.getText().length() == 0){
                Toast.makeText(getActivity().getApplicationContext(), "Mail is empty", Toast.LENGTH_SHORT).show();
            } else {
                serverHandler.resendMail(String.valueOf(email.getText()), new APICallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        Toast.makeText(getActivity().getApplicationContext(), "Check your Mail", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String errorString = "ERROR " + error.networkResponse.statusCode + " : " + new String(error.networkResponse.data, "UTF-8");
                            Toast.makeText(getActivity().getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        Button buttonCancel = v.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(v12 -> alertDialog.dismiss());

        alertDialog.setView(v);
        alertDialog.show();  //<-- See This!

    }
}
