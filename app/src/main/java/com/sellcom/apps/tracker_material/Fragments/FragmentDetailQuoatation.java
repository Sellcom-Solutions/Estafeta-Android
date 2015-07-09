package com.sellcom.apps.tracker_material.Fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sellcom.apps.tracker_material.Adapters.DetailQuotationAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetailQuoatation extends TrackerFragment implements View.OnClickListener{

    public static final String TAG = "FRAG_DETAIL_QUOTATION";

    private Context         context;

    private TextView        txv_send_type,
                            txv_dimensiones,
                            txv_origen,
                            txv_destino,
                            txv_guia,
                            txv_cc_tarifa,
                            txv_cargo_extra,
                            txv_costo,
                            txv_cc_sobrepeso,
                            txv_costo_total,
                            txv_dias_entrega,
                            txv_frecuencia,
                            txv_ocurre_forzoso,
                            txv_costo_reexpedicion;

    private ImageView       imv_send_type;

    private ListView        lv_quotation;

    private List<Map<String,String>> list;

    private ArrayList<String>  servicioList;

    private Map<String,String> map;

    private String type;

    private DetailQuotationAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_quoatation, container, false);

        list    = (List<Map<String,String>>) getArguments().getSerializable("respCotizador");
        type    = getArguments().getString("type");

        txv_send_type           = (TextView)view.findViewById(R.id.txv_send_type);
        txv_dimensiones         = (TextView)view.findViewById(R.id.txv_dimensiones);
        txv_origen              = (TextView)view.findViewById(R.id.txv_origen);
        txv_destino             = (TextView)view.findViewById(R.id.txv_destino);
        txv_guia                = (TextView)view.findViewById(R.id.txv_guia);
        txv_cc_tarifa           = (TextView)view.findViewById(R.id.txv_cc_tarifa);
        txv_cargo_extra         = (TextView)view.findViewById(R.id.txv_cargo_extra);
        txv_costo               = (TextView)view.findViewById(R.id.txv_costo);
        txv_cc_sobrepeso        = (TextView)view.findViewById(R.id.txv_cc_sobrepeso);
        txv_costo_total         = (TextView)view.findViewById(R.id.txv_costo_total);
        txv_dias_entrega        = (TextView)view.findViewById(R.id.txv_dias_entrega);
        txv_frecuencia          = (TextView)view.findViewById(R.id.txv_frecuencia);
        txv_ocurre_forzoso      = (TextView)view.findViewById(R.id.txv_ocurre_forzoso);
        txv_costo_reexpedicion  = (TextView)view.findViewById(R.id.txv_costo_reexpedicion);

        imv_send_type           = (ImageView)view.findViewById(R.id.imv_send_type);

        final FloatingActionButton btn_call = (FloatingActionButton) view.findViewById(R.id.button_call);
        final FloatingActionButton btn_share = (FloatingActionButton) view.findViewById(R.id.button_share);

        btn_call.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        lv_quotation            = (ListView)view.findViewById(R.id.lv_quotation);
        lv_quotation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //view.setSelected(true);
                lv_quotation.setSelection(position);

                map = new HashMap<String,String>();
                map = list.get((position+1));

                txv_guia.setText("$"+Double.parseDouble(map.get("TarifaBase")));
                txv_cc_tarifa.setText("$" + Double.parseDouble(map.get("CCTarifaBase")));
                txv_cargo_extra.setText("$" + Double.parseDouble(map.get("CargosExtra")));

                txv_costo.setText("$" + Double.parseDouble(map.get("SobrePeso")));
                txv_cc_sobrepeso.setText("$" + Double.parseDouble(map.get("CCSobrePeso")));

                txv_costo_total.setText("$" + Double.parseDouble(map.get("CostoTotal")));

                TextView selection = (TextView)view.findViewById(R.id.txv_selection);
                TextView description = (TextView)view.findViewById(R.id.txv_descripcion_servicio);


                adapter.setSelectionState(true, view, selection, description);
                adapter.setLastSelectedItemPosition(position+1);


            }
        });




        printInformation(type);

        return view;
    }

    public void printInformation(String type){

        servicioList = new ArrayList<String>();

        for (int i = 1; i<list.size(); i++){

            if(list.get(i).get("AplicaCotizacion") != null &&
                    list.get(i).get("AplicaCotizacion").equalsIgnoreCase("Si") &&
                    list.get(i).get("AplicaServicio").equalsIgnoreCase("Si")){

                servicioList.add(list.get(i).get("DescripcionServicio"));
            }
        }

        switch (type){

            case "nacional_sobre":
                map = new HashMap<String,String>();
                map = list.get(1);

                imv_send_type.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.sobre));
                txv_send_type.setText("Sobre");

                txv_guia.setText("$"+Double.parseDouble(map.get("TarifaBase")));
                txv_cc_tarifa.setText("$" + Double.parseDouble(map.get("CCTarifaBase")));
                txv_cargo_extra.setText("$"+Double.parseDouble(map.get("CargosExtra")));

                txv_costo.setText("$" + Double.parseDouble(map.get("SobrePeso")));
                txv_cc_sobrepeso.setText("$"+Double.parseDouble(map.get("CCSobrePeso")));

                txv_costo_total.setText("$"+Double.parseDouble(map.get("CostoTotal")));


                map = new HashMap<String,String>();
                map = list.get(0);

                txv_dimensiones.setText("No aplica");

                adapter = new DetailQuotationAdapter(context,getActivity(),servicioList);
                lv_quotation.setAdapter(adapter);


                break;

            case "nacional_paquete":

                map = new HashMap<String,String>();
                map = list.get(1);

                imv_send_type.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.paquete));
                txv_send_type.setText("Paquete");

                txv_guia.setText("$"+Double.parseDouble(map.get("TarifaBase")));
                txv_cc_tarifa.setText("$" + Double.parseDouble(map.get("CCTarifaBase")));
                txv_cargo_extra.setText("$"+Double.parseDouble(map.get("CargosExtra")));

                txv_costo.setText("$" + Double.parseDouble(map.get("SobrePeso")));
                txv_cc_sobrepeso.setText("$"+Double.parseDouble(map.get("CCSobrePeso")));

                txv_costo_total.setText("$"+Double.parseDouble(map.get("CostoTotal")));


                map = new HashMap<String,String>();
                map = list.get(0);

                txv_dimensiones.setText(""+map.get("Alto")+"cm x "+map.get("Ancho")+"cm x "+map.get("Largo")+", "+map.get("Peso")+"kg");

                adapter = new DetailQuotationAdapter(context,getActivity(),servicioList);
                lv_quotation.setAdapter(adapter);


                break;

            case "internacional_sobre":


                break;

            case "internacional_paquete":


                break;

            case "internacional_sobre_eua_canada":


                break;

            case "internacional_paquete_eua_canada":


                break;

        }

            txv_origen.setText(""+map.get("CodigoPosOri")+", "+map.get("MunicipioOri")+", "+map.get("EstadoOri"));
            txv_destino.setText(""+map.get("CpDestino")+", "+map.get("Municipio")+", "+map.get("Estado"));

            String dias = "";

            if(map.get("Domingo").equalsIgnoreCase("x")){
                dias += "DO  ";
            }
            if(map.get("Lunes").equalsIgnoreCase("x")){
                dias += "LU  ";
            }
            if(map.get("Martes").equalsIgnoreCase("x")){
                dias += "MA  ";
            }
            if(map.get("Miercoles").equalsIgnoreCase("x")){
                dias += "MI  ";
            }
            if(map.get("Jueves").equalsIgnoreCase("x")){
                dias += "JU  ";
            }
            if(map.get("Viernes").equalsIgnoreCase("x")){
                dias += "VI  ";
            }
            if(map.get("Sabado").equalsIgnoreCase("x")){
                dias += "SA  ";
            }

            txv_dias_entrega.setText("" + dias);

            txv_frecuencia.setText("" + map.get("Frecuencia"));
            txv_ocurre_forzoso.setText("" + map.get("OcurreForzoso"));
            txv_costo_reexpedicion.setText("" + map.get("CostoReexpedicion"));



    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_call:
                Log.d(TAG, "Action Call");
                String phoneNumber = "018003782338";
                onClickTelefono(phoneNumber);
                break;

            case R.id.button_share:
                Log.d(TAG, "Action Share");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String sendText = "No.Guía: "+ txv_guia.getText()+". "
                        +"Código Rastreo: "+txv_cc_tarifa.getText()+". "
                        +"Origen: "+txv_origen.getText()+". "
                        +"Destino: "+txv_destino.getText()+". ";

                sendIntent.putExtra(Intent.EXTRA_SUBJECT, sendText);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

        }

    }

    public void onClickTelefono(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
