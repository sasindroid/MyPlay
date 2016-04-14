package com.sasi.giffgaffplay;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class NavigationTabActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabFragment.OnFragmentInteractionListener, OneFragment.OnFragmentInteractionListener,
        SecondFragment.OnFragmentInteractionListener, ThirdFragment.OnFragmentInteractionListener,
        FourthFragment.OnFragmentInteractionListener, FifthFragment.OnFragmentInteractionListener, TabFragment2.OnFragmentInteractionListener,
        TabFragment3.OnFragmentInteractionListener, TabFragment4.OnFragmentInteractionListener, TabFragment5.OnFragmentInteractionListener {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransacton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Snackbar.make(drawer, "See Navigation view for more variants on tabs.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransacton = mFragmentManager.beginTransaction();
        mFragmentTransacton.replace(R.id.containerView, new TabFragment()).commit();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation_tab, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            FragmentTransaction fragmentTransacton1 = mFragmentManager.beginTransaction();
            fragmentTransacton1.replace(R.id.containerView, new TabFragment()).commit();
        } else if (id == R.id.nav_gallery) {
            FragmentTransaction fragmentTransacton2 = mFragmentManager.beginTransaction();
            fragmentTransacton2.replace(R.id.containerView, new TabFragment2()).commit();

        } else if (id == R.id.nav_slideshow) {
            FragmentTransaction fragmentTransacton3 = mFragmentManager.beginTransaction();
            fragmentTransacton3.replace(R.id.containerView, new TabFragment3()).commit();
        } else if (id == R.id.nav_gallery2) {
            FragmentTransaction fragmentTransacton4 = mFragmentManager.beginTransaction();
            fragmentTransacton4.replace(R.id.containerView, new TabFragment4()).commit();
        } else if (id == R.id.nav_manage) {
            FragmentTransaction fragmentTransacton5 = mFragmentManager.beginTransaction();
            fragmentTransacton5.replace(R.id.containerView, new TabFragment5()).commit();
        } else if (id == R.id.nav_share) {
            FragmentTransaction fragmentTransacton6 = mFragmentManager.beginTransaction();
            fragmentTransacton6.replace(R.id.containerView, new OneFragment()).commit();
        } else if (id == R.id.nav_send) {
            FragmentTransaction fragmentTransacton7 = mFragmentManager.beginTransaction();
            fragmentTransacton7.replace(R.id.containerView, new SecondFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
