package com.device.aquafox.ui.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.device.aquafox.R;
import com.device.aquafox.data.Services;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.List;



public class ServiceAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<Services> services;
    public ServiceAdapter(Context applicationContext, List<Services> services) {
        this.context = context;
        this.services = services;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return this.services.size();
    }

    @Override
    public Object getItem(int i) {
        return this.services.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.list_servicios, null);

        ImageView icon = (ImageView) view.findViewById(R.id.icon_next);

        final Services item = this.services.get(i);
        icon.setClickable(true);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("descripcion_servicio", item.getDescripcion_servicio());
                bundle.putString("consumo", String.valueOf(item.getConsumo()));
                bundle.putString("estado", String.valueOf(item.getEstado()));
                bundle.putString("direccion", item.getDireccion());
                bundle.putString("inscripcion", item.getInscripcion());
                bundle.putString("finalizacion", item.getFinalizacion());
                bundle.putString("alias", item.getAlias());
                bundle.putString("fecha_registro", item.getFecha_registro());
                bundle.putString("serie", item.getSerie());
                bundle.putString("ubigeo", item.getUbigeo());


                Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_detalleServiceFragment, bundle);

            }
        });

        TextView title = (TextView) view.findViewById(R.id.service_title);
        //TextView subtitle = (TextView) view.findViewById(R.id.service_subtitle);
        //TextView info = (TextView) view.findViewById(R.id.service_info);
        DonutProgress donutProgress =  view.findViewById(R.id.donut_progress);
        //donutProgress.setTextSize(16);
        //donutProgress.setText(this.services.get(i).getConsumo().toString() +" LT");
        donutProgress.setText("S/ "+this.services.get(i).getPrecio().toString());
        //donutProgress.setText(this.services.get(i).getPorcentaje().toString() );

        donutProgress.setProgress(this.services.get(i).getPorcentaje());
        //title.setText(this.services.get(i).getAlias());

        //String alias = this.services.get(i).getAlias()+'\n'+this.services.get(i).getConsumo().toString() +" LT";
        String alias = this.services.get(i).getAlias()+" \nSerie: "+this.services.get(i).getSerie();
        String metro3 = this.services.get(i).getMetro3().toString()+" m3";
        title.setText(alias+" \n"+metro3);
        //subtitle.setText(metro3);
        //info.setText(this.services.get(i).getConsumo().toString() +" LT");

        /*
        TextView title = (TextView) view.findViewById(R.id.service_title);
        TextView info = (TextView) view.findViewById(R.id.service_info);
        ImageView icon = (ImageView) view.findViewById(R.id.service_icon);
        //ImageView iconLink = (ImageView) view.findViewById(R.id.service_icon_link);
        title.setText(this.services.get(i).getDescripcion_servicio());
        info.setText(this.services.get(i).getDireccion());
        icon.setImageResource(R.drawable.medidor);
        //icon.setImageResource(flags[i]);
        //iconLink.setImageResource(flags[i]);;
        */
        return view;
    }

}