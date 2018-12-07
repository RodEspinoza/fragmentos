package com.example.rodrigoespinoza.fragmentos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;



//import com.example.rodrigoespinoza.fragmentos.fragments.AddOrderFragment;


import com.example.rodrigoespinoza.fragmentos.GoogleMaps.MapsActivity;
import com.example.rodrigoespinoza.fragmentos.fragments.AddNewOrderFragment;

import com.example.rodrigoespinoza.fragmentos.fragments.AddProductFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.EditPersonFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.EditProductFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.MapsFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.ProductFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.ProductOrders;
import com.example.rodrigoespinoza.fragmentos.model.Person;

import com.facebook.login.LoginManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EditProductFragment.OnFragmentInteractionListener,
        AddProductFragment.OnFragmentInteractionListener,
        ProductFragment.OnFragmentInteractionListener,
        ProductOrders.OnFragmentInteractionListener,
        EditPersonFragment.OnFragmentInteractionListener,
        AddNewOrderFragment.OnFragmentInteractionListener
        {

    Person person;

    Fragment ourFragment = null;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    StringRequest stringRequest;
    String TAG_MENU ="MENUActivity !!!";
    FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions gso;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        Bundle bundleMenu = intent.getExtras();

        if (!bundleMenu.isEmpty()) {
            if(bundleMenu.get("person_id").toString()!=null){
            person = new Person(bundleMenu.get("person_id").toString());
            }
        }

        if(mAuth.getCurrentUser() != null){
            Log.d(TAG_MENU, "done");

            Toast.makeText(this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT);

        }else{
            Log.d(TAG_MENU, "credenciales ???");
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_products) {

            ourFragment = new ProductFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragmentMenu, ourFragment).commit();

        } else if (id == R.id.nav_order) {

            ourFragment = new ProductOrders();
            Bundle bundle = new Bundle();
            bundle.putString("person_id", person.getId());
            ourFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragmentMenu, ourFragment).commit();

        } else if (id == R.id.nav_edit_person) {

            getCampos(person);


        }else if (id == R.id.maps) {
            
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.logOut) {

        }else if (id == R.id.logOut){


            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

        private void logout() {

            this.mAuth.signOut();
            googleSignInClient = GoogleSignIn.getClient(this, gso);
            googleSignInClient.signOut();

            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        private void getCampos(final Person per) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Cargando... ");
        this.progressDialog.show();

        DocumentReference docRef = db.collection("persona").document(per.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){

                    ourFragment = new EditPersonFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("nombre", document.get("name").toString());
                    bundle.putString("last_name", document.get("last_name").toString());
                    bundle.putString("sexo", document.get("sex").toString());
                    bundle.putString("id", document.getId());
                    progressDialog.hide();
                    ourFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFragmentMenu, ourFragment).commit();

                }
            }
            }
        });
       
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
