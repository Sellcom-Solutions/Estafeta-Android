package com.sellcom.apps.tracker_material.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.TextView;
import android.widget.ListView;


import com.sellcom.apps.tracker_material.Adapters.CPAListdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonathan.vazquez on 25/05/15.
 */


/**
 * Update by hugo.figueroa on 16/06/15.
 */
public class FragmentQuotation extends TrackerFragment implements View.OnClickListener{

    public static final String TAG = "FRAG_QUOTATION";


    private ToggleButton    tbtn_nat,
                            tbtn_inter;

    private CheckBox        cb_packet,
                            cb_package;


    private EditText        edt_zc_origin,
                            edt_zc_destination,
                            edt_weigth,
                            edt_high,
                            edt_long,
                            edt_width,
                            edt_dummy,
                            edt_city,
                            edt_colony;

    private ImageButton     btn_zc_ori_search,
                            btn_zc_ori_phonebook,
                            btn_zc_dest_search,
                            btn_zc_dest_phonebook;

    private Button          btn_quote;

    private LinearLayout    ll_for_package,
                            lin_nacional_cp,
                            lin_internacional_pais,
                            lin_capturar_datos,
                            lin_escoger_cp;

    private ListView        lv_dialog_colonia;

    private Context         context;

    private Button          btn_cancel,
                            btn_accept,
                            btn_search,
                            btn_cancel_cp;

    private Spinner         spn_state,spn_countrie;

    private Dialog          dialogCP;

    private TextView        tv_dialog_estado,
                            tv_dialog_ciudad,
                            tv_dialog_cp;

    String          ciudadString,
                            coloniaString,
                            estadoString;

    private ArrayList<Map<String,String>> colonias;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quotation,container,false);

        if(view != null){

            tbtn_nat            = (ToggleButton) view.findViewById(R.id.tbtn_nat);
            tbtn_inter          = (ToggleButton) view.findViewById(R.id.tbtn_inter);

            cb_packet           = (CheckBox) view.findViewById(R.id.cb_packet);
            cb_package          = (CheckBox) view.findViewById(R.id.cb_package);

            edt_zc_origin       = (EditText) view.findViewById(R.id.edt_zc_origin);
            edt_zc_destination  = (EditText) view.findViewById(R.id.edt_zc_destination);
            edt_weigth          = (EditText) view.findViewById(R.id.edt_weigth);
            edt_high            = (EditText) view.findViewById(R.id.edt_high);
            edt_long            = (EditText) view.findViewById(R.id.edt_long);
            edt_width           = (EditText) view.findViewById(R.id.edt_width);
            edt_dummy  = (EditText) view.findViewById(R.id.edt_dummy);

            btn_zc_ori_search       = (ImageButton) view.findViewById(R.id.btn_zc_ori_search);
            btn_zc_ori_phonebook    = (ImageButton) view.findViewById(R.id.btn_zc_ori_phonebook);
            btn_zc_dest_search      = (ImageButton) view.findViewById(R.id.btn_zc_dest_search);
            btn_zc_dest_phonebook   = (ImageButton) view.findViewById(R.id.btn_zc_dest_phonebook);

            btn_quote               = (Button) view.findViewById(R.id.btn_quote);

            ll_for_package      = (LinearLayout) view.findViewById(R.id.ll_for_package);
            lin_nacional_cp      = (LinearLayout) view.findViewById(R.id.lin_nacional_cp);
            lin_internacional_pais      = (LinearLayout) view.findViewById(R.id.lin_internacional_pais);

            spn_countrie         =(Spinner)view.findViewById(R.id.spn_countrie);
            setCountriesToSpinner(spn_countrie,context);

            tbtn_nat.setOnClickListener(this);
            tbtn_inter.setOnClickListener(this);

            tbtn_nat.setChecked(true);
            tbtn_inter.setChecked(false);

            cb_packet.setOnClickListener(this);
            cb_package.setOnClickListener(this);
            cb_packet.setChecked(true);
            cb_package.setChecked(false);

            btn_zc_ori_search.setOnClickListener(this);
            btn_zc_ori_phonebook.setOnClickListener(this);
            btn_zc_dest_search.setOnClickListener(this);
            btn_zc_dest_phonebook.setOnClickListener(this);
            btn_quote.setOnClickListener(this);

            ll_for_package.setVisibility(View.GONE);

            edt_dummy.requestFocus();

            dialogCP = new Dialog(getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth);
            dialogCP.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogCP.setContentView(R.layout.dialog_quotation_search_zc);
            dialogCP.setCancelable(false);

            spn_state           = (Spinner) dialogCP.findViewById(R.id.spn_state);
            tv_dialog_cp        = (TextView)dialogCP.findViewById(R.id.tv_dialog_cp);
            tv_dialog_estado    = (TextView)dialogCP.findViewById(R.id.tv_dialog_estado);
            tv_dialog_ciudad    = (TextView)dialogCP.findViewById(R.id.tv_dialog_ciudad);
            edt_city            = (EditText) dialogCP.findViewById(R.id.edt_city);
            edt_colony          = (EditText) dialogCP.findViewById(R.id.edt_colony);
            btn_accept          = (Button) dialogCP.findViewById(R.id.btn_accept);
            btn_cancel_cp       = (Button)dialogCP.findViewById(R.id.btn_cancel_cp);
            btn_search          = (Button) dialogCP.findViewById(R.id.btn_search);
            btn_cancel          = (Button) dialogCP.findViewById(R.id.btn_cancel);
            lin_capturar_datos  = (LinearLayout)dialogCP.findViewById(R.id.lin_capturar_datos);
            lin_escoger_cp      = (LinearLayout)dialogCP.findViewById(R.id.lin_escoger_cp);
            lv_dialog_colonia   = (ListView)dialogCP.findViewById(R.id.lv_dialog_colonia);

            btn_cancel_cp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogCP.dismiss();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "CANCEL");
                    dialogCP.dismiss();
                }
            });

            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "ACCEPT");

                    searchZC();
                }
            });

        }

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tbtn_nat:
                if (((ToggleButton) v).isChecked()) {
                    tbtn_inter.setChecked(false);
                    lin_internacional_pais.setVisibility(View.GONE);
                    lin_nacional_cp.setVisibility(View.VISIBLE);

                }
                else{
                    tbtn_nat.setChecked(true);
                }
                break;

            case R.id.tbtn_inter:
                if (((ToggleButton) v).isChecked()) {
                    tbtn_nat.setChecked(false);
                    lin_nacional_cp.setVisibility(View.GONE);
                    lin_internacional_pais.setVisibility(View.VISIBLE);
                }
                else{
                    tbtn_inter.setChecked(true);
                }
                break;

            case R.id.cb_packet:
                if (((CheckBox) v).isChecked()) {
                    cb_package.setChecked(false);
                    ll_for_package.setVisibility(View.GONE);
                }
                else{
                    cb_packet.setChecked(true);
                    ll_for_package.setVisibility(View.GONE);
                }
                break;

            case R.id.cb_package:
                if (((CheckBox) v).isChecked()) {
                    cb_packet.setChecked(false);
                    ll_for_package.setVisibility(View.VISIBLE);
                }
                else{
                    cb_package.setChecked(true);
                    ll_for_package.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btn_zc_ori_search:
                searchZipCode();
                break;

            case R.id.btn_zc_ori_phonebook:
                openPhonebook();
                break;

            case R.id.btn_zc_dest_search:
                searchZipCode();
                break;

            case R.id.btn_zc_dest_phonebook:
                openPhonebook();
                break;

            case R.id.btn_quote:
                requestQuote();
                break;
        }
    }

    private void searchZipCode(){
        dialogCP();

    }

    private void openPhonebook(){


    }

    private void requestQuote(){

    }


    public void dialogCP(){

        if(!dialogCP.isShowing()) {

            setStatesToSpinner(spn_state, context);
            lin_escoger_cp.setVisibility(View.GONE);
            lin_capturar_datos.setVisibility(View.VISIBLE);

        dialogCP.show();
        }

    }

    private void searchZC() {

        ciudadString = edt_city.getText().toString();

        if (spn_state.getSelectedItemPosition() == 0) {

            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_estado), 3000);
        } else if (ciudadString == null || ciudadString.equals("")) {
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_ciudad), 3000);
        } else {
            ciudadString = edt_city.getText().toString();
            coloniaString = edt_colony.getText().toString();
            estadoString = spn_state.getSelectedItem().toString();


            if (estadoString.equalsIgnoreCase("Baja California Norte")) {
                estadoString = "BAJA CALIFORNIA";
            } else if (estadoString.equalsIgnoreCase("Estado de México")) {
                estadoString = "EDO. DE MEXICO";
            } else if (estadoString.equalsIgnoreCase("México, D.F.")) {
                estadoString = "DISTRITO FEDERAL";
            }


            Map<String, String> requestData = new HashMap<>();
            requestData = new HashMap<>();
            requestData.put("pais", "MEXICO");
            requestData.put("estado", estadoString);
            requestData.put("ciudad", ciudadString);
            requestData.put("localidad", coloniaString);

            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando), 0);
            RequestManager.sharedInstance().setListener(this);
            RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE, requestData);

        }
    }


    public void showCP(ArrayList<Map<String,String>> resp){

        try {

            colonias = resp;
            Map<String,String> aux = new HashMap<>();

            aux = colonias.get(0);

            CPAListdapter adapter = new CPAListdapter(getActivity(),colonias,"1");
            lv_dialog_colonia.setAdapter(adapter);

            tv_dialog_ciudad.setText(ciudadString);

            tv_dialog_estado.setText(estadoString);

        } catch (Exception e) {
            e.printStackTrace();
        }



        lin_capturar_datos.setVisibility(View.GONE);
        lin_escoger_cp.setVisibility(View.VISIBLE);

        try {
            DialogManager.sharedInstance().dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void decodeResponse(String response){


        if(response != null && response.length() > 0){
            Log.v("FragmentCodigoPostal", response);
            ArrayList<Map<String,String>> resp = new ArrayList<>();
            resp = RequestManager.sharedInstance().getResponseArray();
            if(resp != null && resp.size()>0 ) {
                showCP(resp);
            }else {
                try {
                    DialogManager.sharedInstance().dismissDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio),2000);
            }

        }else{
            Log.v("FragmentCodigoPostal", "El servidor devolvio null");
        }

    }



}
