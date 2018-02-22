package esgi.jwm.project.loyalty.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import esgi.jwm.project.loyalty.R;
import esgi.jwm.project.loyalty.data.Person;
import esgi.jwm.project.loyalty.fragments.LoginFragment;
import esgi.jwm.project.loyalty.serverhandler.ServerHandler;
import esgi.jwm.project.loyalty.serverhandler.ServerHandlerPersonTest;

public class CoreActivity extends FragmentActivity {
    private SharedPreferences cache;
    public boolean customerOrCompany;
    public long idPersonConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_or_login);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        cache = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        customerOrCompany = cache.getBoolean(getString(R.string.customer_or_company), false);

//        if(customerOrCompany){
//            Toast.makeText(this, "customer", Toast.LENGTH_LONG).show();
//            setTheme(R.style.CustomerTheme);
//        } else {
//            Toast.makeText(this, "company", Toast.LENGTH_LONG).show();
//            setTheme(R.style.CompanyTheme);
//        }

        setupFragment(new LoginFragment());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(customerOrCompany){
            Toast.makeText(this, "customer", Toast.LENGTH_LONG).show();
            setTheme(R.style.CustomerTheme);
        } else {
            Toast.makeText(this, "company", Toast.LENGTH_LONG).show();
            setTheme(R.style.CompanyTheme);
        }
    }

    public void setupFragment(Fragment fragment){


//        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
