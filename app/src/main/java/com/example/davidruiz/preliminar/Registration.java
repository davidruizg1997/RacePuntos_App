package com.example.davidruiz.preliminar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class Registration extends AppCompatActivity implements View.OnClickListener{

    com.example.davidruiz.preliminar.Connection conectar=new com.example.davidruiz.preliminar.Connection();
    EditText typesDocuments, documents, name, lastName, date, address, phoneNumber, emailAddress, password, passwordConfirm;
    CheckBox cbTerms;
    Button registrer;
    private int day, month, year;
    AlertDialog alertDialog;
    String[] documentTypes={"T.I.", "C.C.", "C.E."};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        termsCondictions();

        typesDocuments=(EditText) findViewById(R.id.edtTypes);
        typesDocuments.setCursorVisible(false);
        typesDocuments.getEditableText();
        typesDocuments.setInputType(InputType.TYPE_NULL);
        //documents.setEnabled(false);

        documents=(EditText) findViewById(R.id.edtDocument);
        name=(EditText) findViewById(R.id.edtName);
        lastName=(EditText)findViewById(R.id.edtLastName);

        date=(EditText) findViewById(R.id.edtDate);
        date.setCursorVisible(false);
        date.getEditableText();
        date.setInputType(InputType.TYPE_NULL);
        date.setTextIsSelectable(true);
        //date.setEnabled(false);

        address=(EditText) findViewById(R.id.edtAddress);
        phoneNumber=(EditText) findViewById(R.id.edtPhone);
        emailAddress=(EditText) findViewById(R.id.edtEmail);
        password=(EditText) findViewById(R.id.edtPassword);
        passwordConfirm=(EditText) findViewById(R.id.edtConfirmPassword);

        cbTerms=(CheckBox) findViewById(R.id.checkBox);
        cbTerms.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        registrer=(Button) findViewById(R.id.btnRegistrer);

        typesDocuments.setOnClickListener(this);
        date.setOnClickListener(this);
        registrer.setOnClickListener(this);

    }

    public void blockField(){
        date.setCursorVisible(false);
        date.getEditableText();
        date.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edtTypes:
                CreateAlertDialogWithRadioButtonGroup();
                break;
            case R.id.edtDate:
                birthDate();
                break;
            case R.id.btnRegistrer:
                if(cbTerms.isChecked()==true){
                    final android.app.AlertDialog dialog=new SpotsDialog(Registration.this, R.style.AlertDialogLoad);
                    dialog.show();
                    final Handler handler=new Handler();
                    final Runnable runnable= new Runnable() {
                        @Override
                        public void run() {
                            if(dialog.isShowing()){
                                if(addUser()==true){
                                    Intent activity_sliding = new Intent(getApplicationContext(), Principal.class);
                                    startActivity(activity_sliding);
                                    finish();
                                }else{
                                    addUser();
                                }
                                dialog.dismiss();
                            }
                        }
                    };
                    handler.postDelayed(runnable, 3000);
                }else{
                    Toast.makeText(getApplicationContext(),"No ha aceptado Terminos y Condiciones", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public boolean addUser(){
        boolean verify=false;
        String passwordEntered=password.getText().toString();
        String confirmPassword=passwordConfirm.getText().toString();
        try{
            String documentType = typesDocuments.getText().toString();
            String documentEntered = documents.getText().toString();
            String roleEntered = "USUARIO";
            String officeEntered = "2";
            String userCreation = "ANDROID";
            String namesEntered = name.getText().toString();
            String surnamesEntered = lastName.getText().toString();
            String birthDate = date.getText().toString();
            String addressEntered = address.getText().toString();
            String cellNumber = phoneNumber.getText().toString();
            String email = emailAddress.getText().toString();

            if(documentType.trim().equals("")){
                typesDocuments.setError("Seleccione un tipo de documento.");
                typesDocuments.requestFocus();
                typesDocuments.setText("");
            }
            if(documentEntered.trim().equals("")){
                documents.setError("Ingrese su identificación.");
                documents.requestFocus();
                documents.setText("");
            }
            if(namesEntered.trim().equals("")){
                name.setError("Ingrese su nombre.");
                name.requestFocus();
                name.setText("");
            }
            if(surnamesEntered.trim().equals("")){
                lastName.setError("Ingrese su apellido.");
                lastName.requestFocus();
                lastName.setText("");
            }
            if(birthDate.trim().equals("")){
                date.setError("Ingrese su fecha de nacimiento.");
                date.requestFocus();
                date.setText("");
            }
            if(addressEntered.trim().equals("")){
                address.setError("Ingrese se dirección.");
                address.requestFocus();
                address.setText("");
            }
            if(cellNumber.trim().equals("")){
                phoneNumber.setError("Ingrese su número de celular.");
                phoneNumber.requestFocus();
                phoneNumber.setText("");
            }
            if(email.trim().equals("")){
                emailAddress.setError("Ingrese su correo electronico.");
                emailAddress.requestFocus();
                emailAddress.setText("");
            }
            if(passwordEntered.trim().equals("")){
                password.setError("Ingrese su contraseña.");
                password.requestFocus();
                password.setText("");
            }else if(confirmPassword.trim().equals("")){
                passwordConfirm.setError("Ingrese su confirmación de contraseña.");
                passwordConfirm.requestFocus();
                passwordConfirm.setText("");
            }else if(passwordEntered.equals(confirmPassword)){
                CallableStatement procedure=conectar.conexionBD().prepareCall("registro_persona ?,?,?,?,?,?,?,?,?,?,?,?");
                procedure.setString(1, documentType);
                procedure.setString(2, documentEntered);
                procedure.setString(3, passwordEntered);
                procedure.setString(4, roleEntered);
                procedure.setString(5, officeEntered);
                procedure.setString(6, userCreation);
                procedure.setString(7, namesEntered);
                procedure.setString(8, surnamesEntered);
                procedure.setString(9, birthDate);
                procedure.setString(10, addressEntered);
                procedure.setString(11, cellNumber);
                procedure.setString(12, email);

                procedure.executeUpdate();
                procedure.close();
                conectar.conexionBD().close();
                savePreferences(documentEntered, passwordEntered);
                verify=true;
            }else{
                Toast.makeText(getApplicationContext(),"Contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                passwordConfirm.setError("Contraseña no coincide.");
                passwordConfirm.requestFocus();
                passwordConfirm.setText("");
            }
            return verify;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private void savePreferences(String user, String pass){
        SharedPreferences preferences=getSharedPreferences("Logeo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("user", user);
        editor.putString("pass", pass);
        editor.commit();
    }

    public void CreateAlertDialogWithRadioButtonGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
        builder.setTitle("Seleccione tipo de Documento:");
        builder.setSingleChoiceItems(documentTypes, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        typesDocuments.setText("T.I.");
                        break;
                    case 1:
                        typesDocuments.setText("C.C.");
                        break;
                    case 2:
                        typesDocuments.setText("C.E.");
                        break;
                }
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void birthDate(){
        final Calendar calendar= Calendar.getInstance();
        day=calendar.get(Calendar.DAY_OF_MONTH);
        month=calendar.get(Calendar.MONTH);
        year=calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearCurrent, int monthCurrent, int dayCurrent) {
                date.setText(dayCurrent+"/"+(monthCurrent+1)+"/"+yearCurrent);
            }
        }
        , day, month, year);
        datePickerDialog.show();
    }

    public void termsCondictions(){
        CheckBox terms=(CheckBox) findViewById(R.id.checkBox);

        terms.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(cbTerms.isChecked()==true){
                    openDialog();
                }
            }
        });
    }

    public void openDialog(){
        TermsAndCondictionsDialog fpDialog=new TermsAndCondictionsDialog();
        fpDialog.show(getSupportFragmentManager(), "Terminos y Condiciones");
    }
}