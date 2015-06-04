package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rebecalopezmartinez on 03/06/15.
 */
public class CPAListdapter extends BaseAdapter{


    String TAG = "CP_ADAPTER_LOG";

    Context context;
    ArrayList<Map<String,String>> colonias;
    String tipo;


    public  CPAListdapter (Context context, ArrayList<Map<String,String>> colonias, String tipo){
        this.context        = context;
        this.colonias       = colonias;
        this.tipo           = tipo;
        Log.d("CPAdapter", "sz"+colonias.size());

    }

    class ColoniasViewHolder{
        TextView tv_colonias;
        TextView tv_cp;
        int      position;

    }
    @Override
    public int getCount() {
        return colonias.size();
        //return 0;
    }

    @Override
    public Object getItem(int position) {
         return colonias.get(position);
        //return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ColoniasViewHolder   holder;
        if(tipo.equals("1")) {
            if (convertView == null) {
                holder = new ColoniasViewHolder();
                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_lv_cp, parent, false);
                holder.tv_colonias = (TextView) convertView.findViewById(R.id.txt_lvcp_item);
                holder.tv_cp = (TextView) convertView.findViewById(R.id.txt_lvcp_item1);

                convertView.setTag(holder);
            } else {
                holder = (ColoniasViewHolder) convertView.getTag();
            }

            try {
                Log.d("Adapter", "size" + colonias.size());
                Map<String, String> col = new HashMap<>();
                col = colonias.get(position);
                String coloniaStr = col.get("colonia");
                String cp = col.get("cp");
                //Log.d("Adapter","colonia"+coloniaStr);
                holder.tv_colonias.setText(coloniaStr);
                holder.tv_cp.setText(cp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            if (convertView == null) {
                holder = new ColoniasViewHolder();
                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_spinner, parent, false);
                holder.tv_colonias = (TextView) convertView.findViewById(R.id.txt_spn_item);
                //holder.tv_cp = (TextView) convertView.findViewById(R.id.txt_lvcp_item1);

                convertView.setTag(holder);
            } else {
                holder = (ColoniasViewHolder) convertView.getTag();
            }

            try {
                Log.d("Adapter", "size" + colonias.size());
                Map<String, String> col = new HashMap<>();
                col = colonias.get(position);
                String coloniaStr = col.get("colonia");
                //String cp = col.get("cp");
                //Log.d("Adapter","colonia"+coloniaStr);
                holder.tv_colonias.setText(coloniaStr);
                //holder.tv_cp.setText(cp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }
}
