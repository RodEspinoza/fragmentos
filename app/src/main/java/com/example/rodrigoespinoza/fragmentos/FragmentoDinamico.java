package com.example.rodrigoespinoza.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rodrigoespinoza.fragmentos.fragments.AzulFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.RojoFragment;

public class FragmentoDinamico extends AppCompatActivity implements View.OnClickListener, AzulFragment.OnFragmentInteractionListener, RojoFragment.OnFragmentInteractionListener{
    Button btnVerFragAzul, btnVerFragRojo;
    AzulFragment fragmentAzul;
    RojoFragment fragmentRojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmento_dinamico);
        fragmentAzul = new AzulFragment();
        fragmentRojo = new RojoFragment();
        this.btnVerFragAzul = findViewById(R.id.btnVerFragmentoAzul);
        this.btnVerFragRojo = findViewById(R.id.btnVerFragmentoRojo);
        this.btnVerFragRojo.setOnClickListener(this);
        this.btnVerFragAzul.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().add(
                R.id.contenedorFragment, fragmentAzul).commit();
        SharedPreferences preferences =  getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String usuario = preferences.getString("usuario", "no data");
        String password = preferences.getString("password", "clave null");
        Toast.makeText(this, ""+ usuario +" " + password, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.btnVerFragmentoRojo:
                fragmentTransaction.replace(R.id.contenedorFragment, fragmentRojo).commit();
                break;

            case R.id.btnVerFragmentoAzul:
                fragmentTransaction.replace(R.id.contenedorFragment, fragmentAzul).commit();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
