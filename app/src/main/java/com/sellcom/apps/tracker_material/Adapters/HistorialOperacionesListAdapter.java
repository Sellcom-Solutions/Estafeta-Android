package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Buys;

/**
 * Created by juan.guerra on 15/07/2015.
 */
public class HistorialOperacionesListAdapter extends BaseAdapter{

    String TAG = "RASTREO_ADAPTER_LOG";

    Context context;
    private ArrayList<Map<String,String>> historial;
    private int tipo;
    public static final int PRELLENADOS = 0;
    public static final int COMPRAS = 1;

    public  HistorialOperacionesListAdapter (Context context, ArrayList<Map<String,String>> historial, int tipo){
        this.context        = context;
        this.historial       = historial;
        this.tipo = tipo;
    }

    class CodigosViewHolder{

        TextView txt_date;
        ImageView img_barcode;
        TextView txt_guia;
        TextView txt_origen;
        TextView txt_destino;
        int             position;
    }

    @Override
    public int getCount() {
        return historial.size();
    }

    @Override
    public Object getItem(int position) {
        return historial.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CodigosViewHolder   holder;
        if (convertView == null){
            holder                      = new CodigosViewHolder();
            convertView                 = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.items_history,parent,false);
            holder.txt_date          = (TextView)convertView.findViewById(R.id.txt_date);
            holder.txt_destino          = (TextView)convertView.findViewById(R.id.txt_destino);
            holder.txt_guia          = (TextView)convertView.findViewById(R.id.txt_guia);
            holder.txt_origen          = (TextView)convertView.findViewById(R.id.txt_origen);
            holder.img_barcode          = (ImageView)convertView.findViewById(R.id.img_barcode);
            convertView.setTag(holder);
        }
        else{
            holder  = (CodigosViewHolder)convertView.getTag();
        }

        try {
            Map<String, String> hist = new HashMap<>();
            hist = historial.get(position);

            holder.txt_date.setText(hist.get(Buys.DATE));
            holder.txt_destino.setText(hist.get(Buys.CPD) + ". " + hist.get(Buys.DESTINO));
            holder.txt_guia.setText(hist.get(Buys.REFERENCIA));
            holder.txt_origen.setText(hist.get(Buys.CPO) + ". " + hist.get(Buys.ORIGEN));

            if (tipo == COMPRAS){
                holder.img_barcode.setVisibility(View.INVISIBLE);
            }

            else{
                /*IMPORTANTE!!!!!! VALIDAR SI EL CODIGO ES PRELLENADO O NO Y PONER LA IMAGEN DE ACUERDO A ESO
            EN EL LAYOUT SE DEBE CAMBIAR EL COLOR DE FONDO A TRANSPARENTE
            holder.img_barcode.setImageResource(R.drawable.ic_filtro_gris);
            */
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
