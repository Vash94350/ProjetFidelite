package esgi.jwm.project.loyalty.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import esgi.jwm.project.loyalty.R;
import esgi.jwm.project.loyalty.activities.CoreActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectedPersonFragment extends Fragment {


    private View fragment;
    private CoreActivity coreActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        coreActivity = (CoreActivity) getActivity();
        fragment =  inflater.inflate(R.layout.fragment_connected_person, container, false);

        TextView textView = fragment.findViewById(R.id.textview);
        String text =   "Hello person id : " + coreActivity.getidPersonLogged() + "\n"
                        + "token : " + coreActivity.getToken() + "\n"
                        + "QRToken : " + coreActivity.getQRToken();
        textView.setText(text);

        return fragment;
    }

}
