package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rebecalopezmartinez on 22/05/15.
 */
public class RastreoListAdapter extends BaseAdapter {

    String TAG = "RASTREO_ADAPTER_LOG";

    Context context;
    private ArrayList<Map<String,String>> codigos;
    //String []codigos;
    //private List<Map<String,String>> codigos_copy;

    public  RastreoListAdapter (Context context, ArrayList<Map<String,String>> codigos){
        this.context        = context;
        this.codigos       = codigos;
        //this.codigos_copy  = new ArrayList<>(codigos);
    }



    class CodigosViewHolder{
        TextView        tipo_codigo;
        TextView        no_codigo;
        //ImageButton     favorito;
        CheckBox        favorito;
        int             position;

    }
    @Override
    public int getCount() {
        return codigos.size();
        //return 0;
    }

    @Override
    public Object getItem(int position) {
       return codigos.get(position);
       // return null;
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
            convertView                 = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_rastreo,parent,false);
            holder.tipo_codigo          = (TextView)convertView.findViewById(R.id.li_tipo_codigo);
            holder.no_codigo            = (TextView)convertView.findViewById(R.id.li_codigo);
            //holder.favorito             = (ImageButton)convertView.findViewById(R.id.btn_li_rastreo);
            holder.favorito             = (CheckBox) convertView.findViewById(R.id.btn_li_rastreo);
            convertView.setTag(holder);
        }
        else{
            holder  = (CodigosViewHolder)convertView.getTag();
        }

        //holder.tipo_codigo.setText(codigos[position]);

        try {
            Map<String, String> cod = new HashMap<>();
            cod = codigos.get(position);
            final String codStr = cod.get("codigo");
            if(codStr.length() == 10)
                holder.tipo_codigo.setText(context.getString(R.string.no_guia));
            else
                holder.tipo_codigo.setText(context.getString(R.string.cod_rastreo));

            holder.no_codigo.setText(codStr);
            holder.favorito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.favorito.isChecked()){
                        Map<String, String> aux = new HashMap<>();
                        aux.put("codigo",codStr);
                        aux.put("favorito","true");
                        Log.d("Favorito","true");
                        codigos.remove(position);
                        codigos.add(position,aux);
                    }
                    else {
                        Map<String, String> aux = new HashMap<>();
                        aux.put("codigo",codStr);
                        aux.put("favorito","false");
                        Log.d("Favorito","false");
                        codigos.remove(position);
                        codigos.add(position,aux);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
