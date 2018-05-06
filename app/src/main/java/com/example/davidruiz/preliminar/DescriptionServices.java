package com.example.davidruiz.preliminar;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class DescriptionServices extends AppCompatActivity {

    public TextView textTitle, textPoints, textDescription;
    private ImageView imageService;
    private Button buttonReserve;

    String title, description, textReserve;
    int image, points;

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_description_services);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonReserve=(Button) findViewById(R.id.btnReserve);

        buttonReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textReserve=title;
                openDialog();
            }
        });

        imageService=(ImageView) findViewById(R.id.service_thumbnail);
        textTitle=(TextView) findViewById(R.id.tvTitle);
        textPoints=(TextView) findViewById(R.id.tvPoints);
        textDescription=(TextView) findViewById(R.id.tvDescription);

        Intent intent=getIntent();
        title=intent.getExtras().getString("Title");
        description=intent.getExtras().getString("Description");
        int image=intent.getExtras().getInt("ServiceThumbnail");
        getSupportActionBar().setTitle(title);


        if(title.equals("Cambio de Liquidos")){
            points=450;
        }else if(title.equals("Montallantas")){
            points=100;
        }else if(title.equals("Lavado")){
            points=500;
        }else if(title.equals("Aditivo")){
            points=350;
        }else if(title.equals("Lubricantes")){
            points=400;
        }

        imageService.setImageResource(image);
        textTitle.setText(title);
        textDescription.setText(description);
        textPoints.setText(points+" puntos");
    }

    public void openDialog(){
        ReserveDialog fpDialog=new ReserveDialog();
        fpDialog.show(getSupportFragmentManager(), "Reserva");

        Intent intent=new Intent(getApplicationContext(), ReserveDialog.class);
        intent.putExtra("Title", title);
    }
}