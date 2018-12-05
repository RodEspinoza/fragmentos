package com.example.rodrigoespinoza.fragmentos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.android.volley.toolbox.Volley;


//import com.example.rodrigoespinoza.fragmentos.fragments.AddOrderFragment;


import com.example.rodrigoespinoza.fragmentos.fragments.AddNewOrderFragment;

import com.example.rodrigoespinoza.fragmentos.fragments.AddProductFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.EditPersonFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.EditProductFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.ProductFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.ProductOrders;
import com.example.rodrigoespinoza.fragmentos.model.Person;

import com.facebook.login.LoginManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        Bundle bundleMenu = intent.getExtras();

        if (!bundleMenu.isEmpty()) {
            if(bundleMenu.get("id")!=null){
            person = new Person(Float.parseFloat(bundleMenu.get("id").toString()));
            }
        }

        if(mAuth.getCurrentUser() != null){
            Log.d(TAG_MENU, "done");
        }else{
            Log.d(TAG_MENU, "credenciales ???");
        }
        Toast.makeText(this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT);

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
        this.requestQueue = Volley.newRequestQueue(this);
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
            bundle.putString("person_id", person.getId_user().toString());
            ourFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragmentMenu, ourFragment).commit();

        } else if (id == R.id.nav_edit_person) {

            getCampos(person);

        } else if (id == R.id.logOut){
            FirebaseAuth mAuth=  FirebaseAuth.getInstance();
            mAuth.signOut();
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

        String url = "https://androidsandbox.site/wsAndroid/wsGetPersona.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.optJSONArray("persona");
                    JSONObject json = jsonArray.getJSONObject(0);
                    String name = json.optString("nombre");
                    String last_name = json.optString("last_name");
                    String sex = json.optString("sexo");

                    ourFragment = new EditPersonFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("nombre", name);
                    bundle.putString("last_name", last_name);
                    bundle.putString("sexo", sex);
                    bundle.putString("id", person.getId_user().toString());
                    progressDialog.hide();
                    ourFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFragmentMenu, ourFragment).commit();

                } catch (Exception ex) {

                }
                progressDialog.hide();

            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", per.getId_user().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
