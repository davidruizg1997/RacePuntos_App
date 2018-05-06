package com.example.davidruiz.preliminar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPrincipal extends Fragment {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;

    public FragmentPrincipal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_principal, container, false);
        // Inflate the layout for this fragment
        listView=(ExpandableListView) view.findViewById(R.id.expandableLV);
        initData();
        listAdapter=new ExpandableListAdapterOrganizationProject(getContext(), listDataHeader, listHash);
        listView.setAdapter(listAdapter);
        return view;
    }

    private void initData() {
        listDataHeader=new ArrayList<>();
        listHash=new HashMap<>();

        listDataHeader.add("Quiénes Somos");
        listDataHeader.add("Misión");
        listDataHeader.add("Visión");

        List<String> quienesSomos = new ArrayList<>();
        quienesSomos.add("Somos la solución para las Estaciones De Servicio que buscan un programa para la fidelización de sus clientes, mediante la acumulación de puntos por compra de combustible. Contamos con un equipo enfocado plenamente a las soluciones basadas siempre en la calidad y excelencia para la gestión de los procesos.");

        List<String> mision = new ArrayList<>();
        mision.add("Proporcionar la tecnología de manera innovadora a medida de las necesidades de las Estaciones De Servicios, con el objetivo de incrementar su competitividad, productividad y fidelización.");

        List<String> vision = new ArrayList<>();
        vision.add("Queremos estar comprometidos con los problemas de nuestros clientes de forma transparente y eficaz .En nuestra visión queremos ser una empresa de referencia, que camina con el cambio de la tecnología y la sociedad.");

        listHash.put(listDataHeader.get(0), quienesSomos);
        listHash.put(listDataHeader.get(1), mision);
        listHash.put(listDataHeader.get(2), vision);
    }

}
