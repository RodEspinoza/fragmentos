package com.example.rodrigoespinoza.fragmentos;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rodrigoespinoza.fragmentos.fragments.AzulFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.RojoFragment;

public class FragmentoEstatico extends AppCompatActivity implements AzulFragment.OnFragmentInteractionListener, RojoFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmento_estatico);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
