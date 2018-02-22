package esgi.jwm.project.loyalty.fragments;

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

public class CompanyFragment extends Fragment implements IFragmentsOnClickListener {


    private View fragment;
    private Typeface typeface;
    private SharedPreferences cache;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_company, container, false);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");

        TextView titleTextView = (TextView) fragment.findViewById(R.id.title);
        Button buttonRegister = fragment.findViewById(R.id.button_register);
        Button buttonLogin = fragment.findViewById(R.id.button_login);

        getContext().setTheme(R.style.CompanyTheme);

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
    public void onStart() {
        super.onStart();
        cache = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        editor = cache.edit();
    }

    @Override
    public void onResume() {
        super.onResume();

//        cache.edit().putBoolean(getString(R.string.customer_or_company), false);
    }

    @Override
    public void onClickLogin(View v) {
        editor.putBoolean(getString(R.string.customer_or_company), false);
        if(editor.commit()){
            Log.i("CustomerFragment", "cache updated");
        } else {
            Log.i("CustomerFragment", "cache NOT updated");
        }

        Intent intent = new Intent(getActivity(), CoreActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClickRegister(View v) {
        editor.putBoolean(getString(R.string.customer_or_company), false);
        if(editor.commit()){
            Log.i("CustomerFragment", "cache updated");
        } else {
            Log.i("CustomerFragment", "cache NOT updated");
        }

    }


}
