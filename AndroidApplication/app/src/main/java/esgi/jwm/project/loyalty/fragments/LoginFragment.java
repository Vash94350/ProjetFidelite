package esgi.jwm.project.loyalty.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import esgi.jwm.project.loyalty.viewtools.DialogBoxBuilder;
import esgi.jwm.project.loyalty.viewtools.Keyboard;


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
    private FloatingActionButton resendPasswordButton;

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
        resendPasswordButton = fragment.findViewById(R.id.floatingActionButton);

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

        resendPasswordButton.setOnClickListener(v -> {
            onClickResendPasswordButton();
        });

        return fragment;
    }

    public void onClickLogin(View v) throws InterruptedException {

        Keyboard.hide(getActivity(), v);


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

//                TODO Implement a Error enumeration with the code dans the message
                String message_error = "";

                if(error.networkResponse.statusCode == 500)
                    message_error = getString(R.string.dialog_message_error_unable_to_verify_account);

                if(error.networkResponse.statusCode == 404)
                    message_error = getString(R.string.dialog_message_error_account_do_not_exist);

                if(error.networkResponse.statusCode == 403)
                    message_error = getString(R.string.dialog_message_error_password_invalid_for_this_account);

                if(error.networkResponse.statusCode == 402)
                    message_error = getString(R.string.dialog_message_error_account_not_verified);


                AlertDialog alertDialog = DialogBoxBuilder.build(getActivity(), getString(R.string.dialog_title_error), message_error,
                        getString(R.string.cancel_button), getString(R.string.ok_button), getLayoutInflater(),
                        DialogBoxBuilder.NO_YES_DIALOG, new IBasicDialogCallBack() {
                            @Override
                            public void onClickButton1(View view, AlertDialog dialogBox) {
                                progressBar.setVisibility(View.INVISIBLE);
                                dialogBox.dismiss();
                            }

                            @Override
                            public void onClickButton2(View view, String res, AlertDialog dialogBox) {
//                                here we don't care about res cause it will always be empty with DialogBuilder.NO_YES_DIALOG
                                if(error.networkResponse.statusCode == 402){
                                    serverHandler.resendMail(mail, new APICallback() {
                                        @Override
                                        public void onSuccessResponse(JSONObject result) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getActivity().getApplicationContext(), "Check your Mail", Toast.LENGTH_SHORT).show();
                                            dialogBox.dismiss();
                                        }

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            try {
                                                String errorString = "ERROR " + error.networkResponse.statusCode + " : " + new String(error.networkResponse.data, "UTF-8");
                                                Toast.makeText(getActivity().getApplicationContext(), errorString , Toast.LENGTH_SHORT).show();
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    dialogBox.dismiss();
                                }
                            }
                        });
                alertDialog.show();

            }
        });
    }

    public void onClickResendPasswordButton(){

        AlertDialog alertDialog = DialogBoxBuilder.build(getActivity(), getString(R.string.dialog_title_reset_password),
                getString(R.string.dialog_message_reset_password), getString(R.string.cancel_button),
                getString(R.string.ok_button), getLayoutInflater(), DialogBoxBuilder.BASIC_DIALOG, new IBasicDialogCallBack() {
                    @Override
                    public void onClickButton1(View view, AlertDialog dialogBox) {
                        dialogBox.dismiss();
                    }

                    @Override
                    public void onClickButton2(View view, String res, AlertDialog dialogBox) {
                        if(TextUtils.isEmpty(res)) {
                            Toast.makeText(getActivity().getApplicationContext(), "Mail is empty", Toast.LENGTH_SHORT).show();
                        } else {
                            serverHandler.resetPassword(res, new APICallback() {
                                @Override
                                public void onSuccessResponse(JSONObject result) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Check your Mail", Toast.LENGTH_SHORT).show();
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
                            dialogBox.dismiss();
                        }
                    }
                });

        alertDialog.show();
    }
}
