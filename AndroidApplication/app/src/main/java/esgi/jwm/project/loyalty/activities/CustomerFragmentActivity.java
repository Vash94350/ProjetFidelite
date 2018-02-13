package esgi.jwm.project.loyalty.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import esgi.jwm.project.loyalty.R;


/**
 * Created by wmorgado on 13/02/2018.
 */

public class CustomerFragmentActivity extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_customer, container, false);
        return fragment;
    }
}
