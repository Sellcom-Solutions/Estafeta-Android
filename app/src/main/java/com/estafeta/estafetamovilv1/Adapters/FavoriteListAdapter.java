package com.estafeta.estafetamovilv1.Adapters;

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

import com.estafeta.estafetamovilv1.Fragments.FragmentDialogEditFavorite;
import com.estafeta.estafetamovilv1.Fragments.FragmentDialogFavorite;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Favorites;

/**
 * Created by anel on 30/06/2015.
 * This adapter is for FragmentFavorite class.
 */
public class FavoriteListAdapter extends BaseAdapter implements FragmentDialogEditFavorite.changeReference{


    Context context;
    Activity activity;

    /**
     * This list only stores a code scanning at a time.
     */
    private ArrayList<Map<String,String>> codigos;
    /**
     * List to control the favorites to be removed.
     */
    ArrayList<Integer> list_delete = new ArrayList<Integer>();

    String TAG = "FAVORITE_LIST_ADAPTER_LOG";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TrackerFragment fragment;
    private String type = "";
    delete  delete;
    TextView    referencia;
    TextView    no_guia;
    TextView    codigo;
    TextView    estatus;
    ImageButton btn_editar;
    CheckBox check_delete;

    LinearLayout    linear_favorite;


    public FavoriteListAdapter(Activity activity,Context context, ArrayList<Map<String,String>> codigos, FragmentManager fragmentManager,String type) {
        this.codigos        =codigos;
        this.activity       =activity;
        this.context        = context;
        this.fragmentManager=fragmentManager;
        this.type           = type;
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

    /**
     * This method creates the views that appear in each list item.
     * Here the removal of a favorite is controlled and information is sent to another fragment to click on an item.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_favorite, parent, false);
        TextView referencia = (TextView) view.findViewById(R.id.favorite_reference);
        TextView no_guia = (TextView) view.findViewById(R.id.favorite_NoGuia);
        final TextView codigo = (TextView) view.findViewById(R.id.favorite_cod_rastreo);
        TextView estatus = (TextView) view.findViewById(R.id.favorite_ServiceStatus);
        ImageButton btn_editar = (ImageButton) view.findViewById(R.id.btn_editar);
        LinearLayout linear_favorite = (LinearLayout) view.findViewById((R.id.linear_favorite));
        CheckBox check_delete = (CheckBox) view.findViewById(R.id.check_delete);


        Map<String, String> codigos_copy = new HashMap<>();
        codigos_copy = codigos.get(position);

        Log.d("Reference FA codigocopy", "" + codigos_copy);

        String reference = codigos_copy.get("referencia");
        Log.d("Reference FA Adapter", "" + reference);
        referencia.setText(reference);


        String noGuia = codigos_copy.get("no_guia");
        Log.d("noGuia FA Adapter", "" + noGuia);
        no_guia.setText(noGuia);

        String codigoStr = codigos_copy.get("codigo_rastreo");
        Log.d("Codigo FA Adapter", "" + codigoStr);
        codigo.setText(codigoStr);

        String estatusStr = codigos_copy.get("estatus");
        Log.d("estatus FA Adapter1", "" + estatusStr);

//        estatusStr= selectImageOnStatus(estatusStr);
//
//        Log.d("estatus FA Adapter", "" + estatusStr);
        /*switch (estatusStr) {
            case "celda_pr":

                holder.estatus.setText("Pendiente en tránsito");
                break;

            case "celda_pe":
                //img_estatus.setImageResource(R.drawable.estatus_pendiente);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                holder.estatus.setText("Pendiente");
                break;

            case "celda_en":
                //img_estatus.setImageResource(R.drawable.estatus_entregado);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                holder.estatus.setText("Entregado");
                break;

            default:
                //img_estatus.setImageResource(R.drawable.estatus_sin);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                holder.estatus.setText("Sin información");
                break;
        }*/
        estatus.setText(estatusStr);

        if (type.equalsIgnoreCase("favorite")) {
            linear_favorite.setEnabled(true);
            check_delete.setVisibility(View.GONE);
            btn_editar.setVisibility(View.VISIBLE);
        } else if (type.equalsIgnoreCase("delete")) {
            linear_favorite.setEnabled(false);
            btn_editar.setVisibility(View.GONE);
            check_delete.setVisibility(View.VISIBLE);
        }

        btn_editar.setTag(position);
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btn edit: ");

                int pos = (int) v.getTag();

                Bundle bundle = new Bundle();
                bundle.putString("no_guia", codigos.get(pos).get("no_guia"));
                bundle.putString("codigo_rastreo", codigos.get(pos).get("codigo_rastreo"));
                bundle.putSerializable("code_array", (java.io.Serializable) codigos.get(pos));

                FragmentDialogEditFavorite fdfe = new FragmentDialogEditFavorite();
                fdfe.setChangeReference(FavoriteListAdapter.this);
                fdfe.setArguments(bundle);
                fdfe.show(fragmentManager, fdfe.TAG);

            }
        });


        if (list_delete.size() != 0){
            for(int i= 0; i<list_delete.size(); i++) {
                if(list_delete.get(i) == position) {
                    Log.d(TAG, "POS en getView: " + position);
                    check_delete.setChecked(true);
                }
            }
        }


        check_delete.setTag(position);
        check_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = (int) buttonView.getTag();
                Log.d(TAG, "POS en onCheckedChanged: "+pos);

                if (isChecked) {
                    delete.deleteFavoriteById(codigos.get(pos));
                    list_delete.add(pos);
                } else {
                    for(int i=0; i<list_delete.size(); i++){
                        if(list_delete.get(i) == pos){
                            list_delete.remove(i);
                        }
                    }
                    delete.cancelDeleteById(codigos.get(pos));
                }
            }
        });

        linear_favorite.setTag(position);
       linear_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "list edit: ");

                int pos = (int)v.getTag();

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
        return view;




    }

    /**
     * This method notifies you when a reference was changed.
     */
    @Override
    public void changeDBReference() {
        delete.changeReference();

    }

    /**
     * This interface notifies the FragmentFavorites when a favorite is deleted.
     */
    public interface delete{
        public void deleteFavoriteById(Map<String,String> favoriteDelete);
        public void cancelDeleteById(Map<String,String> favoriteDelete);
        public void changeReference();
    }


    public void setDelete(delete listener) {
        delete = listener;
    }

    /**
     * This method determines that status has a scan code.
     */
    public String selectImageOnStatus(String status){

        String imagen = "celda_";
        if (status == null) {
            imagen = imagen + "no";
        } else {
            if (status.equals("CONFIRMADO")) {
                imagen = imagen + "en";
            } else if (status.equals("DEVUELTO")) {
                imagen = imagen + "pe";
            } else if (status.equals("EN_TRANSITO")) {
                    imagen = imagen + "pe";
                }
            else
                imagen = imagen + "no";
            }
        return imagen;
    }

}
