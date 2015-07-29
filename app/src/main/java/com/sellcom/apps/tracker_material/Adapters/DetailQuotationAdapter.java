package com.sellcom.apps.tracker_material.Adapters;

/**
 * Created by hugo.figueroa on 07/07/15.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import java.util.ArrayList;
import java.util.Map;

public class DetailQuotationAdapter extends BaseAdapter {

    private Context context;
    private static Activity activity;
    private ArrayList<String> list;
    public DetailQuotationAdapter(Context context, Activity activity, ArrayList<String> list){

        this.context = context;
        this.activity = activity;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final CodigosViewHolder   holder;
        if (convertView == null){
            holder                      = new CodigosViewHolder();
            convertView                 = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_detail_quotation,parent,false);
            holder.txv_descripcion_servicio          = (TextView)convertView.findViewById(R.id.txv_descripcion_servicio);
            holder.txv_selection                    = (TextView)convertView.findViewById(R.id.txv_selection);
            convertView.setTag(holder);
        }
        else{
            holder  = (CodigosViewHolder)convertView.getTag();
        }

        holder.txv_descripcion_servicio.setText("" + list.get(position));



        if(Utilities.position == position ) {
            //setSelectionState(true, convertView, holder.txv_selection, holder.txv_descripcion_servicio);
            holder.txv_selection.setVisibility(View.VISIBLE);
            holder.txv_descripcion_servicio.setTextColor(activity.getResources().getColor(R.color.estafeta_text));
            convertView.setBackgroundResource(R.color.white);
        }
        else {
            //setSelectionState(false, convertView, holder.txv_selection, holder.txv_descripcion_servicio);
            holder.txv_selection.setVisibility(View.INVISIBLE);
            holder.txv_descripcion_servicio.setTextColor(activity.getResources().getColor(R.color.estafeta_soft_gray));
            convertView.setBackgroundResource(R.color.estafeta_light_gray);
        }

        return convertView;
    }

    class CodigosViewHolder{
        TextView        txv_descripcion_servicio;
        TextView        txv_selection;

    }



}
