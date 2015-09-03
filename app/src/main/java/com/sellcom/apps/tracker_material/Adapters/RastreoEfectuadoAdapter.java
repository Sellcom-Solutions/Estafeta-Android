package com.sellcom.apps.tracker_material.Adapters;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
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
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesign.views.Button;
import com.sellcom.apps.tracker_material.Fragments.FragmentDetalleRastreo;
import com.sellcom.apps.tracker_material.Fragments.FragmentRastreoEfectuado;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Codes;
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
    private Map<String,String> codigos_copy;


    Context context;
    Activity activity;
    String new_status;

    private ArrayList<ArrayList<Map<String,String>>> codigos;


    public  RastreoEfectuadoAdapter (Activity activity,Context context, ArrayList<ArrayList<Map<String,String>>> codigos, FragmentManager fragmentManager){
        this.context        = context;
        this.codigos        = codigos;
        this.activity       =activity;
        this.fragmentManager=fragmentManager;
        Log.d("Cod adapter size",""+codigos.size());
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

        View view =  ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_rastreo_efectuado,parent,false);

        TextView codigo           = (TextView)view.findViewById(R.id.txt_codigo_rastreo);
        TextView no_guia          = (TextView)view.findViewById(R.id.txt_no_guia);
        TextView estatus          = (TextView)view.findViewById(R.id.txt_estatus);
        CheckBox btn_favoritos    = (CheckBox)view.findViewById(R.id.btn_favorito);
        ImageView img_status       = (ImageView) view.findViewById(R.id.img_status);
        LinearLayout linear_rastreo   = (LinearLayout) view.findViewById(R.id.linear_rastreo);



        codigos_copy= codigos.get(position).get(0);

        String noGuia = codigos_copy.get("wayBill");
        Log.d("Codigo RE Adapter",""+noGuia);
        no_guia.setText(noGuia);

        String codigoStr = codigos_copy.get("shortWayBillId");
        Log.d("Codigo RE Adapter",""+codigoStr);
        codigo.setText(codigoStr);

        String estatusStr = codigos_copy.get("statusSPA");
        String codigoExcStr = codigos_copy.get("H_exceptionCode");

        String estatus_aux= selectImageOnStatus(estatusStr, codigoExcStr);

        switch (estatus_aux) {
            case "celda_pr":
                img_status.setImageResource(R.drawable.estatus_transito);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                estatus.setText("Pendiente en tránsito");
                new_status="Pendiente en tránsito";
                codigos.get(position).get(0).put("estatus1",new_status);
                break;

            case "celda_pe":
                img_status.setImageResource(R.drawable.estatus_transito);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                estatus.setText("Pendiente");
                new_status="Pendiente";
                codigos.get(position).get(0).put("estatus1", new_status);
                break;

            case "celda_en":
                img_status.setImageResource(R.drawable.estatus_entregado);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                estatus.setText("Entregado");
                new_status="Entregado";
                codigos.get(position).get(0).put("estatus1", new_status);
                break;

            default:
                img_status.setImageResource(R.drawable.estatus_sin);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                estatus.setText("Sin información");
                new_status="Sin información";
                codigos.get(position).get(0).put("estatus1",new_status);
                break;
        }

        String favorite = codigos_copy.get("favorites");
        Log.d("Codigo RE Adapter", "" + favorite);
        if(favorite.equals("true")){
            btn_favoritos.setChecked(true);
            btn_favoritos.setEnabled(false);
            //btn_favoritos.setVisibility(View.INVISIBLE);
        }

        btn_favoritos.setTag(position);
        btn_favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                CheckBox btnFav = (CheckBox)v;
                codigos_copy= codigos.get(pos).get(0);

                Log.e(TAG, "Posición: " + pos);
                if (btnFav.isChecked()) {
                    if (codigos.get(pos).get(0).get("estatus1").equals("Sin información")) {
                        btnFav.setChecked(false);
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, context.getString(R.string.error_agregar_fav), 3000);
                        //Log.d(TAG,"Position"+holder.position);
                    } else {
                        try {
                            btnFav.setEnabled(false);

                            long idFavorite = Favorites.insertMap(context, codigos.get(pos).get(0));


                            for (int i = 1; i < codigos.get(pos).size(); i++) {

                                codigos.get(pos).get(i).put("favorite_id", String.valueOf(idFavorite));
                                History.insertMap(context, codigos.get(pos).get(i));
                                Log.d(TAG, "INSERT IN HISTORY");


                            }
                            codigos_copy.put("favorites", "true");

                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SUCCESS, context.getString(R.string.exito_agregar_fav), 4050);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        linear_rastreo.setTag(position);
        linear_rastreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int)v.getTag();

                if(codigos.get(pos).get(0).get("statusSPA").equals("Sin información")){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No existe información acerca de este registro.",3000);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("code", codigos.get(pos).get(0).get("wayBill"));
                    bundle.putSerializable("code_array", codigos.get(pos));

                    Log.d(TAG, "codigo enviado" + codigos.get(pos).get(0).get("wayBill"));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragment = new FragmentDetalleRastreo();
                    fragment.addFragmentToStack(activity);
                    fragment.setArguments(bundle);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.container, fragment, TAG);
                    fragmentTransaction.commit();
                    // DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SUCCESS, context.getString(R.string.exito_agregar_fav),1000);
                }
            }
        });

        return view;
    }

    public String selectImageOnStatus(String status, String code){

        boolean conCodigo = false;
        if (code != null)
            conCodigo = Codes.existsZclave(code, context);

        String imagen = "celda_";
        if (status == null) {
            imagen = imagen + "no";
        } else {
            if (status.equals("CONFIRMADO")) {
                imagen = imagen + "en";
            } else if (status.equals("DEVUELTO")) {
                imagen = imagen + "pe";
            } else if (status.equals("EN_TRANSITO")) {
                if (conCodigo) {
                    imagen = imagen + "pe";
                } else {
                    imagen = imagen + "pr";
                }
            }
        }
        return imagen;
    }





}
