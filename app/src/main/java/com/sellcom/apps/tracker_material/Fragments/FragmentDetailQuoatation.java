package com.sellcom.apps.tracker_material.Fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sellcom.apps.tracker_material.Adapters.DetailQuotationAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                            txv_costo_reexpedicion,
                            txv_guia_internacional,
                            txv_cc_tarifa_internacional,
                            txv_costo_total_internacional,
                            txv_terminos,
                            footer,
                            txv_articulos;

    private ImageView       imv_send_type;

    private ListView        lv_quotation;

    private LinearLayout    lin_dimensiones,
                            lin_detail_international,
                            lin_detail_national,
                            lin_terminos_internacional;

    private Button          btn_buy;

    private List<Map<String,String>> list;

    private ArrayList<String>  servicioList;

    private Map<String,String> map;

    private String type;
    private DecimalFormat   decimales;
    private double          numero;

    private String cpo,cpd;

    private static  int posServicio = 0;

    DetailQuotationAdapter adapter;


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

        list    = (List<Map<String,String>>) getArguments().getSerializable("auxResp");


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
        txv_guia_internacional  = (TextView)view.findViewById(R.id.txv_guia_internacional);
        txv_cc_tarifa_internacional  = (TextView)view.findViewById(R.id.txv_cc_tarifa_internacional);
        txv_costo_total_internacional  = (TextView)view.findViewById(R.id.txv_costo_total_internacional);
        txv_terminos            = (TextView)view.findViewById(R.id.txv_terminos);
        txv_articulos           = (TextView)view.findViewById(R.id.txv_articulos);

        txv_articulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.estafeta.com"));
                getActivity().startActivity(intent);
            }
        });

        footer      = (TextView)view.findViewById(R.id.footer);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String currentYear = formatter.format(new Date());
        footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));

        imv_send_type           = (ImageView)view.findViewById(R.id.imv_send_type);

        lin_dimensiones         = (LinearLayout)view.findViewById(R.id.lin_dimensiones);

        lin_detail_international = (LinearLayout)view.findViewById(R.id.lin_detail_international);
        lin_detail_national     = (LinearLayout)view.findViewById(R.id.lin_detail_national);
        lin_terminos_internacional = (LinearLayout)view.findViewById(R.id.lin_terminos_internacional);

        btn_buy = (Button) view.findViewById(R.id.btn_buy);

        final FloatingActionButton btn_call = (FloatingActionButton) view.findViewById(R.id.button_call);
        final FloatingActionButton btn_share = (FloatingActionButton) view.findViewById(R.id.button_share);

        btn_call.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_buy.setOnClickListener(this);

        lv_quotation            = (ListView)view.findViewById(R.id.lv_quotation);





        printInformation(type);

        return view;
    }

    public void checkListNational(final List<Map<String,String>> list){

        lin_detail_national.setVisibility(View.VISIBLE);
        lin_detail_international.setVisibility(View.GONE);
        lin_terminos_internacional.setVisibility(View.GONE);

        servicioList = new ArrayList<String>();

        for (int i = 1; i<list.size(); i++){
            if(list.get(i).get("AplicaCotizacion") != null &&
                    list.get(i).get("AplicaCotizacion").equalsIgnoreCase("Si") &&
                    list.get(i).get("AplicaServicio").equalsIgnoreCase("Si") &&
                    !list.get(i).get("TarifaBase").equalsIgnoreCase("0")){

                servicioList.add(list.get(i).get("DescripcionServicio"));

            }else{
                Log.d(TAG, "Tamaño lista: " + list.size());
                Log.d(TAG,"i: "+list.size());
                list.remove(i);
                i--;

            }
        }

        adapter = new DetailQuotationAdapter(context,getActivity(),servicioList);
        lv_quotation.setAdapter(adapter);

        lv_quotation.getSelectedItemPosition();
        Log.d(TAG, "LV_QUOTATION: " +  lv_quotation.getSelectedItemPosition());

        lv_quotation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Utilities.position = position;

                adapter.notifyDataSetChanged();

                posServicio = position;
                TextView selection = (TextView) view.findViewById(R.id.txv_selection);
                TextView description = (TextView) view.findViewById(R.id.txv_descripcion_servicio);


                Log.d(TAG, " --- " + view);


                lv_quotation.setSelection(position);

                map = new HashMap<String, String>();
                map = list.get((position + 1));

                decimales = new DecimalFormat("0.00");

                numero = Double.parseDouble(map.get("TarifaBase"));
                txv_guia.setText(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("CCTarifaBase"));
                txv_cc_tarifa.setText(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("CargosExtra"));
                txv_cargo_extra.setText(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("SobrePeso"));
                txv_costo.setText(Utilities.setReceiptMoneyNumberFormat(numero, 2));


                numero = Double.parseDouble(map.get("CCSobrePeso"));
                txv_cc_sobrepeso.setText(Utilities.setReceiptMoneyNumberFormat(numero, 2));


                numero = Double.parseDouble(map.get("CostoTotal"));


                txv_costo_total.setText(Utilities.setReceiptMoneyNumberFormat(numero, 2));


            }
        });

    }

    public void checkListInternational(final List<Map<String,String>> list){

        lin_detail_national.setVisibility(View.GONE);
        lin_detail_international.setVisibility(View.VISIBLE);
        lin_terminos_internacional.setVisibility(View.VISIBLE);

        servicioList = new ArrayList<String>();

        for (int i = 0; i<list.size(); i++){

            servicioList.add(list.get(i).get("DescripcionServicio"));

        }

        adapter = new DetailQuotationAdapter(context,getActivity(),servicioList);
        lv_quotation.setAdapter(adapter);

        lv_quotation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Utilities.position = position;

                adapter.notifyDataSetChanged();

                posServicio = position;
                TextView selection = (TextView) view.findViewById(R.id.txv_selection);
                TextView description = (TextView) view.findViewById(R.id.txv_descripcion_servicio);


                Log.d(TAG, " --- " + view);


                lv_quotation.setSelection(position);

                map = new HashMap<String, String>();
                map = list.get((position));

                txv_guia_internacional.setText("USD ");
                txv_cc_tarifa_internacional.setText("USD ");
                txv_costo_total_internacional.setText("USD ");


                decimales = new DecimalFormat("0.00");
                numero = Double.parseDouble(map.get("PrecioPaquete"));
                txv_guia_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("PrecioCombustible"));
                txv_cc_tarifa_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("PrecioCotizado"));
                txv_costo_total_internacional.append("" + Utilities.setReceiptMoneyNumberFormat(numero, 2));

                if(position == 0){
                    txv_terminos.setText(getActivity().getString(R.string.terminos_internacional_3));
                }else{
                    txv_terminos.setText(getActivity().getString(R.string.terminos_internacional_2));
                }

            }
        });

    }

    public void printInformation(String type){

        switch (type){

            case "nacional_sobre":
                checkListNational(list);


                map = new HashMap<String,String>();
                map = list.get(1);

                imv_send_type.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.sobre));
                txv_send_type.setText("Sobre");
                lin_dimensiones.setVisibility(View.GONE);

                decimales = new DecimalFormat("0.00");


                numero = Double.parseDouble(map.get("TarifaBase"));
                txv_guia.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("CCTarifaBase"));
                txv_cc_tarifa.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("CargosExtra"));
                txv_cargo_extra.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));


                numero = Double.parseDouble(map.get("SobrePeso"));
                txv_costo.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("CCSobrePeso"));
                txv_cc_sobrepeso.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("CostoTotal"));
                txv_costo_total.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                txv_terminos.setText(getActivity().getString(R.string.terminos_internacional_3));


                map = new HashMap<String,String>();
                map = list.get(0);

                addExtraData();

                break;

            case "nacional_paquete":
                checkListNational(list);
                map = new HashMap<String,String>();
                map = list.get(1);

                imv_send_type.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.paquete));
                txv_send_type.setText("Paquete");
                lin_dimensiones.setVisibility(View.VISIBLE);


                decimales = new DecimalFormat("0.00");


                numero = Double.parseDouble(map.get("TarifaBase"));
                txv_guia.setText(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("CCTarifaBase"));
                txv_cc_tarifa.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("CargosExtra"));
                txv_cargo_extra.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));


                numero = Double.parseDouble(map.get("SobrePeso"));

                txv_costo.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("CCSobrePeso"));
                txv_cc_sobrepeso.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("CostoTotal"));
                txv_costo_total.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                txv_terminos.setText(getActivity().getString(R.string.terminos_internacional_3));

                map = new HashMap<String,String>();
                map = list.get(0);

                txv_dimensiones.setText("" + map.get("Alto") + "cm x " + map.get("Ancho") + "cm x " + map.get("Largo") + "cm, " + map.get("Peso") + "kg");

                addExtraData();

                break;

            case "internacional_sobre":
                checkListInternational(list);

                map = new HashMap<String,String>();
                map = list.get(0);

                imv_send_type.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.sobre));
                txv_send_type.setText("Sobre");
                lin_dimensiones.setVisibility(View.GONE);

                decimales = new DecimalFormat("0.00");
                numero = Double.parseDouble(map.get("PrecioPaquete"));
                txv_guia_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("PrecioCombustible"));
                txv_cc_tarifa_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("PrecioCotizado"));
                txv_costo_total_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                txv_origen.setText("México");
                txv_destino.setText("" + getArguments().getString("destino"));

                txv_terminos.setText(getActivity().getString(R.string.terminos_internacional_3));

                break;

            case "internacional_paquete":

                checkListInternational(list);

                map = new HashMap<String,String>();
                map = list.get(0);

                imv_send_type.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.paquete));
                txv_send_type.setText("Paquete");
                lin_dimensiones.setVisibility(View.VISIBLE);

                decimales = new DecimalFormat("0.00");
                numero = Double.parseDouble(map.get("PrecioPaquete"));
                txv_guia_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("PrecioCombustible"));
                txv_cc_tarifa_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("PrecioCotizado"));
                txv_costo_total_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                txv_terminos.setText(getActivity().getString(R.string.terminos_internacional_3));

                txv_origen.setText("México");
                txv_destino.setText(""+getArguments().getString("destino"));

                txv_dimensiones.setText("" + getArguments().getString("alto") + "cm x " + getArguments().getString("ancho") + "cm x " + getArguments().getString("largo") + "cm, " + getArguments().getString("peso") + "kg");

                break;

            case "internacional_sobre_eua_canada":

                checkListInternational(list);

                map = new HashMap<String,String>();
                map = list.get(0);

                imv_send_type.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.sobre));
                txv_send_type.setText("Sobre");
                lin_dimensiones.setVisibility(View.GONE);

                decimales = new DecimalFormat("0.00");
                numero = Double.parseDouble(map.get("PrecioPaquete"));
                txv_guia_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("PrecioCombustible"));
                txv_cc_tarifa_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("PrecioCotizado"));
                txv_costo_total_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                txv_origen.setText("México");
                txv_destino.append(""+getArguments().getString("destino"));

                txv_terminos.setText(getActivity().getString(R.string.terminos_internacional_3));

                break;

            case "internacional_paquete_eua_canada":

                checkListInternational(list);

                map = new HashMap<String,String>();
                map = list.get(0);

                imv_send_type.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.paquete));
                txv_send_type.setText("Paquete");
                lin_dimensiones.setVisibility(View.VISIBLE);

                decimales = new DecimalFormat("0.00");
                numero = Double.parseDouble(map.get("PrecioPaquete"));
                txv_guia_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("PrecioCombustible"));
                txv_cc_tarifa_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                numero = Double.parseDouble(map.get("PrecioCotizado"));
                txv_costo_total_internacional.append(Utilities.setReceiptMoneyNumberFormat(numero, 2));

                txv_terminos.setText(getActivity().getString(R.string.terminos_internacional_3));

                txv_origen.setText("México");
                txv_destino.setText(""+getArguments().getString("destino"));

                txv_dimensiones.setText("" + getArguments().getString("alto") + "cm x " + getArguments().getString("ancho") + "cm x " + getArguments().getString("largo") + "cm, " + getArguments().getString("peso") + "kg");

                break;

        }


        DialogManager.sharedInstance().dismissDialog();

    }


    public void addExtraData(){

            txv_origen.setText(""+map.get("CodigoPosOri")+", "+map.get("MunicipioOri")+", "+map.get("EstadoOri"));
            cpo = map.get("CodigoPosOri");
            cpd = map.get("CpDestino");
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
                if (lin_dimensiones.getVisibility() == View.GONE) {
                    String sendText = "Servicio: " + servicioList.get(Utilities.position) + ". "
                            + "Tipo: " + txv_send_type.getText() + ". "
                            + "Origen: " + txv_origen.getText() + ". "
                            + "Destino: " + txv_destino.getText() + ". "
                            + "Costo total: " + txv_costo_total.getText() + ". "
                            + "Tarifa:" + txv_guia.getText() + ". "
                            + "Cargo extra: " + txv_cargo_extra.getText() + ". "
                            + "CC: " + txv_cc_tarifa.getText() + ". "
                            + "Costo sobrepeso: " + txv_costo.getText() + ". "
                            + "CC sobrepeso: " + txv_cc_sobrepeso.getText() + ". Precios sujetos a cambio sin previo aviso. Los precios no incluyen IVA.";


                    sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
                    sendIntent.setType("text/plain");
                }else{
                    String sendText = "Servicio: " + servicioList.get(Utilities.position) + ". "
                            + "Tipo: " + txv_send_type.getText() + ". "
                            + "Origen: " + txv_origen.getText() + ". "
                            + "Destino: " + txv_destino.getText() + ". "
                            + "Costo total: " + txv_costo_total.getText() + ". "
                            + "Tarifa:" + txv_guia.getText() + ". "
                            + "Cargo extra: " + txv_cargo_extra.getText() + ". "
                            + "CC: " + txv_cc_tarifa.getText() + ". "
                            + "Costo sobrepeso: " + txv_costo.getText() + ". "
                            + "CC sobrepeso: " + txv_cc_sobrepeso.getText() + ". "
                            +"Dimensiones: "+txv_dimensiones.getText()+". Precios sujetos a cambio sin previo aviso. Los precios no incluyen IVA.";


                    sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
                    sendIntent.setType("text/plain");

                }
                startActivity(sendIntent);
                break;

            case R.id.btn_buy:

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                TrackerFragment fragment = new FragmentQuotationBuy();
                Bundle b = new Bundle();
                b.putString(FragmentQuotationBuy.EXTRAS.CP_ORIGEN.toString(), cpo);
                b.putString(FragmentQuotationBuy.EXTRAS.CP_DESTINO.toString(), cpd);
                b.putString(FragmentQuotationBuy.EXTRAS.COSTO.toString(),txv_costo_total.getText().toString());
                fragment.setArguments(b);
                fragment.addFragmentToStack(getActivity());
                fragmentTransaction.replace(R.id.container, fragment, "");
                fragmentTransaction.commit();
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
