package com.estafeta.estafetamovilv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.estafeta.estafetamovilv1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rebecalopezmartinez on 22/05/15.
 * This adapter is for FragmentRastreo class.
 */

public class RastreoListAdapter extends BaseAdapter {

    String TAG = "RASTREO_ADAPTER_LOG";

    Context context;
    private ArrayList<Map<String,String>> codigos;
    setNumCodes setNumCodes;

    /**
     *  Monitors the status of the trash to remove the scan codes and tracking numbers entered.
     */
    public static int[] stateDelete = {0,0,0,0,0,0,0,0,0,0};


    public  RastreoListAdapter (Context context, ArrayList<Map<String,String>> codigos){
        this.context        = context;
        this.codigos       = codigos;
    }

    class CodigosViewHolder{
        TextView        tipo_codigo;
        TextView        no_codigo;
        LinearLayout    lin_delete;
        LinearLayout    lin_container_codes;
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

    /**
     * This method creates the views that appear in each list item.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //final CodigosViewHolder   holder;
        //if (convertView == null){
            //holder                      = new CodigosViewHolder();
            convertView                 = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_rastreo,parent,false);
            final TextView tipo_codigo          = (TextView)convertView.findViewById(R.id.li_tipo_codigo);
        final TextView no_codigo            = (TextView)convertView.findViewById(R.id.li_codigo);
        final LinearLayout lin_delete           = (LinearLayout)convertView.findViewById(R.id.lin_delete);
        final LinearLayout lin_container_codes  = (LinearLayout)convertView.findViewById(R.id.lin_container_codes);
            //convertView.setTag(holder);
        //}
        //else{
          //  holder  = (CodigosViewHolder)convertView.getTag();
        //}

        try {

            if(stateDelete[position] == 1){
                lin_delete.setVisibility(View.VISIBLE);
            }else{
                lin_delete.setVisibility(View.GONE);
            }

            lin_container_codes.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(lin_delete.getVisibility() == View.GONE){
                        stateDelete[position] = 1;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_right);
                        lin_delete.startAnimation(animation);
                        lin_delete.setVisibility(View.VISIBLE);
                    }else if(lin_delete.getVisibility() == View.VISIBLE){
                        stateDelete[position] = 0;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_to_right);
                        lin_delete.startAnimation(animation);
                        lin_delete.setVisibility(View.GONE);
                    }
                    return true;
                }
            });

            lin_container_codes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lin_delete.getVisibility() == View.VISIBLE){
                        stateDelete[position] = 0;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_to_right);
                        lin_delete.startAnimation(animation);
                        lin_delete.setVisibility(View.GONE);
                    }
                }
            });


            lin_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNumCodes.removeCode(position);
                }
            });

            Map<String, String> cod = new HashMap<>();
            cod = codigos.get(position);
            final String codStr = cod.get("codigo");
            if(codStr.length() == 10)
                tipo_codigo.setText(context.getString(R.string.cod_rastreo) );
            else {
                tipo_codigo.setText(context.getString(R.string.no_guia));

            }

            int size = codigos.size();
            setNumCodes.setCodes(size);

            no_codigo.setText(codStr);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


    public interface setNumCodes{
        public void setCodes(int restantes);
        public void removeCode(int position);
    }

    public void setCodesNumbers(setNumCodes listener) {
        setNumCodes = listener;
    }

}
