package esgi.jwm.project.loyalty.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONObject;

import java.util.Calendar;

import esgi.jwm.project.loyalty.R;
import esgi.jwm.project.loyalty.serverhandler.APICallback;
import esgi.jwm.project.loyalty.serverhandler.ServerHandler;
import esgi.jwm.project.loyalty.serverhandler.ServerHandlerCompanyTest;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegisterFragment extends Fragment {
    private View fragment;

    private final int TYPE_COUNTRY = 1005;
    final String[] SEXE = {"Male","Female","Other"};

    private AutoCompleteTextView email;
    private AutoCompleteTextView firstName;
    private AutoCompleteTextView lastName;

//    pwd : password, pwd1 : retypepassword
    private TextInputEditText pwd;
    private TextInputEditText pwd1;

    private PlaceAutocompleteFragment adress;
    private MaterialBetterSpinner sexe;
    private TextInputEditText telephone;
    private ServerHandler serverHandler;
    private SharedPreferences cache;
    private Place place;

    private EditText birthDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragment =  inflater.inflate(R.layout.fragment_register, container, false);

        //        get the cache of the application
        this.cache = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());

        email = fragment.findViewById(R.id.email);
        pwd = fragment.findViewById(R.id.password);
        pwd1 = fragment.findViewById(R.id.password1);
        sexe = (MaterialBetterSpinner) fragment.findViewById(R.id.android_material_design_spinner);
        firstName = fragment.findViewById(R.id.firstname);
        lastName = fragment.findViewById(R.id.lastname);
        birthDate = fragment.findViewById(R.id.birthdate);
        telephone = fragment.findViewById(R.id.telephone);

        birthDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            int year  = calendar.get(Calendar.YEAR);
            int month  = calendar.get(Calendar.MONTH);
            int day  = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.semitransparentblack)));
            datePickerDialog.show();

        });


        dateSetListener = (view, year, month, dayOfMonth) -> {
            month += 1;
            birthDate.setText(dayOfMonth + "/" + month + "/"+year);
        };

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SEXE);
        sexe.setAdapter(arrayAdapter);

        adress = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_adress);


        Button buttonRegister = fragment.findViewById(R.id.button_register);


        buttonRegister.setOnClickListener(v -> {
            try {
                register(v);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        });

//        CITY

        adress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place p) {
                place = p;
                System.out.println(place.toString());
                System.out.println("place getadress(): " + place.getAddress());
                System.out.println("place getName(): " + place.getName());
                System.out.println("place getLocal(): " + place.getLocale());
            }

            @Override
            public void onError(Status status) {
//                Log.i(TAG, "An error occurred: " + status);
            }
        });
        adress.setFilter(new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).build());
        adress.setHint(getActivity().getString(R.string.adress_label));

//        autocompleteFragmentCity.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//                Log.i(TAG, "Place: " + place.getName());
//            }
//
//            @Override
//            public void onError(Status status) {
//                Log.i(TAG, "An error occurred: " + status);
//            }
//        });
//

        return fragment;
    }

    private void register(View v) throws UnirestException {
        if(checkInputs()){
            serverHandler = new ServerHandler(new ServerHandlerCompanyTest(getContext(), cache));

            String mail = email.getText().toString();
            String password = pwd.getText().toString();
            String tel = telephone.getText().toString();
            String firstname = firstName.getText().toString();
            String lastname = lastName.getText().toString();
            String sexe = "M";
            String birthdate = birthDate.getText().toString();
            String streetNumber;
            String route;
            String zipCode;
            String city;
            String country;


            serverHandler.register(mail, password, tel,
                    firstname, lastname, sexe, birthdate,
                    "45", "bd totopar", "77777",
                    "Paris", "France", new APICallback() {
                        @Override
                        public void onSuccessResponse(JSONObject result) {
                            
                        }


                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    })

        }
    }

    private boolean checkInputs() {

        if(TextUtils.isEmpty(email.getText())) {
            email.setError("Please enter your mail");
            email.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(firstName.getText())) {
            firstName.setError("Please enter your first name");
            firstName.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(lastName.getText())) {
            lastName.setError("Please enter your last name");
            lastName.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(birthDate.getText())) {
            birthDate.setError("Please enter your birthdate");
            birthDate.requestFocus();
            return false;
        }
        if(place == null){
            Toast.makeText(getActivity(), "Please enter an valid adress", Toast.LENGTH_LONG).show();
        }

        if(TextUtils.isEmpty(telephone.getText())) {
            pwd.setError("Please enter your telephone");
            pwd.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(pwd.getText())) {
            pwd.setError("Please enter your password");
            pwd.requestFocus();
            return false;
        }

        if(pwd.getText() != pwd1.getText()){
            pwd.setError("Both password don't match");
            return false;
        }

        return true;
    }


}
