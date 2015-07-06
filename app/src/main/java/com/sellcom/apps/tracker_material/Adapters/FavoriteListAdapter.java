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

import com.sellcom.apps.tracker_material.Fragments.FragmentDialogEditFavorite;
import com.sellcom.apps.tracker_material.Fragments.FragmentDialogFavorite;
import com.sellcom.apps.tracker_material.Fragments.FragmentFavorites;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Codes;

/**
 * Created by anel on 30/06/2015.
 */
public class FavoriteListAdapter extends BaseAdapter{


    Context context;
    Activity activity;

    private ArrayList<Map<String,String>> codigos;

    String TAG = "FAVORITE_LIST_ADAPTER_LOG";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TrackerFragment fragment;


    public FavoriteListAdapter(Activity activity,Context context, ArrayList<Map<String,String>> codigos, FragmentManager fragmentManager) {
        this.codigos        =codigos;
        this.activity       =activity;
        this.context        = context;
        this.fragmentManager=fragmentManager;
    }
    class CodigosViewHolder{

        int         position;
        TextView    referencia;
        TextView    no_guia;
        TextView    codigo;
        TextView    estatus;
        ImageButton btn_editar;

        LinearLayout    linear_favorite;

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
                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_favorite, parent, false);
                holder.referencia = (TextView) convertView.findViewById(R.id.favorite_reference);
                holder.no_guia = (TextView) convertView.findViewById(R.id.favorite_NoGuia);
                holder.codigo = (TextView) convertView.findViewById(R.id.favorite_cod_rastreo);
                holder.estatus = (TextView) convertView.findViewById(R.id.favorite_ServiceStatus);
                holder.btn_editar = (ImageButton) convertView.findViewById(R.id.btn_editar);
                holder.linear_favorite=(LinearLayout)convertView.findViewById((R.id.linear_favorite));

                holder.position = position;
                convertView.setTag(holder);
            } else {
                holder = (CodigosViewHolder) convertView.getTag();
            }


        Map<String, String> codigos_copy = new HashMap<>();
        codigos_copy = codigos.get(position);

        String reference = codigos_copy.get("referencia");
        Log.d("Reference FA Adapter", "" + reference);
        holder.referencia.setText(reference);


        String noGuia = codigos_copy.get("no_guia");
        Log.d("noGuia FA Adapter", "" + noGuia);
        holder.no_guia.setText(noGuia);

        String codigoStr = codigos_copy.get("codigo_rastreo");
        Log.d("Codigo FA Adapter", "" + codigoStr);
        holder.codigo.setText(codigoStr);

        String estatusStr = codigos_copy.get("estatus");
        Log.d("estatus FA Adapter", "" + estatusStr);
        holder.estatus.setText(estatusStr);


        String favorite = codigos_copy.get("favorites");

        Log.d("favorite FA Adapter", "" + favorite);



        holder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btn edit: ");
                FragmentDialogEditFavorite fdfe = new FragmentDialogEditFavorite();
                fdfe.show(fragmentManager,"FRAG_DIALOG_FAVORITE_EDIT");

            }
        });

       holder.linear_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "list edit: ");
                FragmentDialogFavorite fdf = new FragmentDialogFavorite();
                fdf.show(fragmentManager, "FRAG_DIALOG_FAVORITE");
            }
        });
        return convertView;
    }
}
