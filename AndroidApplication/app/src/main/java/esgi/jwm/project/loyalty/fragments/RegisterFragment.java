package esgi.jwm.project.loyalty.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import esgi.jwm.project.loyalty.R;
import esgi.jwm.project.loyalty.activities.CoreActivity;
import esgi.jwm.project.loyalty.serverhandler.APICallback;
import esgi.jwm.project.loyalty.serverhandler.ServerHandler;
import esgi.jwm.project.loyalty.serverhandler.ServerHandlerPersonTest;
import esgi.jwm.project.loyalty.viewtools.DialogBoxBuilder;
import esgi.jwm.project.loyalty.viewtools.Keyboard;
import fr.ganfra.materialspinner.MaterialSpinner;


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
    private TextInputEditText telephone;
    private ServerHandler serverHandler;

    private CoreActivity coreActivity;
    private ProgressBar progressBar;
    private SharedPreferences cache;
    private MaterialSpinner sexeList;
    private String sex = "";


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

        progressBar = fragment.findViewById(R.id.progressBar);

        firstName = fragment.findViewById(R.id.firstname);
        lastName = fragment.findViewById(R.id.lastname);
        birthDate = fragment.findViewById(R.id.birthdate);
        telephone = fragment.findViewById(R.id.telephone);

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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item, SEXE);
        sexeList = fragment.findViewById(R.id.sexe);
        sexeList.setAdapter(arrayAdapter);
        sexeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex = parent.getItemAtPosition(position).toString().substring(0,1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adress = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_adress);
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

        Button buttonRegister = fragment.findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(v -> {
            onClickRegister(v);
        });

        return fragment;
    }

    private void onClickRegister(View v) {
        Keyboard.hide(getActivity(), v);

        progressBar.setVisibility(View.VISIBLE);
        String res = checkInputs();
        if(TextUtils.isEmpty(res)){
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

            serverHandler.register(mail, password, tel,
                    firstname, lastname, sex, birthdate,
                    streetNumber, route, zipCode,
                    city, country, ServerHandler.webSiteURL, new APICallback() {
                        @Override
                        public void onSuccessResponse(JSONObject result) {
                            AlertDialog alertDialog = DialogBoxBuilder.build(getActivity(), getString(R.string.dialog_title_succes),
                                    getString(R.string.dialog_message_succes_account_created), getString(R.string.ok_button), "",
                                    getLayoutInflater(), DialogBoxBuilder.DIALOG_WITH_NO_INPUT_TWO_BUTTONS, new IBasicDialogCallBack() {
                                        @Override
                                        public void onClickButton1(View view, AlertDialog dialogBox) {
                                            dialogBox.dismiss();
                                        }

                                        @Override
                                        public void onClickButton2(View view, String res, AlertDialog dialogBox) {
                                            dialogBox.dismiss();
                                        }
                                    });
                            if(alertDialog != null)
                                alertDialog.show();
                            else
                                Log.d("AlertDialog Register", "alertDialog :> NULL");

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
                            Toast.makeText(getContext(), error.networkResponse.statusCode + " " + error.getMessage(),Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    });
        } else {

            AlertDialog alertDialog = DialogBoxBuilder.build(getActivity(), getString(R.string.dialog_title_error),
                    res, getString(R.string.ok_button), "", getLayoutInflater(), DialogBoxBuilder.DIALOG_WITH_NO_INPUT_TWO_BUTTONS, new IBasicDialogCallBack() {
                        @Override
                        public void onClickButton1(View view, AlertDialog dialogBox) {
                            dialogBox.dismiss();
                        }

                        @Override
                        public void onClickButton2(View view, String res, AlertDialog dialogBox) {
                            dialogBox.dismiss();
                        }
                    });

            if(alertDialog != null)
                alertDialog.show();
            else
                Log.d("AlertDialog Register", "alertDialog :> NULL");
        }
        progressBar.setVisibility(View.GONE);
    }

    private String checkInputs() {

        if(TextUtils.isEmpty(email.getText())) {
            email.requestFocus();
            return "Please enter your mail";
        }

        if(TextUtils.isEmpty(firstName.getText())) {
            firstName.requestFocus();
            return "Please enter your first name";
        }

        if(TextUtils.isEmpty(lastName.getText())) {
            lastName.requestFocus();
            return "Please enter your last name";
        }

        if(TextUtils.isEmpty(birthDate.getText())) {
            return "Please enter your birthdate";
        }
        if(place == null){
            return "Please enter an valid adress";
        } else {
            if(!checkAdressInput()){
                return "Street number is missing";
            }
        }

        if(TextUtils.isEmpty(sex)){
            sexeList.requestFocus();
            return "Please select a sexe";
        }

        if(TextUtils.isEmpty(telephone.getText())) {
            telephone.requestFocus();
            return "Please enter your telephone";
        }

        if(TextUtils.isEmpty(pwd.getText())) {
            pwd.requestFocus();
            return "Please enter your password";
        }

        if(pwd.getText().toString().equals(pwd1.getText().toString())){
            return "";
        } else {
            pwd.requestFocus();
            return "Both password don't match";
        }
    }
    private boolean checkAdressInput() {
        String adress = place.getAddress().toString();
        if (adress.matches(".*\\d+.*"))
            return true;
        else
            return false;
    }


}
