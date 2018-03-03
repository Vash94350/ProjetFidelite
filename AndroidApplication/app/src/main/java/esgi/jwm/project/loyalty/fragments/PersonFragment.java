package esgi.jwm.project.loyalty.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import esgi.jwm.project.loyalty.R;
import esgi.jwm.project.loyalty.activities.IFragmentsOnClickListener;
import esgi.jwm.project.loyalty.activities.CoreActivity;


/**
 * Created by wmorgado on 13/02/2018.
 */

public class PersonFragment extends Fragment implements IFragmentsOnClickListener {

    private View fragment;
    private Typeface typeface;
    private SharedPreferences cache;
    private SharedPreferences.Editor editor;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        cache = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        editor = cache.edit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_person, container, false);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");

        TextView titleTextView = (TextView) fragment.findViewById(R.id.title);
        Button buttonRegister = fragment.findViewById(R.id.button_register);
        Button buttonLogin = fragment.findViewById(R.id.button_login);

        if(typeface != null){
            titleTextView.setTypeface(typeface);
            buttonRegister.setTypeface(typeface);
            buttonLogin.setTypeface(typeface);
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin(v);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegister(v);
            }
        });

        return fragment;
    }

    @Override
    public void onClickLogin(View v) {
        editor.putBoolean(getString(R.string.person_or_company), true);
        editor.putBoolean(getString(R.string.login_or_register), true);
        if(editor.commit()){
            Intent intent = new Intent(getActivity(), CoreActivity.class);
            startActivity(intent);
        } else {
            Log.i("PersonFragment", "cache NOT updated");
        }


    }


    @Override
    public void onClickRegister(View v) {
        editor.putBoolean(getString(R.string.person_or_company), true);
        editor.putBoolean(getString(R.string.login_or_register), false);
        if(editor.commit()){
            Intent intent = new Intent(getActivity(), CoreActivity.class);
            startActivity(intent);
        } else {
            Log.i("PersonFragment", "cache NOT updated");
        }


    }


}
