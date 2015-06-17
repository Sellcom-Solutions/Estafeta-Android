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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sellcom.apps.tracker_material.Fragments.FragmentDetalleRastreo;
import com.sellcom.apps.tracker_material.Fragments.FragmentRastreoEfectuado;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Favorites;
import database.model.History;
import database.model.Trackdata;

import static android.app.PendingIntent.getActivity;

/**
 * Created by rebecalopezmartinez on 30/05/15.
 */
public class RastreoEfectuadoAdapter extends BaseAdapter{
    String TAG = "RASTREO_EFECTUADO_ADAPTER_LOG";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TrackerFragment fragment;

    Context context;
    Activity activity;
    private ArrayList<Map<String,String>> codigos;


    public  RastreoEfectuadoAdapter (Activity activity,Context context, ArrayList<Map<String,String>> codigos, FragmentManager fragmentManager){
        this.context        = context;
        this.codigos        = codigos;
        this.activity       =activity;
        this.fragmentManager=fragmentManager;
        Log.d("Cod adapter size",""+codigos.size());
    }

    class CodigosViewHolder{
        TextView        no_guia;
        TextView        codigo;
        TextView        estatus;
        CheckBox        btn_favoritos;
        ImageView       img_status;
        LinearLayout    linear_rastreo;
        int             position;

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
        final CodigosViewHolder   holder;
        if (convertView == null){
            holder                  = new CodigosViewHolder();
            convertView             = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_rastreo_efectuado,parent,false);
            holder.codigo           = (TextView)convertView.findViewById(R.id.txt_codigo_rastreo);
            holder.no_guia          = (TextView)convertView.findViewById(R.id.txt_no_guia);
            holder.estatus          = (TextView)convertView.findViewById(R.id.txt_estatus);
            holder.btn_favoritos    = (CheckBox)convertView.findViewById(R.id.btn_favorito);
            holder.img_status       = (ImageView) convertView.findViewById(R.id.img_status);
            holder.linear_rastreo   = (LinearLayout) convertView.findViewById(R.id.linear_rastreo);
            holder.position         = position;
            convertView.setTag(holder);
        }
        else{
            holder  = (CodigosViewHolder)convertView.getTag();
        }

        Map<String,String> codigos_copy=new HashMap<>();
        codigos_copy= codigos.get(position);

        String noGuia = codigos_copy.get("wayBill");
        Log.d("Codigo RE Adapter",""+noGuia);
        holder.no_guia.setText(noGuia);

        String codigoStr = codigos_copy.get("shortWayBillId");
        Log.d("Codigo RE Adapter",""+codigoStr);
        holder.codigo.setText(codigoStr);

        String estatusStr = codigos_copy.get("statusSPA");


        switch (estatusStr) {
            case "EN_TRANSITO":
                holder.img_status.setImageResource(R.drawable.estatus_transito);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                holder.estatus.setText("Pendiente de entrega");
                break;

            case "ENTREGADO":
                holder.img_status.setImageResource(R.drawable.estatus_entregado);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                holder.estatus.setText("Entregado");
                break;

            default:
                holder.img_status.setImageResource(R.drawable.estatus_sin);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                holder.estatus.setText("Sin información");
                break;
        }

        String favorite = codigos_copy.get("favorites");
        Log.d("Codigo RE Adapter",""+favorite);
        if(favorite.equals("true")){
            holder.btn_favoritos.setChecked(true);
            holder.btn_favoritos.setEnabled(false);
        }

        holder.btn_favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btn_favoritos.isChecked()) {
                    if (holder.estatus.getText().toString().equals("Sin información")) {
                        holder.btn_favoritos.setChecked(false);
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, context.getString(R.string.error_agregar_fav),1000);
                        //Log.d(TAG,"Position"+holder.position);
                    }
                    else {
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SUCCESS, context.getString(R.string.exito_agregar_fav),1000);
                        holder.btn_favoritos.setEnabled(false);
                        //Log.d(TAG,"Position"+holder.position);

                        try {
                            long idHistory = History.insertMap(context, codigos.get(holder.position));
                            Log.d(TAG,"Despues de insertar en History");
                            codigos.get(holder.position).put("history_id",String.valueOf(idHistory));

                            Favorites.insertMap(context, codigos.get(holder.position));
                            Log.d(TAG,"Despues de insertar en fav");

                            String id= Favorites.getIdByWayBill(context,codigos.get(holder.position).get("wayBill"));
                            Log.d(TAG,"Recuperar id favorites"+id);

                            ArrayList<Map<String, String>> auxFav= Favorites.getAll(context);
                            Log.d(TAG,"Recuperar tamaño fav"+auxFav.size());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        holder.linear_rastreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Módulo en Desarrollo", Toast.LENGTH_SHORT).show();

                /*if(codigos.get(holder.position).get("statusSPA").equals("Sin información")){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, context.getString(R.string.error_agregar_fav),1000);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("code",codigos.get(holder.position).get("wayBill"));
                    bundle.putSerializable("code_array", (java.io.Serializable) codigos.get(holder.position));

                    Log.d(TAG, "codigo enviado" + codigos.get(holder.position).get("wayBill"));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragment = new FragmentDetalleRastreo();
                    fragment.addFragmentToStack(activity);
                    fragment.setArguments(bundle);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.container, fragment, TAG);
                    fragmentTransaction.commit();
                   // DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SUCCESS, context.getString(R.string.exito_agregar_fav),1000);
                }*/
            }
        });

        return convertView;
    }
}
