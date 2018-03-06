package esgi.jwm.project.loyalty.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import esgi.jwm.project.loyalty.R;
import esgi.jwm.project.loyalty.fragments.LoginFragment;
import esgi.jwm.project.loyalty.fragments.RegisterFragment;

public class CoreActivity extends FragmentActivity {
    public SharedPreferences cache;
    private SharedPreferences.Editor editor;
//    public boolean personOrCompany;
    private boolean loginOrRegister;
    private CoordinatorLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        root = findViewById(R.id.root);

        cache = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = cache.edit();


        loginOrRegister = cache.getBoolean(getString(R.string.login_or_register), true);
        if(cache.getBoolean(getString(R.string.person_or_company), false)){
            Toast.makeText(this, "customer", Toast.LENGTH_LONG).show();
            setTheme(R.style.CustomerTheme);
            root.setBackgroundColor(getResources().getColor(R.color.customerColor));
        } else {
            Toast.makeText(this, "company", Toast.LENGTH_LONG).show();
            setTheme(R.style.CompanyTheme);
            root.setBackgroundColor(getResources().getColor(R.color.companyColor));

        }

        if(loginOrRegister)
            setupFragment(new LoginFragment());
        else
            setupFragment(new RegisterFragment());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.d("onDestroy", "here");
        editor.clear().apply();

    }

    public int getidPersonLogged(){
        return cache.getInt(getString(R.string.personId_data_label), 0);
    }

    public boolean getModeLogged(){
        return cache.getBoolean(getString(R.string.person_or_company), true);
    }

    public String getToken(){
        return cache.getString(getString(R.string.token), "");
    }
    public String getQRToken() {
        return cache.getString(getString(R.string.QRtoken), "");
    }
    public void setupFragment(Fragment fragment){


//        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }


}
