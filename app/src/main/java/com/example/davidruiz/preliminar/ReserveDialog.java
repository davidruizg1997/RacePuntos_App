package com.example.davidruiz.preliminar;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReserveDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener{

    private TextView tvtitleService, tvPointsService;
    String service="";
    String station="";
    String horaReserva="";
    String user="";
    int horaReservaInt=0;
    public int pointsReserve;
    int calendarDay, calendarMonth, calendarYear;
    public Spinner spinnerOrganizations, spinnerEstations, spinnerHourData;
    DescriptionServices descriptionServices=new DescriptionServices();

    public ReserveDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context=getActivity();
        SharedPreferences preferences= context.getSharedPreferences("Logeo", Context.MODE_PRIVATE);
        user=preferences.getString("user", "");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater= getActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.fragment_reserve_dialog, null);

        final DatePicker datePicker=(DatePicker) view.findViewById(R.id.datePickerReserve);
        datePicker.setMinDate(System.currentTimeMillis()-1000);

        spinnerOrganizations=(Spinner) view.findViewById(R.id.spinnerOrganizationEstations);
        spinnerEstations=(Spinner) view.findViewById(R.id.spinnerEstations);
        spinnerHourData=(Spinner) view.findViewById(R.id.spinnerHour);

        String[] hourData=new String[]{"07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"};
        String[] organizations=new String[]{"Mobil", "Terpel", "Petrobras"};


        ArrayAdapter<String> adapterOrganizations= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, organizations);
        spinnerOrganizations.setAdapter(adapterOrganizations);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, hourData);
        spinnerHourData.setAdapter(adapter);

        service=getActivity().getIntent().getExtras().getString("Title");

        spinnerOrganizations.setOnItemSelectedListener(this);

        if(service.equals("Cambio de Liquidos")){
            pointsReserve=450;
        }else if(service.equals("Montallantas")){
            pointsReserve=100;
        }else if(service.equals("Lavado")){
            pointsReserve=500;
        }else if(service.equals("Aditivo")){
            pointsReserve=350;
        }else if(service.equals("Lubricantes")){
            pointsReserve=400;
        }

        tvtitleService=(TextView) view.findViewById(R.id.titleService);
        tvPointsService=(TextView) view.findViewById(R.id.pointsServiceReserve);
        tvtitleService.setText(service);
        tvPointsService.setText(pointsReserve+" puntos");

        builder.setView(view).setTitle("Reserva")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder notNet=new AlertDialog.Builder(getContext());
                        notNet.setTitle("¿Seguro de realizar la reserva?");
                        notNet.setMessage("Al confirmar la reserva se descuentan los puntos para adquirir el servicio.");
                        notNet.setCancelable(false);
                        notNet.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final Calendar calendar= Calendar.getInstance();
                                DatePicker datePicker1;
                                datePicker1=(DatePicker) view.findViewById(R.id.datePickerReserve);
                                calendarDay=datePicker1.getDayOfMonth();
                                calendarMonth=datePicker1.getMonth()+1;
                                calendarYear=datePicker1.getYear();
                                horaReserva=spinnerHourData.getSelectedItem().toString();
                                String obtenerSoloHora=horaReserva.substring(0,2);
                                horaReservaInt=Integer.parseInt(obtenerSoloHora);
                                station=spinnerEstations.getSelectedItem().toString();

                                try {
                                    String SQL= "SELECT DATEPART(day, GETDATE()) AS DIA, DATEPART(month, GETDATE()) AS MES, DATEPART(year, GETDATE()) AS ANIO";
                                    Connection connection=new Connection();
                                    Statement statement=null;
                                    statement=connection.conexionBD().createStatement();
                                    ResultSet resultSet=statement.executeQuery(SQL);

                                    int dia=0;
                                    int mes=0;
                                    int anio=0;

                                    while (resultSet.next()){
                                        dia=resultSet.getInt("DIA");
                                        mes=resultSet.getInt("MES");
                                        anio=resultSet.getInt("ANIO");
                                    }

                                    String SQLHOUR= "SELECT DATEPART(HOUR, GETDATE())-5 AS HORA";
                                    Statement statementTwo=null;
                                    statementTwo=connection.conexionBD().createStatement();
                                    ResultSet resultSetTwo=statementTwo.executeQuery(SQLHOUR);

                                    int hora=0;

                                    while (resultSetTwo.next()){
                                        hora=resultSetTwo.getInt("HORA");
                                    }

                                    int pointsUser=0;

                                    if(hora<5){
                                        hora=-(-hora)+24;
                                        dia=dia-1;
                                        if(calendarDay==dia&&calendarMonth==mes&&calendarYear==anio){
                                            if(horaReservaInt>hora){
                                                validationReserves();
                                            }else{
                                                /*dialogInterface.dismiss();
                                                AlertDialog.Builder notTime=new AlertDialog.Builder(getContext());
                                                notTime.setTitle("Hora No Establecida");
                                                notTime.setMessage("La hora establecida para la reserva es menor a la actual. Por favor defina una hora posterior a la actual.");
                                                notTime.setCancelable(false);
                                                notTime.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dismiss();
                                                    }
                                                });*/

                                                String message="La hora establecida para la reserva es menor a la actual. Por favor defina una hora posterior a la actual.";

                                                alertUserTop(getActivity(),message);
                                            }
                                        }else{
                                            if(calendarDay>dia&&calendarMonth>=mes&&calendarYear==anio){
                                                validationReserves();
                                            }else{
                                                Toast.makeText(getContext(),"Fecha no permitida", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }else{
                                        if(calendarDay==dia&&calendarMonth==mes&&calendarYear==anio){
                                            if(horaReservaInt>hora){
                                                validationReserves();
                                            }else{
                                                /*Context context=getActivity();
                                                AlertDialog.Builder notTime=new AlertDialog.Builder(context);
                                                notTime.setTitle("Hora No Establecida");
                                                notTime.setMessage("La hora establecida para la reserva es menor a la actual. Por favor defina una hora posterior a la actual.");
                                                notTime.setCancelable(false);
                                                notTime.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dismiss();
                                                    }
                                                });*/



                                                String message="La hora establecida para la reserva es menor a la actual. Por favor defina una hora posterior a la actual.";
                                                //showToast(message);
                                                AppCompatDialogFragment compatDialogFragment=new AppCompatDialogFragment();
                                                alertUserTop(getContext(),message);

                                                //Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
                                                //dialogInterface.dismiss();
                                            }
                                        }else{
                                            if(calendarDay>dia&&calendarMonth>=mes&&calendarYear==anio){
                                                validationReserves();
                                            }else{
                                                Toast.makeText(getContext(),"Fecha no permitida", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }


                                }catch (SQLException e){
                                    Log.e("Resu:", e.getMessage());
                                }

                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dismiss();
                            }
                        });
                        notNet.show();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item=adapterView.getItemAtPosition(i).toString();
        String[] estationsMobil=new String[]{"Cll 28", "Cll 34", "La Soledad", "CAD"};
        String[] estationsTerpel=new String[]{"Avenida 28", "Javeriana"};
        String[] estationsPetrobras=new String[]{"Cll 45"};
        String selectedOrganization=spinnerOrganizations.getSelectedItem().toString();
        if(selectedOrganization.equals("Mobil")){
            ArrayAdapter<String> adapterEstationMobil= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, estationsMobil);
            spinnerEstations.setAdapter(adapterEstationMobil);
        }else if (selectedOrganization.equals("Terpel")){
            ArrayAdapter<String> adapterEstationTerpel= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, estationsTerpel);
            spinnerEstations.setAdapter(adapterEstationTerpel);
        }else if (selectedOrganization.equals("Petrobras")){
            ArrayAdapter<String> adapterEstationPetrobras= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, estationsPetrobras);
            spinnerEstations.setAdapter(adapterEstationPetrobras);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void validationReserves(){
        try{
            int idMaxPoints=0;
            Connection connection=new Connection();

            CallableStatement procedure=connection.conexionBD().prepareCall("{call obtener_id_puntos_actual_max(?)}");
            procedure.setString("@id_usuario", user);

            procedure.executeQuery();

            final ResultSet resultSetOne=procedure.getResultSet();

            while(resultSetOne.next()){
                idMaxPoints=resultSetOne.getInt("id_puntos_max");
            }

            connection.conexionBD().close();

            CallableStatement procedureTwo=connection.conexionBD().prepareCall("{call obtener_puntos_acumulados_actual(?)}");
            procedureTwo.setInt("@id_puntos_maximo", idMaxPoints);

            procedureTwo.executeQuery();

            final ResultSet resultSetTwo=procedureTwo.getResultSet();

            int pointsUser=0;

            while(resultSetTwo.next()){
                pointsUser=resultSetTwo.getInt("puntos_acumulados");
            }

            connection.conexionBD().close();

            if(pointsUser>=pointsReserve){

                CallableStatement procedureThree=connection.conexionBD().prepareCall("{call obtener_id_estacion_servicio(?)}");
                procedureThree.setString("@nombre_estacion", station);

                procedureThree.executeQuery();

                final ResultSet resultSetThree=procedureThree.getResultSet();

                int idStation=0;

                while(resultSetThree.next()){
                    idStation=resultSetThree.getInt("id_estacion");
                }

                connection.conexionBD().close();

                CallableStatement procedureFour=connection.conexionBD().prepareCall("{call consulta_disponibilidad_estacion(?,?,?)}");
                procedureFour.setInt("@id_estacion_recibida", idStation);
                procedureFour.setString("@fecha_recibida", calendarYear+"/"+calendarMonth+"/"+calendarDay);
                procedureFour.setInt("@hora_recibida", horaReservaInt);

                procedureFour.executeQuery();

                final ResultSet resultSetFour=procedureFour.getResultSet();

                int contadorReservas=0;

                if(resultSetFour.next()){
                    while(resultSetFour.next()){
                        contadorReservas+=1;
                    }
                }else {
                    contadorReservas=0;
                }

                connection.conexionBD().close();

                if(contadorReservas<=3){
                    int descount;
                    descount=pointsUser-pointsReserve;

                    CallableStatement procedureFive=connection.conexionBD().prepareCall("ingreso_puntos ?,?,?");
                    procedureFive.setString(1, user);
                    procedureFive.setString(2, "ANDROID");
                    procedureFive.setInt(3, descount);

                    procedureFive.executeUpdate();

                    connection.conexionBD().close();

                    CallableStatement procedureSix=connection.conexionBD().prepareCall("{call obtener_id_puntos_actual_max_para_ingreso_detalle_puntos(?)}");
                    procedureSix.setString("@id_usuario_recibido", user);

                    procedureSix.executeQuery();

                    final ResultSet resultSetSix=procedureSix.getResultSet();

                    int id_points=0;

                    while(resultSetSix.next()){
                        id_points=resultSetSix.getInt("id_puntos");
                    }

                    connection.conexionBD().close();

                    CallableStatement procedureSeven=connection.conexionBD().prepareCall("{call obtener_id_puntos_servicio(?)}");
                    procedureSeven.setString("@nombre_servicio_recibido", service);

                    procedureSeven.executeQuery();

                    final ResultSet resultSetSeven=procedureSeven.getResultSet();

                    int id_service=0;

                    while(resultSetSeven.next()){
                        id_service=resultSetSeven.getInt("id_servicio");
                    }

                    connection.conexionBD().close();

                    CallableStatement procedureEigth=connection.conexionBD().prepareCall("ingreso_detalle_puntos_reserva ?,?,?,?");
                    procedureEigth.setInt(1, idStation);
                    procedureEigth.setInt(2, id_service);
                    procedureEigth.setInt(3, id_points);
                    procedureEigth.setInt(4, pointsReserve);

                    procedureEigth.executeUpdate();

                    connection.conexionBD().close();

                    CallableStatement procedureNine=connection.conexionBD().prepareCall("ingreso_reserva ?,?,?,?,?");
                    procedureNine.setInt(1, idStation);
                    procedureNine.setString(2, user);
                    procedureNine.setString(3, String.valueOf(calendarYear)+"/"+String.valueOf(calendarMonth)+"/"+String.valueOf(calendarDay));
                    procedureNine.setInt(4, pointsReserve);
                    procedureNine.setInt(5, horaReservaInt);

                    procedureNine.executeUpdate();

                    connection.conexionBD().close();

                    CallableStatement procedureTen=connection.conexionBD().prepareCall("{call obtener_id_reserva_ultima(?)}");
                    procedureTen.setString("@id_usuario_recibido", user);

                    procedureTen.execute();

                    final ResultSet resultSetTen=procedureTen.getResultSet();
                    int id_reserve=0;

                    while(resultSetTen.next()){
                        id_reserve=resultSetTen.getInt("id_reserva_ultima");
                    }

                    connection.conexionBD().close();

                    CallableStatement procedureEleven=connection.conexionBD().prepareCall("ingreso_detalle_reserva ?,?,?");
                    procedureEleven.setInt(1, id_reserve);
                    procedureEleven.setInt(2, id_service);
                    procedureEleven.setInt(3, pointsReserve);

                    procedureEleven.executeUpdate();

                    connection.conexionBD().close();
                }else{

                }
            }else{
                Toast.makeText(getContext(),"Puntos Insuficientes", Toast.LENGTH_SHORT).show();
            }
        }catch (SQLException ex){
            Log.e("Resultado", ex.toString());
        }
    }

    private  static void displayRow(String title, ResultSet rs){
        try {
            String ejemplo;
            while (rs.next()){
                ejemplo=rs.getString("");
            }
        }catch (SQLException e){

        }
    }

    public static void alertUserTop(Context context, String msg) {
        Toast t = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        t.show();
    }
}