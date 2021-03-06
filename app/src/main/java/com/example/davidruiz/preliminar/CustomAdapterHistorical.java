package com.example.davidruiz.preliminar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterHistorical extends BaseAdapter {

    private Activity fragment;
    private ArrayList<ListModelHistorical> mListHistorical;
    private static LayoutInflater mInflater=null;
    private ListModelHistorical temporalValues=null;
    private int i=0;

    public CustomAdapterHistorical(Activity f, ArrayList d){
        fragment=f;
        mListHistorical=d;
    }

    @Override
    public int getCount() {
        if(mListHistorical.size()<=0)
            return 1;
            return mListHistorical.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolderHistorical{
        public TextView textService;
        public TextView textPoints;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View vi=view;
        ViewHolderHistorical viewHolderHistorical;
        if(view==null){
            Context c=viewGroup.getContext();
            mInflater=LayoutInflater.from(c);
            vi=mInflater.inflate(R.layout.historical_layout, null);
            viewHolderHistorical=new ViewHolderHistorical();
            viewHolderHistorical.textService=vi.findViewById(R.id.tvDataBaseService);
            viewHolderHistorical.textPoints=vi.findViewById(R.id.tvDataBasePoints);
            vi.setTag(viewHolderHistorical);
        }else
            viewHolderHistorical=(ViewHolderHistorical) vi.getTag();
        try{
            if(mListHistorical.size()<=0){
                viewHolderHistorical.textService.setText("No hay información");
                viewHolderHistorical.textPoints.setText("No hay información");
            }else{
                temporalValues=null;
                temporalValues=(ListModelHistorical) mListHistorical.get(position);
                viewHolderHistorical.textService.setText(temporalValues.getNameService());

                if(temporalValues.getNameService().equals("Cambio de liquidos")){
                    viewHolderHistorical.textService.setTextColor(Color.parseColor("#DF0101"));
                    viewHolderHistorical.textPoints.setTextColor(Color.parseColor("#DF0101"));
                    viewHolderHistorical.textPoints.setText(String.valueOf(temporalValues.getPoints())+" Desc.");
                }
                if(temporalValues.getNameService().equals("Montallantas")){
                    viewHolderHistorical.textService.setTextColor(Color.parseColor("#DF0101"));
                    viewHolderHistorical.textPoints.setTextColor(Color.parseColor("#DF0101"));
                    viewHolderHistorical.textPoints.setText(String.valueOf(temporalValues.getPoints())+" Desc.");
                }
                if(temporalValues.getNameService().equals("Lavado")){
                    viewHolderHistorical.textService.setTextColor(Color.parseColor("#DF0101"));
                    viewHolderHistorical.textPoints.setTextColor(Color.parseColor("#DF0101"));
                    viewHolderHistorical.textPoints.setText(String.valueOf(temporalValues.getPoints())+" Desc.");
                }
                if(temporalValues.getNameService().equals("Aditivo")){
                    viewHolderHistorical.textService.setTextColor(Color.parseColor("#DF0101"));
                    viewHolderHistorical.textPoints.setTextColor(Color.parseColor("#DF0101"));
                    viewHolderHistorical.textPoints.setText(String.valueOf(temporalValues.getPoints())+" Desc.");
                }
                if(temporalValues.getNameService().equals("Lubricantes")){
                    viewHolderHistorical.textService.setTextColor(Color.parseColor("#DF0101"));
                    viewHolderHistorical.textPoints.setTextColor(Color.parseColor("#DF0101"));
                    viewHolderHistorical.textPoints.setText(String.valueOf(temporalValues.getPoints())+" Desc.");
                }
                if(temporalValues.getNameService().equals("Carga de Combustible")){
                    viewHolderHistorical.textService.setTextColor(Color.parseColor("#01DF01"));
                    viewHolderHistorical.textPoints.setTextColor(Color.parseColor("#01DF01"));
                    viewHolderHistorical.textPoints.setText(String.valueOf(temporalValues.getPoints())+" Adic.");
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return vi;
    }
}
