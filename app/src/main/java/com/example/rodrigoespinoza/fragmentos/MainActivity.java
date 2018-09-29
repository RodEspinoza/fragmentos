package com.example.rodrigoespinoza.fragmentos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rodrigoespinoza.fragmentos.fragments.LoginFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.RegistroFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RegistroFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener{
    Button btnDinamico, btnEstatico, btnNavegacion;
    Intent intent;
    RegistroFragment registroFragment;
    LoginFragment loginFragment;
    Button btnOpenLoginFragment, btnOpenSigInFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btnOpenLoginFragment = findViewById(R.id.btnOpenLoginFragment);
        this.btnOpenSigInFragment = findViewById(R.id.btnOpenSingInFragment);

        this.btnOpenLoginFragment.setOnClickListener(this);
        this.btnOpenSigInFragment.setOnClickListener(this);
        this.registroFragment = new RegistroFragment();
        this.loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.containerFragment, this.loginFragment).commit();

    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.btnOpenLoginFragment:
                fragmentTransaction.replace(R.id.containerFragment, this.loginFragment).commit();
                break;
            case R.id.btnOpenSingInFragment:
                fragmentTransaction.replace(R.id.containerFragment, this.registroFragment).commit();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        
    }
}
