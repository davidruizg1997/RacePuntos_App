package com.example.davidruiz.preliminar;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReserveDialog extends AppCompatDialogFragment {

    private TextView tvtitleService;
    String service="";
    DescriptionServices descriptionServices=new DescriptionServices();

    public ReserveDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.fragment_reserve_dialog, null);

        final DatePicker datePicker=(DatePicker) view.findViewById(R.id.datePickerReserve);
        datePicker.setMinDate(System.currentTimeMillis()-1000);

        Spinner spinner=(Spinner) view.findViewById(R.id.spinnerHour);

        String[] datos=new String[]{"7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datos);
        spinner.setAdapter(adapter);


        service=getActivity().getIntent().getExtras().getString("Title");
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
        tvtitleService=(TextView) view.findViewById(R.id.titleService);
        tvtitleService.setText(service);
        return builder.create();
    }
}
