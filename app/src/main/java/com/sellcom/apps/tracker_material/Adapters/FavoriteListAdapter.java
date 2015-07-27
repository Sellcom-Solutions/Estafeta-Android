package com.sellcom.apps.tracker_material.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CheckBox;

import com.sellcom.apps.tracker_material.Fragments.FragmentDialogEditFavorite;
import com.sellcom.apps.tracker_material.Fragments.FragmentDialogFavorite;
import com.sellcom.apps.tracker_material.Fragments.FragmentFavorites;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Favorites;

/**
 * Created by anel on 30/06/2015.
 */
public class FavoriteListAdapter extends BaseAdapter implements FragmentDialogEditFavorite.changeReference{


    Context context;
    Activity activity;

    private ArrayList<Map<String,String>> codigos;

    String TAG = "FAVORITE_LIST_ADAPTER_LOG";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TrackerFragment fragment;
    private String type = "";
    delete  delete;


    public FavoriteListAdapter(Activity activity,Context context, ArrayList<Map<String,String>> codigos, FragmentManager fragmentManager,String type) {
        this.codigos        =codigos;
        this.activity       =activity;
        this.context        = context;
        this.fragmentManager=fragmentManager;
        this.type           = type;
    }

    class CodigosViewHolder{

        int         position;
        TextView    referencia;
        TextView    no_guia;
        TextView    codigo;
        TextView    estatus;
        ImageButton btn_editar;
        CheckBox check_delete;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

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
                holder.check_delete = (CheckBox)convertView.findViewById(R.id.check_delete);

                holder.position = position;
                convertView.setTag(holder);
            } else {
                holder = (CodigosViewHolder) convertView.getTag();
            }


        Map<String, String> codigos_copy = new HashMap<>();
        codigos_copy = codigos.get(position);

        Log.d("Reference FA codigocopy", "" + codigos_copy);

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

        if(type.equalsIgnoreCase("favorite")){
            holder.linear_favorite.setEnabled(true);
            holder.check_delete.setVisibility(View.GONE);
            holder.btn_editar.setVisibility(View.VISIBLE);
        }else if(type.equalsIgnoreCase("delete")){
            holder.linear_favorite.setEnabled(false);
            holder.btn_editar.setVisibility(View.GONE);
            holder.check_delete.setVisibility(View.VISIBLE);
        }

        holder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btn edit: ");

                Bundle bundle = new Bundle();
                bundle.putString("code", codigos.get(holder.position).get("no_guia"));
                bundle.putString("code", codigos.get(holder.position).get("codigo_rastreo"));
                bundle.putSerializable("code_array", (java.io.Serializable) codigos.get(holder.position));

                FragmentDialogEditFavorite fdfe = new FragmentDialogEditFavorite();
                fdfe.setChangeReference(FavoriteListAdapter.this);
                fdfe.setArguments(bundle);
                fdfe.show(fragmentManager, fdfe.TAG);

            }
        });


        holder.check_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    delete.deleteFavoriteById(codigos.get(holder.position));
                }else{
                    delete.cancelDeleteById(codigos.get(holder.position));
                }
            }
        });

       holder.linear_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "list edit: ");

                Bundle bundle = new Bundle();
                Map<String,String> map = Favorites.getFavoriteByWayBill(context, codigos.get(position).get("no_guia"));
                bundle.putSerializable("code_array", (java.io.Serializable) map);
                Log.d(TAG, "codigo MAP  " + String.valueOf(map));


                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentDialogFavorite fdf = new FragmentDialogFavorite();
                fdf.setArguments(bundle);
              //  fdf.show(fragmentManager, "FRAG_DIALOG_FAVORITE");
                fragmentTransaction.addToBackStack(null);
                fdf.addFragmentToStack(activity);
                fragmentTransaction.replace(R.id.container, fdf, TAG);
                fragmentTransaction.commit();
            }
        });
        return convertView;




    }

    @Override
    public void changeDBReference() {
        delete.changeReference();

    }

    public interface delete{
        public void deleteFavoriteById(Map<String,String> favoriteDelete);
        public void cancelDeleteById(Map<String,String> favoriteDelete);
        public void changeReference();
    }

    public void setDelete(delete listener) {
        delete = listener;
    }

}
