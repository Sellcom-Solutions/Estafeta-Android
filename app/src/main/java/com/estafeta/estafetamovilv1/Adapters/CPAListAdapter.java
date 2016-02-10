package com.estafeta.estafetamovilv1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.estafeta.estafetamovilv1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rebecalopezmartinez on 03/06/15.
 * This class is responsible for creating the style of the item from the list of postal codes screen.
 */
public class CPAListAdapter extends BaseAdapter{


    public static final String TAG = "CP_ADAPTER_LOG";

    Activity activity;
    Context context;
    ArrayList<Map<String,String>> colonias;
    String tipo;


    private int lastSelectedItemPosition = 0;

    private View lastSelectedView;


    public CPAListAdapter(Activity activity, Context context, ArrayList<Map<String, String>> colonias, String tipo){
        this.activity = activity;
        this.context        = context;
        this.colonias       = colonias;
        this.tipo           = tipo;
        //Log.d("CPAdapter", "sz"+colonias.size());

    }

    class ColoniasViewHolder{
        TextView tv_colonias;
        TextView tv_cp;
        LinearLayout lin_item_cp;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ColoniasViewHolder   holder;
        if(tipo.equals("1")) {
            if (convertView == null) {
                holder = new ColoniasViewHolder();
                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_lv_cp, parent, false);
                holder.lin_item_cp = (LinearLayout)convertView.findViewById(R.id.lin_item_cp);
                holder.tv_colonias = (TextView) convertView.findViewById(R.id.txt_lvcp_item);
                holder.tv_cp = (TextView) convertView.findViewById(R.id.txt_lvcp_item1);


                convertView.setTag(holder);
            } else {
                holder = (ColoniasViewHolder) convertView.getTag();
            }

            try {
                //Log.d("Adapter", "size" + colonias.size());
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
        }else if(tipo.equals("cotizador")){
            if (convertView == null) {
                holder = new ColoniasViewHolder();
                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_lv_cp, parent, false);
                holder.lin_item_cp = (LinearLayout)convertView.findViewById(R.id.lin_item_cp);
                holder.tv_colonias = (TextView) convertView.findViewById(R.id.txt_lvcp_item);
                holder.tv_cp = (TextView) convertView.findViewById(R.id.txt_lvcp_item1);

                convertView.setTag(holder);
            } else {
                holder = (ColoniasViewHolder) convertView.getTag();
            }

            if( position == lastSelectedItemPosition )
                setSelectionState( true, convertView );
            else
                setSelectionState( false, convertView );

            try {
                //Log.d("Adapter", "size" + colonias.size());
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
                //Log.d("Adapter", "size" + colonias.size());
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

    public void setSelectionState( boolean state, View currentView ){
        if( state ){
            if( lastSelectedView != null )
                lastSelectedView.setBackgroundResource( 0 );
            if( currentView != null )
                currentView.setBackgroundResource( R.color.estafeta_soft_gray );

            lastSelectedView = currentView;
        }else{
            currentView.setBackgroundResource( 0 );

        }

    }
    public void setLastSelectedItemPosition( int position ){
        lastSelectedItemPosition = position;
    }
}
