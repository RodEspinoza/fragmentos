package com.example.rodrigoespinoza.fragmentos;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rodrigoespinoza.fragmentos.fragments.AddProductFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.EditPersonFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.EditProductFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.ProductFragment;
import com.example.rodrigoespinoza.fragmentos.fragments.ProductOrders;


public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EditProductFragment.OnFragmentInteractionListener,
        AddProductFragment.OnFragmentInteractionListener,
        ProductFragment.OnFragmentInteractionListener,
        ProductOrders.OnFragmentInteractionListener,
        EditPersonFragment.OnFragmentInteractionListener
        {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
        Fragment ourFragment = null;
        Bundle bundleMenu = getIntent().getExtras();
        Bundle bundle = new Bundle();
        Integer idUser = Integer.parseInt(bundleMenu.get("id").toString());

        int id = item.getItemId();

        if (id == R.id.nav_products) {
            ourFragment = new ProductFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragmentMenu, ourFragment).commit();
        } else if (id == R.id.nav_order) {
            ourFragment = new ProductOrders();
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragmentMenu, ourFragment).commit();

        } else if (id == R.id.nav_edit_person) {
            ourFragment = new EditPersonFragment();
            bundle.putInt("idUser", idUser);
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragmentMenu, ourFragment).commit();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
