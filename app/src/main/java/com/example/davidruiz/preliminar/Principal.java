package com.example.davidruiz.preliminar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_principal, new FragmentPrincipal()).commit();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm= getSupportFragmentManager();

        if (id == R.id.nav_camera) {
            fm.beginTransaction().replace(R.id.content_principal, new FragmentPrincipal()).commit();
            getSupportActionBar().setTitle("Principal");
        } else if (id == R.id.nav_gallery) {
            fm.beginTransaction().replace(R.id.content_principal, new Historical()).commit();
            getSupportActionBar().setTitle("Historial");
        } else if (id == R.id.nav_slideshow) {
            fm.beginTransaction().replace(R.id.content_principal, new MapEstacionesFragment()).commit();
            getSupportActionBar().setTitle("Estaciones de Servicio");
        } else if (id == R.id.servicios){
            fm.beginTransaction().replace(R.id.content_principal, new MenuPremios()).commit();
            getSupportActionBar().setTitle("Servicios");
        } else if (id == R.id.ayuda){
            fm.beginTransaction().replace(R.id.content_principal, new PDFManual()).commit();
            getSupportActionBar().setTitle("Ayuda");
        } else if (id == R.id.salir){
            final AlertDialog.Builder alertExit=new AlertDialog.Builder(this);
            alertExit.setTitle("Salir de la Aplicación");
            alertExit.setMessage("¿Desea salir de la aplicación? - Si sale de la aplicación se cierra la sesión.");
            alertExit.setCancelable(false);
            alertExit.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences remove= getApplicationContext().getSharedPreferences("Logeo", Context.MODE_PRIVATE);
                    remove.edit().clear().commit();
                    Intent activity_sliding=new Intent(getApplicationContext(),Splash.class);
                    startActivity(activity_sliding);
                    finish();
                }
            });
            alertExit.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertExit.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
