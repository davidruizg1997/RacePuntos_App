package com.example.davidruiz.preliminar;

import android.os.StrictMode;
import android.widget.Toast;

import java.sql.DriverManager;

/**
 * Created by DavidRuiz on 23/10/2017.
 */

public class Connection {

    public java.sql.Connection conexionBD(){
        java.sql.Connection conexion=null;

        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://smarttech.database.windows.net:1433;databaseName=RacePuntos;user=plataforma;password=6QphC1SQ");
        }catch(Exception e){
            e.printStackTrace();
        }
        return conexion;
    }
}