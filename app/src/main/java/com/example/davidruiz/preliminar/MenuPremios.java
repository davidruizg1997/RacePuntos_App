package com.example.davidruiz.preliminar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuPremios extends Fragment {


    List<Premios> listaPremios;

    public MenuPremios() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_menu_premios, container, false);

        listaPremios = new ArrayList<>();
        listaPremios.add(new Premios("Cambio de Liquidos", 450, "Reemplazo de Aceite basico para el motor.", R.drawable.liquidos));
        listaPremios.add(new Premios("Montallantas", 100, "Servicio de ", R.drawable.montallantas));
        listaPremios.add(new Premios("Lavado", 500, "Cambio", R.drawable.lavado));
        listaPremios.add(new Premios("Aditivo", 350, "Recarga de Gasolina", R.drawable.aditivo));
        listaPremios.add(new Premios("Lubricantes", 400, "Recarga de Gasolina", R.drawable.lubricantes));

        RecyclerView premiosRecyclerView=(RecyclerView) view.findViewById(R.id.recyclerview_id);
        RecyclerViewPremiosAdapter myAdapter= new RecyclerViewPremiosAdapter(view.getContext(), listaPremios);
        premiosRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        premiosRecyclerView.setAdapter(myAdapter);

        // Inflate the layout for this fragment
        return view;
    }
}
