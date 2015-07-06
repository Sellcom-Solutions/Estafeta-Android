package com.sellcom.apps.tracker_material.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anel.calvo on 02/07/2015.
 */
public class FavoriteEditAdapter extends BaseAdapter {

    Context context;
    Activity activity;

    String TAG = "FAVORITE_EDIT_ADAPTER";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TrackerFragment fragment;

    private ArrayList<Map<String,String>> codigos;

    public FavoriteEditAdapter(Activity activity,Context context, ArrayList<Map<String,String>> codigos, FragmentManager fragmentManager) {
        this.codigos        =codigos;
        this.activity       =activity;
        this.context        = context;
        this.fragmentManager=fragmentManager;
    }
    class CodigosViewHolder{

        TextView    no_guia;
        TextView    codigo;


    }

    @Override
    public int getCount() {
        return codigos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       final CodigosViewHolder holder;
        if (convertView == null) {
            holder = new CodigosViewHolder();
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.fragment_dialog_edit_favorite, parent, false);
            holder.no_guia = (TextView) convertView.findViewById(R.id.fav_edit_no_guia);
            holder.codigo = (TextView) convertView.findViewById(R.id.fav_edit_cod_rastreo);

            convertView.setTag(holder);
        } else {
            holder = (CodigosViewHolder) convertView.getTag();
        }

        Map<String, String> codigos_copy = new HashMap<>();
        codigos_copy = codigos.get(position);



        String noGuia = codigos_copy.get("no_guia");
        Log.d("noGuia FA Adapter", "" + noGuia);
        holder.no_guia.setText(noGuia);

        String codigoStr = codigos_copy.get("codigo_rastreo");
        Log.d("Codigo FA Adapter", "" + codigoStr);
        holder.codigo.setText(codigoStr);



        return convertView;
    }
}
