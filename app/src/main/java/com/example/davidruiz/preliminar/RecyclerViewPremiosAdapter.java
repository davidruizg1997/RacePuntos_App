package com.example.davidruiz.preliminar;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DavidRuiz on 23/02/2018.
 */

public class RecyclerViewPremiosAdapter extends RecyclerView.Adapter<RecyclerViewPremiosAdapter.MyViewHolder>{

    private Context mContext;
    private List<Premios> mPremios;

    public FragmentManager fragmentManager;

    public RecyclerViewPremiosAdapter(Context mContext, List<Premios> mPremios) {
        this.mContext = mContext;
        this.mPremios = mPremios;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista;
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        vista=mInflater.inflate(R.layout.cardview_item_premios,parent,false);
        return new MyViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.textViewTitulo.setText(mPremios.get(position).getTitulo());
        holder.imageViewPremio.setImageResource(mPremios.get(position).getMiniatura());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, DescriptionServices.class);
                intent.putExtra("Title", mPremios.get(position).getTitulo());
                intent.putExtra("Description", mPremios.get(position).getDescripcion());
                intent.putExtra("ServiceThumbnail", mPremios.get(position).getMiniatura());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPremios.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitulo;
        ImageView imageViewPremio;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);

            textViewTitulo=(TextView) itemView.findViewById(R.id.titulo_premio_id);
            imageViewPremio=(ImageView) itemView.findViewById(R.id.img_premio_id);
            cardView=(CardView) itemView.findViewById(R.id.cardview_items);
        }
    }
}
