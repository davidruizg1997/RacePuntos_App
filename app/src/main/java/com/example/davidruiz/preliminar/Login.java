package com.example.davidruiz.preliminar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity {

    Connection connectRacePuntos=new Connection();
    Button btnEnter;
    EditText userDocument, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        send_registration();
        forgotPassword();

        btnEnter=(Button) findViewById(R.id.button);
        userDocument=(EditText) findViewById(R.id.user_document);
        loginPassword=(EditText) findViewById(R.id.password);

        btnEnter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final android.app.AlertDialog dialog=new SpotsDialog(Login.this, R.style.AlertDialogLoad);
                dialog.show();
                final Handler handler=new Handler();
                final Runnable runnable= new Runnable() {
                    @Override
                    public void run() {
                        String message;
                        Boolean access=false;
                        String user=userDocument.getText().toString();
                        String password=loginPassword.getText().toString();
                        if(user.equals("")){
                            userDocument.setError("Ingresar documento");
                            userDocument.requestFocus();
                        }
                        if(password.equals("")){
                            loginPassword.setError("Ingresar contraseña");
                            loginPassword.requestFocus();
                        }
                        if(dialog.isShowing()){
                            try{
                                if(user.trim().equals("")||password.trim().equals("")){
                                    message="Por favor ingrese los datos";
                                    Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                                }else{
                                    enterApp();
                                }
                            }catch (Exception e){
                                message=e.getMessage();
                            }
                            dialog.dismiss();
                        }
                    }
                };
                handler.postDelayed(runnable, 3000);
            }
        });
    }

    public void send_registration(){
        TextView tv=(TextView) findViewById(R.id.textView);
        tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent activity_registrer=new Intent(getApplicationContext(), Registration.class);
                startActivity(activity_registrer);
            }
        });
    }

    private void savePreferences(String user, String password){
        SharedPreferences preferences=getSharedPreferences("Logeo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("user", user);
        editor.putString("pass", password);
        editor.commit();
    }

    public void enterApp(){
        String message;
        Boolean access=false;
        try{
            String document=userDocument.getText().toString();
            String password=loginPassword.getText().toString();
            CallableStatement procedure=connectRacePuntos.conexionBD().prepareCall("{call logeo_persona(?,?)}");
            procedure.setString("@documento_ingresado", document);
            procedure.setString("@contrasena_ingresada", password);
            // Ejecuta el procedimiento almacenado.
            procedure.execute();
            // Confirma que se ejecuto el procedimiento almacenado.
            final ResultSet rs=procedure.getResultSet();
            if (rs.next()==true){
                message="Acceso exitoso";
                Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                access=true;
                savePreferences(document, password);
                Intent activity_sliding=new Intent(getApplicationContext(),Principal.class);
                startActivity(activity_sliding);
                finish();
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

    public void forgotPassword(){
        TextView fPassword=(TextView) findViewById(R.id.textView2);
        fPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openDialog();
            }
        });
    }

    public void openDialog(){
        ForgotPasswordDialog fpDialog=new ForgotPasswordDialog();
        fpDialog.show(getSupportFragmentManager(), "Olvido Contraseña");
    }
}