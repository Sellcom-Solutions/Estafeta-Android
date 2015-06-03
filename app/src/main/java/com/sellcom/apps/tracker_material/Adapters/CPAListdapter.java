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
import java.util.List;
import java.util.Map;

/**
 * Created by rebecalopezmartinez on 03/06/15.
 */
public class CPAListdapter extends BaseAdapter{


    String TAG = "CP_ADAPTER_LOG";

    Context context;
    //private List<Map<String,String>> codigos;
    List<String> colonias;


    public  CPAListdapter (Context context, List colonias){
        this.context        = context;
        this.colonias       = (List <String>) colonias;
        Log.d("CPAdapter", "sz"+colonias.size());

    }

    class ColoniasViewHolder{
        TextView tv_colonias;
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
        if (convertView == null){
            holder                      = new ColoniasViewHolder();
            convertView                 = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_spinner,parent,false);
            holder.tv_colonias          = (TextView)convertView.findViewById(R.id.txt_spn_item);

            convertView.setTag(holder);
        }
        else{
            holder  = (ColoniasViewHolder)convertView.getTag();
        }

        try {
            Log.d("Adapter","size"+colonias.size());

            String coloniaStr = this.colonias.get(position);
            Log.d("Adapter","colonia"+coloniaStr);

            holder.tv_colonias.setText(coloniaStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
