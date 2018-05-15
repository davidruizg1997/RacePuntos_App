package com.example.davidruiz.preliminar;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Historical extends Fragment {

    Connection connection=new Connection();
    private CustomAdapterHistorical adapter;
    ListView listHistorical;
    public ArrayList<ListModelHistorical> CustomListView=new ArrayList<ListModelHistorical>();
    private boolean success=false;
    TextView textPoints;

    public Historical() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_historical, container, false);
        // Inflate the layout for this fragment
        listHistorical=(ListView) view.findViewById(R.id.lvHistoricalServices);

        textPoints=(TextView) view.findViewById(R.id.totalPoints);

        try {
            java.sql.Connection con=connection.conexionBD();
            String user="";
            SharedPreferences preferences=getContext().getSharedPreferences("Logeo", Context.MODE_PRIVATE);
            user=preferences.getString("user", "");

            CallableStatement procedure=con.prepareCall("{call consulta_puntos_acumulados(?)}");
            procedure.setString("@id_usuario_ingreso", user);
            procedure.execute();

            ResultSet rs=procedure.getResultSet();

            int totalPoints=0;
            while (rs.next()){
                totalPoints=rs.getInt("puntos_acumulados");
                textPoints.setText(totalPoints+" Puntos Disponibles");
            }
        }catch (Exception ex){

        }

        FilList filList=new FilList();
        filList.execute("");
        return view;
    }

    public class FilList extends AsyncTask<String, String, String>{

        String r="";
        List<Map<String, String>> prolist=new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            Toast toast=Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
            toast.show();
            if(success==false){

            }else{
                try{
                    adapter=new CustomAdapterHistorical(getActivity(), CustomListView);
                    listHistorical.setAdapter(adapter);
                }catch (Exception ex){
                    r=ex.getMessage();
                }
            }
        }

        @Override
        protected String doInBackground(String... string) {
            try {
                java.sql.Connection con=connection.conexionBD();
                if(con==null){
                    success=false;
                    r="Error en la conexión con la base de datos";
                }else{
                    Context context;
                    context=getContext();
                    String user;
                    SharedPreferences preferences=context.getSharedPreferences("Logeo", Context.MODE_PRIVATE);
                    user=preferences.getString("user", "");

                    CallableStatement procedureOne=con.prepareCall("{call obtener_id_puntos_actuales(?)}");
                    procedureOne.setString("@id_usuario_ingreso", user);
                    procedureOne.execute();

                    ResultSet rsOne=procedureOne.getResultSet();
                    int reference_points=0;

                    while(rsOne.next()){
                        reference_points=rsOne.getInt("id_puntos_actuales");

                            CallableStatement procedureTwo=con.prepareCall("{call obtener_id_servicio_puntos_compra(?)}");
                        procedureTwo.setInt("@id_puntos_actuales_ingreso", reference_points);
                        procedureTwo.execute();

                        ResultSet rsTwo=procedureTwo.getResultSet();

                        int id_service=0;
                        int buy_points=0;

                        while(rsTwo.next()){
                            id_service=rsTwo.getInt("id_puntos_servicio");
                            buy_points=rsTwo.getInt("puntos_compra");
                        }

                        CallableStatement procedureThree=con.prepareCall("{call obtener_nombre_servicio(?)}");
                        procedureThree.setInt("@id_servicio_ingreso", id_service);
                        procedureThree.execute();

                        ResultSet rsThree=procedureThree.getResultSet();

                        String service="";

                        while(rsThree.next()){
                            service=rsThree.getString("nombre_servicio");
                            Log.d("Resultado", service);
                            if(service.equals("Cambio de liquidos")){
                                CustomListView.add(new ListModelHistorical(service, buy_points));
                            }else if(service.equals("Montallantas")){
                                CustomListView.add(new ListModelHistorical(service, buy_points));
                            }else if(service.equals("Lavado")){
                                CustomListView.add(new ListModelHistorical(service, buy_points));
                            }else if (service.equals("Aditivo")){
                                CustomListView.add(new ListModelHistorical(service, buy_points));
                            }else if (service.equals("Lubricantes")){
                                CustomListView.add(new ListModelHistorical(service, buy_points));
                            }else if (service.equals("Carga de Combustible")){
                                CustomListView.add(new ListModelHistorical(service, buy_points));
                            }
                        }
                    }

                    r="Historial:Exitoso";
                    success=true;
                }
            }catch (Exception ex){
                r=ex.getMessage();
                success=false;
            }
            return r;
        }
    }
}