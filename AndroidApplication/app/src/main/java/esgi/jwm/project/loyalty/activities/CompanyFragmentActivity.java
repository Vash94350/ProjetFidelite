package esgi.jwm.project.loyalty.activities;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import esgi.jwm.project.loyalty.R;

/**
 * Created by wmorgado on 13/02/2018.
 */

public class CompanyFragmentActivity extends Fragment{


    View fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.fragment_company, container, false);
        return fragment;
    }

    public void showConnexionPage(View v){
        Snackbar.make(fragment, "hi", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
