package com.example.davidruiz.preliminar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by DAVIDRUIZ on 25/03/2018.
 */

public class TermsAndCondictionsDialog extends AppCompatDialogFragment {
    private TextView title;
    private TextView text;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_dialog_termsandcondictions, null);

        builder.setView(view).setTitle("Terminos y Condiciones")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        text=(TextView) view.findViewById(R.id.textTerms);
        text.setText("Al descargar o utilizar la aplicación Race Puntos, aceptará automáticamente estos términos. Asegúrese de leerlos atentamente antes de utilizar la aplicación. Le ofrecemos esta aplicación para su uso personal sin ningún coste, no deberá intentar extraer código fuente de la aplicación, traducir la aplicación a otros idiomas ni crear versiones derivadas. La aplicación y todas las marcas comerciales, los derechos de autor, los derechos sobre bases de datos y demás derechos de propiedad intelectual relacionados continuaran siendo propiedad de Smart Tech.");
        return builder.create();
    }
}
