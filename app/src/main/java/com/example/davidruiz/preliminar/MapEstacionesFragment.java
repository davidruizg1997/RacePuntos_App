package com.example.davidruiz.preliminar;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapEstacionesFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    private final static int MY_PERMISSION_FINE_LOCATION=100;
    ArrayList<LatLng> listPoints;

    String[] listadoEstaciones;
    boolean[] checkedEstaciones;
    ArrayList<Integer> mUserItems= new ArrayList<>();
    Button ejemplo;

    private Context mContext;

    String[] stations={"Mobil", "Terpel", "Petrobras"};
    boolean[] checkedItems;
    int stationProvider=0;
    ArrayList<Integer> mStationItems=new ArrayList<>();
    android.support.v7.app.AlertDialog alertDialog;
    double latitud, longitud;

    public MapEstacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView=(MapView) view.findViewById(R.id.map);

        if(mMapView!=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map_estaciones, container, false);
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        Localizacion localizacion=new Localizacion();
        LocationManager locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        UiSettings uiSettings=mGoogleMap.getUiSettings();

        if(ActivityCompat.checkSelfPermission(MapEstacionesFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            mGoogleMap.setMyLocationEnabled(true);
        }else{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_FINE_LOCATION);
            }
            return;
        }

        LatLng latLng=new LatLng(4.628355,-74.072861);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        uiSettings.setMyLocationButtonEnabled(true);
        mGoogleMap.setMyLocationEnabled(true);

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(listPoints.size()==2){
                    listPoints.clear();
                }
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.position(latLng);

                if(listPoints.size()==1){

                }else{

                }

                if(listPoints.size()==2){
                    String url = getRequestUrl(new LatLng(latitud, longitud),listPoints.get(0));
                    TaskRequestDirections taskRequestDirections=new TaskRequestDirections();
                    taskRequestDirections.execute(url);
                }
            }
        });
        Mobil(googleMap);
        Terpel(googleMap);
        Petrobras(googleMap);
    }

    private String getRequestUrl(LatLng origin, LatLng dest) {
        String str_org="origin="+origin.latitude+","+origin.longitude;
        String str_dest="destination="+dest.latitude+","+dest.longitude;
        String sensor="sensor=false";
        String mode="mode=driving";
        String param=str_org+"&"+str_dest+"&"+sensor+"&"+mode;
        String output="json";
        String url="https://maps.googleapis.com/maps/apis/directions/"+output+"?"+param;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString="";
        InputStream inputStream=null;
        HttpsURLConnection httpsURLConnection=null;
        try{
            URL url=new URL(reqUrl);
            httpsURLConnection=(HttpsURLConnection) url.openConnection();
            httpsURLConnection.connect();

            inputStream=httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer=new StringBuffer();
            String line="";
            while ((line=bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }

            responseString=stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream!=null){
                inputStream.close();
            }
            httpsURLConnection.disconnect();
        }
        return responseString;
    }

    public void Mobil(GoogleMap googleMap){
        mGoogleMap=googleMap;
        final LatLng estacion1=new LatLng(4.619588,-74.074542);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion1).title("Esso Mobil - Cll 28").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.mobil)));
        final LatLng estacion2=new LatLng(4.623995, -74.076052);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion2).title("Esso Mobil - Cll. 34").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.mobil)));
        final LatLng estacion3=new LatLng(4.630981, -74.072502);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion3).title("Esso Mobil - La Soledad").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.mobil)));
        final LatLng estacion4=new LatLng(4.626217, -74.080040);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion4).title("Esso Mobil - CAD").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.mobil)));
    }

    public void Texaco(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        final LatLng estacion1=new LatLng(4.6161191,-74.0728485);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion1).title("Texaco Av. Caracas - Cll 26").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.texacoprueba)));
    }

    public void Terpel(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        final LatLng estacion1=new LatLng(4.630661,-74.078772);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion1).title("Terpel Avenida 28").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.terpel)));
        final LatLng estacion2=new LatLng(4.627100, -74.065772);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion2).title("Terpel Javeriana").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.terpel)));
    }

    public void Petrobras(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        final LatLng estacion1=new LatLng(4.633121, -74.070061);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion1).title("Petrobras - Cll 45").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.petrobras)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listadoEstaciones=getResources().getStringArray(R.array.filtros_item);
        checkedEstaciones=new boolean[listadoEstaciones.length];
        listPoints=new ArrayList<>();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter, menu);  // Use filter.xml from step 1
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        checkedItems=new boolean[stations.length];
        if(id == R.id.action_filter){
            //Do whatever you want to do
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
            builder.setTitle("Seleccione una empresa prestadora de servicio:");
            builder.setMultiChoiceItems(stations, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    if(b){
                        mStationItems.add(i);
                    }else{
                        mStationItems.remove((Integer.valueOf(i)));
                    }
                }
            });

            builder.setCancelable(false);
            builder.setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String item="";
                    for(int u=0;i<mStationItems.size();i++){
                        item=item+stations[mStationItems.get(u)];
                        if(u!=mStationItems.size()-1){
                            if(item.equals("Mobil")){
                                stationProvider=1;
                                Log.d("Visual Mapa", "Selecciono Movil");
                            }
                            if(item.equals("Terpel")){
                                stationProvider=2;
                            }
                            if(item.equals("Petrobras")){
                                stationProvider=3;
                            }else{
                                stationProvider=0;
                            }
                        }
                    }
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }
        Log.e("Mensaje Error", "Si funciona");
        return true;
    }

    public class Localizacion implements LocationListener{
        MapEstacionesFragment maps;

        public MapEstacionesFragment getMaps() {
            return maps;
        }

        public void setMapEstacionesFragment(MapEstacionesFragment mapEstacionesFragment){
            this.maps=mapEstacionesFragment;
        }

        @Override
        public void onLocationChanged(Location loc){
            LatLng latLng=new LatLng(loc.getLatitude(),loc.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(getActivity(),"GPS Activado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(getActivity(),"GPS Desactivado", Toast.LENGTH_SHORT).show();
        }
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String responseString="";
            try{
                responseString=requestDirection(strings[0]);
            }catch (IOException e){
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TaskParser taskParser=new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String,String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject=null;
            List<List<HashMap<String, String>>> routes=null;
            try {
                jsonObject=new JSONObject(strings[0]);
                DirectionsEstations directionsEstations=new DirectionsEstations();
                routes=directionsEstations.parse(jsonObject);
            }catch (JSONException e){
                e.printStackTrace();;
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            ArrayList points=null;
            PolylineOptions polylineOptions=null;
            for (List<HashMap<String, String>> path:lists){
                points=new ArrayList();
                polylineOptions=new PolylineOptions();
                for(HashMap<String, String> point:path){
                    double lat= Double.parseDouble(point.get("lat"));
                    double lon= Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat, lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if(polylineOptions!=null){
                mGoogleMap.addPolyline(polylineOptions);
            }else{
                Toast.makeText(getContext(), "Dirección no encontrada", Toast.LENGTH_LONG).show();
            }
        }
    }

}