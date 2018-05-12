package com.example.davidruiz.preliminar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Splash extends AppCompatActivity {

    Connection connectRacePuntos=new Connection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(!conexionVerificar(this)){
            AlertDialog.Builder notNet=new AlertDialog.Builder(this);
            notNet.setTitle("Error de conexión");
            notNet.setMessage("No tiene conexión a red. Por favor verifique su conexión y vuelva a intentarlo.");
            notNet.setCancelable(false);
            notNet.setPositiveButton("Reintente", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    retry();
                }
            });
            notNet.show();
        }else {
            Thread timer = new Thread() {
                public void run() {
                    Looper.prepare();
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                        try{
                            String user, pass;
                            SharedPreferences preferences=getSharedPreferences("Logeo", Context.MODE_PRIVATE);
                            user=preferences.getString("user", "");
                            pass=preferences.getString("pass", "");

                            if(user.trim().equals("")||pass.trim().equals("")){
                                Intent openLogin=new Intent(Splash.this, Login.class);
                                startActivity(openLogin);
                                finish();
                            }else{
                                String message;
                                Boolean access=false;
                                try{
                                    CallableStatement procedure=connectRacePuntos.conexionBD().prepareCall("{call logeo_persona(?,?)}");
                                    procedure.setString("@documento_ingresado", user);
                                    procedure.setString("@contrasena_ingresada", pass);
                                    // Ejecuta el procedimiento almacenado.
                                    procedure.execute();
                                    // Confirma que se ejecuto el procedimiento almacenado.
                                    final ResultSet rs=procedure.getResultSet();

                                    if (rs.next()==true){
                                        message="Acceso exitoso";
                                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                                        access=true;
                                        Intent activity_sliding=new Intent(getApplicationContext(),Principal.class);
                                        startActivity(activity_sliding);
                                    }else{
                                        message="Acceso denegado";
                                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                                        access=false;
                                    }
                                    connectRacePuntos.conexionBD().commit();
                                    connectRacePuntos.conexionBD().close();
                                }catch (SQLException e){
                                    access=false;
                                    message=e.getMessage();
                                }
                            }
                        }catch(Exception e){
                            e.getMessage();
                        }
                    }

                }
            };
            timer.start();
        }
    }

    private static ConnectivityManager manager;

    public static boolean connectedNetwork(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static boolean conexionVerificar(Context context){
        boolean conectado=false;
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] red=connectivityManager.getAllNetworkInfo();

        for(int i=0; i<red.length;i++){
            if(red[i].getState()==NetworkInfo.State.CONNECTED){
                conectado=true;
            }
        }
        return conectado;
    }

    public void retry(){
        Intent retryApp=new Intent(this, Splash.class);
        startActivity(retryApp);
        finish();
    }
}