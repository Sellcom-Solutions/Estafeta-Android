package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rebecalopezmartinez on 22/05/15.
 */
public class RastreoListAdapter extends BaseAdapter {

    String TAG = "RASTREO_ADAPTER_LOG";

    Context context;
    //private List<Map<String,String>> codigos;
    String []codigos;
    private List<Map<String,String>> codigos_copy;

    public  RastreoListAdapter (Context context, /*List<Map<String,String>> */String []codigos){
        this.context        = context;
        this.codigos       = codigos;
        //this.codigos_copy  = new ArrayList<>(codigos);
    }

    class CodigosViewHolder{
        TextView        tipo_codigo;
        TextView        no_codigo;
        ImageButton     favorito;
        int             position;

    }
    @Override
    public int getCount() {
        return codigos.length;
        //return 0;
    }

    @Override
    public Object getItem(int position) {
       // return codigos.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CodigosViewHolder   holder;
        if (convertView == null){
            holder                      = new CodigosViewHolder();
            convertView                 = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_rastreo,parent,false);
            holder.tipo_codigo          = (TextView)convertView.findViewById(R.id.li_tipo_codigo);
            holder.no_codigo            = (TextView)convertView.findViewById(R.id.li_codigo);
            holder.favorito             = (ImageButton)convertView.findViewById(R.id.btn_li_rastreo);

            convertView.setTag(holder);
        }
        else{
            holder  = (CodigosViewHolder)convertView.getTag();
        }

        holder.tipo_codigo.setText(codigos[position]);

        return convertView;
    }
}
