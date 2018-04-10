package com.example.davidruiz.preliminar;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapEstacionesFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    private final static int MY_PERMISSION_FINE_LOCATION=100;

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
            //localizacion.setMapEstacionesFragment(this);
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,(LocationListener) localizacion);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,(LocationListener) localizacion);
        }else{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_FINE_LOCATION);
            }
            return;
        }
        //uiSettings.setMyLocationButtonEnabled(true);
        //mGoogleMap.setMyLocationEnabled(true);
        Criteria criteria=new Criteria();
        Location loc=locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        final LatLng estacion1=new LatLng(loc.getLatitude(),loc.getLongitude());
        latitud=loc.getLatitude();
        longitud=loc.getLongitude();
        System.out.println(latitud+", "+longitud);

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.addPolyline(new PolylineOptions().add(
                new LatLng(4.6193382,-74.0747432),
                new LatLng(latitud, longitud)
                ).width(10).color(Color.RED)

        );

        Mobil(googleMap);
        Terpel(googleMap);
        Petrobras(googleMap);

        /*if(mGoogleMap!=null){
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    View row=getLayoutInflater().inflate(R.layout.custom_marker, null);
                    ImageView imageStation=(ImageView) row.findViewById(R.id.imageMarker);
                    TextView titleMarker=(TextView) row.findViewById(R.id.titleStation);
                    Button go=(Button) row.findViewById(R.id.btnGo);

                    imageStation.setImageResource(R.drawable.propuesta2);
                    titleMarker.setText(marker.getTitle());
                    //go.setOnClickListener();
                    return row;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    return null;
                }
            });
        }*/
    }

    public void Mobil(GoogleMap googleMap){
        mGoogleMap=googleMap;
        final LatLng estacion1=new LatLng(4.6193382,-74.0747432);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion1).title("Esso Mobil - Cll 28").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.mobil)));
        final LatLng estacion2=new LatLng(4.623858, -74.075975);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion2).title("Esso Mobil - Cra. 34").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.mobil)));
        final LatLng estacion3=new LatLng(4.631120, -74.072411);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion3).title("Esso Mobil - La Soledad").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.mobil)));
        final LatLng estacion4=new LatLng(4.626229, -74.080204);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion4).title("Esso Mobil - CAD").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.mobil)));
    }

    public void Texaco(GoogleMap googleMap){
        mGoogleMap=googleMap;
        final LatLng estacion1=new LatLng(4.6161191,-74.0728485);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion1).title("Texaco Av. Caracas - Cll 26").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.texacoprueba)));
    }

    public void Terpel(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        final LatLng estacion1=new LatLng(4.6306523,-74.0787819);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion1).title("Terpel Avenida 28").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.terpel)));
        final LatLng estacion2=new LatLng(4.627091, -74.065766);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion1).title("Terpel Javeriana").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.terpel)));
    }

    public void Petrobras(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        final LatLng estacion1=new LatLng(4.633012, -74.070072);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion1).title("Petrobras - Cll 45").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.terpel)));
        final LatLng estacion2=new LatLng(4.627091, -74.065766);
        mGoogleMap.addMarker(new MarkerOptions().position(estacion1).title("Terpel Javeriana").snippet("El precio de la gasolina es :").icon(BitmapDescriptorFactory.fromResource(R.drawable.terpel)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listadoEstaciones=getResources().getStringArray(R.array.filtros_item);
        checkedEstaciones=new boolean[listadoEstaciones.length];
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
                            if(item=="Mobil"){
                                stationProvider=1;
                            }
                            if(item=="Terpel"){
                                stationProvider=2;
                            }
                            if(item=="Petrobras"){
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
            //latitud=loc.getLatitude();
            //longitud=loc.getLongitude();
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


}