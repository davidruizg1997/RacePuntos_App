package com.example.davidruiz.preliminar;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import java.util.Calendar;


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
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater= getActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.fragment_reserve_dialog, null);

        final DatePicker datePicker=(DatePicker) view.findViewById(R.id.datePickerReserve);
        datePicker.setMinDate(System.currentTimeMillis()-1000);

        spinnerOrganizations=(Spinner) view.findViewById(R.id.spinnerOrganizationEstations);
        spinnerEstations=(Spinner) view.findViewById(R.id.spinnerEstations);
        spinnerHourData=(Spinner) view.findViewById(R.id.spinnerHour);

        String[] hourData=new String[]{"7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"};
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
                                String obtenerSoloHora=horaReserva.substring(0,1);
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

                                    if(calendarDay==dia&&calendarMonth==mes&&calendarYear==anio){
                                        if(horaReservaInt>hora){
                                            String SQLSTATIONSERVICE= "SELECT COUNT( FROM ";
                                            Statement statementThree=null;
                                            statementThree=connection.conexionBD().createStatement();
                                            ResultSet resultSetThree=statementTwo.executeQuery(SQLSTATIONSERVICE);

                                            while(resultSetThree.next()){

                                            }
                                        }else{
                                            Toast.makeText(getContext(),"Hora no permitida", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        if(calendarDay>dia&&calendarMonth>=mes&&calendarYear==anio){
                                            String SQLSTATIONSERVICE= "SELECT puntos_acumulados FROM puntos WHERE id_usuario_puntos='"+user+"'";
                                            Statement statementThree=null;
                                            statementThree=connection.conexionBD().createStatement();
                                            ResultSet resultSetThree=statementThree.executeQuery(SQLSTATIONSERVICE);

                                            while(resultSetThree.next()){
                                                pointsUser=resultSetThree.getInt("puntos_acumulados");
                                            }
                                        }else{
                                            Toast.makeText(getContext(),"Fecha no permitida", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }catch (SQLException e){

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
        /*try{
            String horaActual="";
            Connection connection=new Connection();
            String query="SELECT {fn NOW()}";
            PreparedStatement consulta= connection.conexionBD().prepareStatement(query);
            consulta.setString(1, horaActual);
            ResultSet resultSet=consulta.executeQuery();

            while(resultSet.next()){
                horaActualRS=resultSet.getString("fn NOW()");
                Toast.makeText(getContext(),horaActualRS, Toast.LENGTH_SHORT).show();
            }
        }catch (SQLException e){

        }*/

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

    private  static void displayRow(String title, ResultSet rs){
        try {
            String ejemplo;
            while (rs.next()){
                ejemplo=rs.getString("");
            }
        }catch (SQLException e){

        }
    }
}
