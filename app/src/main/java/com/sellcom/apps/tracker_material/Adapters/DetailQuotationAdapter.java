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

import java.util.ArrayList;
import java.util.Map;

public class DetailQuotationAdapter extends BaseAdapter {

    private Context context;
    private static Activity activity;
    private ArrayList<String> list;
    private static int lastSelectedItemPosition = 0;
    private static TextView txv_selection, txv_description;
    private static View lastSelectedView = null;

    public DetailQuotationAdapter(Context context, Activity activity, ArrayList<String> list){

        lastSelectedItemPosition = 0;
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



        if( position == lastSelectedItemPosition ) {
            setSelectionState(true, convertView, holder.txv_selection, holder.txv_descripcion_servicio);
            Log.d("getView", "TRUE lastSelectedItemPosition = " + lastSelectedItemPosition  );
        }
        else {
            setSelectionState(false, convertView, holder.txv_selection, holder.txv_descripcion_servicio);

        }

        return convertView;
    }

    class CodigosViewHolder{
        TextView        txv_descripcion_servicio;
        TextView        txv_selection;

    }

    public static void setSelectionState( boolean state, View currentView, TextView selection, TextView description){
        CodigosViewHolder holder  = (CodigosViewHolder)currentView.getTag();
        Log.d("setSelectionState"," ----!!");
        if( state ){
            if( lastSelectedView != null ) {
                Log.d("lastSelectedView"," entre!!");
                txv_selection.setVisibility(View.INVISIBLE);
                txv_description.setTextColor(activity.getResources().getColor(R.color.estafeta_soft_gray));
                lastSelectedView.setBackgroundResource(R.color.estafeta_light_gray);
            }
            if( currentView != null ) {
                Log.d("currentView"," entre!!");
                holder.txv_selection.setVisibility(View.VISIBLE);
                holder.txv_descripcion_servicio.setTextColor(activity.getResources().getColor(R.color.estafeta_text));
                currentView.setBackgroundResource(R.color.white);

                txv_selection = selection;
                txv_description = description;
                lastSelectedView = currentView;
            }

        }else{

            holder.txv_selection.setVisibility(View.INVISIBLE);
            holder.txv_descripcion_servicio.setTextColor(activity.getResources().getColor(R.color.estafeta_soft_gray));
            currentView.setBackgroundResource(R.color.estafeta_light_gray);


        }

    }

    public static void setLastSelectedItemPosition( int position ){
        lastSelectedItemPosition = position;
    }

}
