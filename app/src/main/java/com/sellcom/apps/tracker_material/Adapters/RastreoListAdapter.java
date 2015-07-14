package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rebecalopezmartinez on 22/05/15.
 */

public class RastreoListAdapter extends BaseAdapter {

    String TAG = "RASTREO_ADAPTER_LOG";

    Context context;
    private ArrayList<Map<String,String>> codigos;

    public  RastreoListAdapter (Context context, ArrayList<Map<String,String>> codigos){
        this.context        = context;
        this.codigos       = codigos;
    }

    class CodigosViewHolder{
        TextView        tipo_codigo;
        TextView        no_codigo;
        int             position;

    }
    @Override
    public int getCount() {
        return codigos.size();
    }

    @Override
    public Object getItem(int position) {
       return codigos.get(position);
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
            convertView.setTag(holder);
        }
        else{
            holder  = (CodigosViewHolder)convertView.getTag();
        }

        try {
            Map<String, String> cod = new HashMap<>();
            cod = codigos.get(position);
            final String codStr = cod.get("codigo");
            if(codStr.length() == 10)
                holder.tipo_codigo.setText(context.getString(R.string.cod_rastreo) );
            else
                holder.tipo_codigo.setText(context.getString(R.string.no_guia));

            holder.no_codigo.setText(codStr);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
