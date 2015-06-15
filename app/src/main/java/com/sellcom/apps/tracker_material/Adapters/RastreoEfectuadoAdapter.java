package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Favorites;
import database.model.Trackdata;

/**
 * Created by rebecalopezmartinez on 30/05/15.
 */
public class RastreoEfectuadoAdapter extends BaseAdapter{
    String TAG = "RASTREO_EFECTUADO_ADAPTER_LOG";

    Context context;
    private ArrayList<Map<String,String>> codigos;


    public  RastreoEfectuadoAdapter (Context context, ArrayList<Map<String,String>> codigos){
        this.context        = context;
        this.codigos       = codigos;
        Log.d("Cod adapter size",""+codigos.size());
    }

    class CodigosViewHolder{
        TextView        no_guia;
        TextView        codigo;
        TextView        estatus;
        CheckBox        btn_favoritos;
        ImageView       img_status;
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
                Log.d("Codigo RE Adapter", "" + estatusStr);
                holder.estatus.setText("Pendiente de entrega");
                break;

            case "ENTREGADO":
                holder.img_status.setImageResource(R.drawable.estatus_entregado);
                Log.d("Codigo RE Adapter", "" + estatusStr);
                holder.estatus.setText("Entregado");
                break;

            default:
                holder.img_status.setImageResource(R.drawable.estatus_sin);
                Log.d("Codigo RE Adapter", "" + estatusStr);
                holder.estatus.setText("Sin información");
                break;
        }

        holder.btn_favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btn_favoritos.isChecked()) {
                    if (holder.estatus.getText().toString().equals("Sin información")) {
                        holder.btn_favoritos.setChecked(false);
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, context.getString(R.string.error_agregar_fav),1000);
                        Log.d(TAG,"Position"+holder.position);
                    }
                    else {
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SUCCESS, context.getString(R.string.exito_agregar_fav),1000);
                        holder.btn_favoritos.setEnabled(false);
                        Log.d(TAG,"Position"+holder.position);

                        try {
                            Favorites.insertMap(context, codigos.get(holder.position));
                            Log.d(TAG,"Despues de insertar en fav");
                            String id= Favorites.getIdByWayBill(context,codigos.get(holder.position).get("wayBill"));
                            Log.d(TAG,"Recuperar id tabla "+id);
                            codigos.get(holder.position).put("id_ctl_favoritos",id);
                            Trackdata.insert(codigos.get(holder.position));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        return convertView;
    }
}
