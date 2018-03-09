package esgi.jwm.project.loyalty.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import esgi.jwm.project.loyalty.R;
import esgi.jwm.project.loyalty.activities.CoreActivity;
import esgi.jwm.project.loyalty.serverhandler.APICallback;
import esgi.jwm.project.loyalty.serverhandler.ServerHandler;
import esgi.jwm.project.loyalty.serverhandler.ServerHandlerCompanyTest;
import esgi.jwm.project.loyalty.serverhandler.ServerHandlerPersonTest;
import esgi.jwm.project.loyalty.viewtools.Keyboard;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegisterFragment extends Fragment {
    private View fragment;

    private final int TYPE_COUNTRY = 1005;
    final String[] SEXE = {"Male","Female","Other"};

    private TextInputEditText email;
    private TextInputEditText firstName;
    private TextInputEditText lastName;

//    pwd : password, pwd1 : retypepassword
    private TextInputEditText pwd;
    private TextInputEditText pwd1;

    private PlaceAutocompleteFragment adress;
    private MaterialBetterSpinner sexe;
    private TextInputEditText telephone;
    private ServerHandler serverHandler;

    private CoreActivity coreActivity;
    private ProgressBar progressBar;
    private SharedPreferences cache;
    private String sex;


    private Place place;
    private EditText birthDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private SharedPreferences.Editor editor;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragment =  inflater.inflate(R.layout.fragment_register, container, false);

        coreActivity = (CoreActivity) getActivity();

        //        get the cache of the application
        this.cache = coreActivity.cache;
        editor = this.cache.edit();

        email = fragment.findViewById(R.id.email);
        pwd = fragment.findViewById(R.id.password);
        pwd1 = fragment.findViewById(R.id.password1);
        sexe = fragment.findViewById(R.id.android_material_design_spinner);
        progressBar = fragment.findViewById(R.id.progressBar);

        firstName = fragment.findViewById(R.id.firstname);
        lastName = fragment.findViewById(R.id.lastname);
        birthDate = fragment.findViewById(R.id.birthdate);
        telephone = fragment.findViewById(R.id.telephone);

        sexe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex = parent.getItemAtPosition(position).toString().substring(0,1);
                System.out.println("SEXE :" + sex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        birthDate.setOnClickListener(v -> {
//
//            Calendar calendar = Calendar.getInstance();
//            int year  = calendar.get(Calendar.YEAR);
//            int month  = calendar.get(Calendar.MONTH);
//            int day  = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
//                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                    dateSetListener, year, month, day);
////            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.semitransparentblack)));
//            datePickerDialog.show();
//
//        });

        birthDate.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                Calendar calendar = Calendar.getInstance();
                int year  = calendar.get(Calendar.YEAR);
                int month  = calendar.get(Calendar.MONTH);
                int day  = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.semitransparentblack)));
                datePickerDialog.show();
            }
        });


        dateSetListener = (view, year, month, dayOfMonth) -> {
            month += 1;
            birthDate.setText(year + "-" + month + "-"+dayOfMonth);
        };

//        setup Sexe spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SEXE);
        sexe.setAdapter(arrayAdapter);
        sexe.setSelection(arrayAdapter.getPosition("Male"));

        adress = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_adress);


        Button buttonRegister = fragment.findViewById(R.id.button_register);


        buttonRegister.setOnClickListener(v -> {
            onClickRegister(v);
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

        return fragment;
    }

    private void onClickRegister(View v) {
        Keyboard.hide(getActivity(), v);

        progressBar.setVisibility(View.VISIBLE);
        if(checkInputs()){
            serverHandler = new ServerHandler(new ServerHandlerPersonTest(getContext()));

            String mail = email.getText().toString();
            String password = pwd.getText().toString();
            String tel = telephone.getText().toString();
            String firstname = firstName.getText().toString();
            String lastname = lastName.getText().toString();

            String birthdate = birthDate.getText().toString();

            String placeLocale = place.getAddress().toString();
            //10 Ruelle des Chats, 77860 Quincy-Voisins, France\n" +

            String streetNumber = placeLocale.substring(0, placeLocale.indexOf(' '));
            placeLocale = placeLocale.substring(placeLocale.lastIndexOf(streetNumber) + streetNumber.length() + 1, placeLocale.length());

            //Ruelle des Chats, 77860 Quincy-Voisins, France\n" +
            String route = placeLocale.substring(0, placeLocale.indexOf(','));
            placeLocale = placeLocale.substring(placeLocale.indexOf(route) + route.length() + 2, placeLocale.length());

            //77860 Quincy-Voisins, France\n" +
            String zipCode = placeLocale.substring(0,placeLocale.indexOf(' '));
            placeLocale = placeLocale.substring(placeLocale.indexOf(zipCode) + zipCode.length() + 1, placeLocale.length());

            //Quincy-Voisins, France\n" +

            String city = placeLocale.substring(0, placeLocale.indexOf(','));
            placeLocale = placeLocale.substring(placeLocale.indexOf(city) + city.length() + 2, placeLocale.length());

            //France" +
            String country = placeLocale;

            System.out.println("streetNumber " + streetNumber + " route "+ route + " zipCode " + zipCode + " city " + city + " Country " + country);

            serverHandler.register(mail, password, tel,
                    firstname, lastname, sex, birthdate,
                    streetNumber, route, zipCode,
                    city, country, new APICallback() {
                        @Override
                        public void onSuccessResponse(JSONObject result) {
                            System.out.println(result.toString());
                            Toast.makeText(getContext(), result.toString(),Toast.LENGTH_LONG).show();

                            try {
                                editor.putInt(getContext().getString(R.string.personId_data_label), result.getInt(getContext().getString(R.string.personId_data_label)));
                                editor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressBar.setVisibility(View.GONE);
                            coreActivity.setupFragment(new LoginFragment());

                        }


                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.getMessage());
                            Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    });
        }
        progressBar.setVisibility(View.GONE);
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

        if(pwd.getText().toString().equals(pwd1.getText().toString())){
            return true;
        }

        pwd.setError("Both password don't match");
        return false;
    }


}
