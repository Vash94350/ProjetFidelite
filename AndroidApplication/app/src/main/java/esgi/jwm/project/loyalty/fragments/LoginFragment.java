package esgi.jwm.project.loyalty.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import esgi.jwm.project.loyalty.R;
import esgi.jwm.project.loyalty.activities.CoreActivity;
import esgi.jwm.project.loyalty.serverhandler.ServerHandler;
import esgi.jwm.project.loyalty.serverhandler.ServerHandlerCompanyTest;
import esgi.jwm.project.loyalty.serverhandler.ServerHandlerPersonTest;


public class LoginFragment extends Fragment {

    private View fragment;
    private ServerHandler serverHandler;
    private Typeface typeface;
    private AutoCompleteTextView email;
    private EditText pwd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment=  inflater.inflate(R.layout.fragment_login, container, false);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");

        Button buttonLogin = fragment.findViewById(R.id.button_login);
        email = fragment.findViewById(R.id.email);
        pwd = fragment.findViewById(R.id.password);

        if(typeface != null){

            buttonLogin.setTypeface(typeface);
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickLogin(v);
                }
            });
        }

        return fragment;
    }

    public void onClickLogin(View v){

        Toast.makeText(getContext(), getContext().toString(), Toast.LENGTH_LONG).show();
        CoreActivity coreActivity = (CoreActivity) getActivity();
//        String email =

//        serverHandler is a TESTING object which simulate a serverHandlerPerson
//        check
        String mail = String.valueOf(email.getText());
        String password = String.valueOf(pwd.getText());

        if(coreActivity.customerOrCompany)
//            if we are in customer mode
            serverHandler = new ServerHandler(new ServerHandlerPersonTest());
        else
            serverHandler = new ServerHandler(new ServerHandlerCompanyTest());
        coreActivity.idPersonConnected = serverHandler.logIn(mail, password);

//        change the actual fragment
        coreActivity.setupFragment(new ConnectedPersonFragment());

    }
}
