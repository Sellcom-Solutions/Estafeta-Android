package com.sellcom.apps.tracker_material.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.TextView;
import android.widget.ListView;


import com.sellcom.apps.tracker_material.Adapters.CPAListdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Countries;

/**
 * Created by jonathan.vazquez on 25/05/15.
 */


/**
 * Update by hugo.figueroa on 16/06/15.
 */
public class FragmentQuotation extends TrackerFragment implements View.OnClickListener, EditText.OnEditorActionListener{

    public static final String TAG = "FRAG_QUOTATION";
    protected static final int REQ_GET_CONTACT_ORIGIN = 0;
    protected static final int REQ_GET_CONTACT_DESTINATION = 1;

    private TrackerFragment fragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

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
                            lin_escoger_cp,
                            lin_item_cp;

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

    String                  ciudadString,
                            coloniaString,
                            estadoString,
                            cp_obtenido;

    private boolean         cpOrigen = true;

    private static int             positionCP = 0;

    private String          origen = "",
                            destino = "",
                            peso = "",
                            alto = "",
                            largo = "",
                            ancho = "",
                            typeSend = "";

    private int             contEUA_Canada = 0;

    private Map<String,String> pais;

    private Bundle          bundle;

    private ArrayList<Map<String,String>> colonias,respCotizador,auxResp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        fragmentManager = getActivity().getSupportFragmentManager();

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
            edt_width.setOnEditorActionListener(this);
            edt_dummy  = (EditText) view.findViewById(R.id.edt_dummy);
/*
            edt_weigth.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if(event.getAction()==KeyEvent.ACTION_DOWN ) {

                        if (Integer.parseInt(edt_weigth.getText().toString()) > 70) {
                            return false;
                        } else {
                            return true;
                        }
                    }

                    return false;
                }
            });/*
            edt_weigth.setOnKeyListener(new View.OnKeyListener() {


                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if(event.getAction()==KeyEvent.ACTION_DOWN ) {

                        if (Integer.parseInt(edt_weigth.getText().toString()) > 70) {
                            return false;
                        } else {
                            return true;
                        }
                    }

                    return false;
                }
            });*/



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

            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogCP.dismiss();
                    if(cpOrigen) {
                        edt_zc_origin.setText("" + cp_obtenido);
                    }else{
                        edt_zc_destination.setText("" + cp_obtenido);
                    }
                    //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();
                }
            });



        }

        return view;
    }


    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

        if (actionId == EditorInfo.IME_ACTION_DONE) {
            btn_quote.performClick();
            Utilities.hideKeyboard(context,edt_width);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);

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
                cpOrigen = true;
                positionCP = 0;
                searchZipCode();
                break;

            case R.id.btn_zc_ori_phonebook:

                    startActivityForResult(intent, REQ_GET_CONTACT_ORIGIN);

                break;

            case R.id.btn_zc_dest_search:
                cpOrigen = false;
                positionCP = 0;
                searchZipCode();
                break;

            case R.id.btn_zc_dest_phonebook:

                    startActivityForResult(intent, REQ_GET_CONTACT_DESTINATION);

                break;

            case R.id.btn_quote:
                //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();
                auxResp = new ArrayList<Map<String,String>>();
                requestQuote();
                break;

        }
    }

    private void searchZipCode(){
        dialogCP();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            //Código para obtener el cp de los contactos.
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // The Contacts API is about the most complex to use.
                    // First we have to retrieve the Contact, since we only get its
                    // URI from the Intent
                    Uri resultUri = data.getData(); // e.g.,
                    // content://contacts/people/123
                    Cursor cont = getActivity().getContentResolver().query(resultUri, null, null,
                            null, null);
                    if (!cont.moveToNext()) { // expect 001 row(s)
                        return;
                    }
                    int columnIndexForId = cont
                            .getColumnIndex(ContactsContract.Contacts._ID);
                    String contactId = cont.getString(columnIndexForId);
                    cont.close();
                    String cp = getCP(contactId);

                    try {
                        if (cp != null) { // No es null
                            if (cp.trim().length() > 0) { // Que longitud tiene?
                                Double.parseDouble(cp); // Es numero?
                                if (cp.trim().length() == 5) { // es igual a cinco?
                                    if (requestCode == REQ_GET_CONTACT_ORIGIN) {
                                        edt_zc_origin.setText("" + cp);
                                    }else if(requestCode == REQ_GET_CONTACT_DESTINATION) {
                                        edt_zc_destination.setText("" + cp);
                                    }
                                } else { // No es igual a cinco
                                    throw new NumberFormatException();
                                }
                            } else { // Su longitud es cero
                                throw new NumberFormatException();
                            }
                        } else { // es null ... :(
                            throw new NumberFormatException();

                        }
                    } catch (NumberFormatException e) {
                        //com.gc.materialdesign.widgets.Dialog dialog = new com.gc.materialdesign.widgets.Dialog(context,"Error",getResources().getString(R.string.noPostalCode));
                        if (requestCode == REQ_GET_CONTACT_ORIGIN) {
                            edt_zc_origin.setText("");
                        }else if(requestCode == REQ_GET_CONTACT_DESTINATION) {
                            edt_zc_destination.setText("");
                        }
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noPostalCode), 3000);
                    }
                    break;
                case Activity.RESULT_CANCELED:

                default:
            }


    }

    private String getCP(String contactId) {
        String postalCode = null;
        String where = ContactsContract.Data.CONTACT_ID + " =  ? "
                + " AND "+ ContactsContract.Data.MIMETYPE +" = ? ";

        String[] projection = new String[] { ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE };
        String[] params = new String[] {
                contactId,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
        Cursor cursor = getActivity().getContentResolver().query(
                ContactsContract.Data.CONTENT_URI, projection, where, params,
                null);
        if (cursor.moveToFirst()) {
            postalCode = cursor.getString(0);
        }
        cursor.close();
        return postalCode;
    }

    private void requestQuote(){

        if(!isNetworkAvailable()){
            Toast.makeText(context, "Debe tener acceso a Internet", Toast.LENGTH_SHORT).show();
        }else{

            if(ll_for_package.getVisibility() == View.VISIBLE) {

                    if (lin_nacional_cp.getVisibility() == View.VISIBLE) {
                        if (edt_zc_origin.getText().toString().equals("") || edt_zc_origin.getText().toString().length() != 5) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noOrigin), 3000);
                            return;
                        } else if (edt_zc_destination.getText().toString().equals("") || edt_zc_destination.getText().toString().length() != 5) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noDestination), 3000);
                            return;
                        } else if (edt_weigth.getText().toString().equals("")) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noWeigth), 3000);
                            return;
                        } else if (edt_high.getText().toString().equals("")) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noHigh), 3000);
                            return;
                        } else if (edt_long.getText().toString().equals("")) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noLong), 3000);
                            return;
                        } else if (edt_width.getText().toString().equals("")) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noWidth), 3000);
                            return;
                        }else{



                            if(Integer.parseInt(edt_weigth.getText().toString()) > 70){
                                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "El peso no debe superar los 70kg.", 3000);
                            }else if(Integer.parseInt(edt_high.getText().toString()) > 70){
                                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "La altura no debe superar los 70cm.", 3000);
                            }else if(Integer.parseInt(edt_long.getText().toString()) > 70){
                                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "El largo no debe superar los 70cm.", 3000);
                            }else if(Integer.parseInt(edt_width.getText().toString()) > 70){
                                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "El ancho no debe superar los 70cm.", 3000);
                            }else{

                                int pesoVolumetrico = (Integer.parseInt(edt_high.getText().toString()) * Integer.parseInt(edt_long.getText().toString()) * Integer.parseInt(edt_width.getText().toString()))/5000;

                                if(pesoVolumetrico > 70){
                                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "El peso volumetrico no debe ser mayor que 70, verifique los datos ingresados.", 3000);
                                }else{
                                    origen  = edt_zc_origin.getText().toString();
                                    destino = edt_zc_destination.getText().toString();
                                    peso    = edt_weigth.getText().toString();
                                    alto    = edt_high.getText().toString();
                                    largo   = edt_long.getText().toString();
                                    ancho   = edt_width.getText().toString();
                                    cotizar("nacional_paquete");
                                }


                            }




                        }
                    } else if (lin_internacional_pais.getVisibility() == View.VISIBLE) {
                        if(spn_countrie.getSelectedItemPosition() == 0){
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noCountry), 3000);
                            return;
                        }else if (edt_weigth.getText().toString().equals("")) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noWeigth), 3000);
                            return;
                        } else if (edt_high.getText().toString().equals("")) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noHigh), 3000);
                            return;
                        } else if (edt_long.getText().toString().equals("")) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noLong), 3000);
                            return;
                        } else if (edt_width.getText().toString().equals("")) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noWidth), 3000);
                            return;
                        }else{


                            if(Integer.parseInt(edt_weigth.getText().toString()) > 70){
                                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "El peso no debe superar los 70kg.", 3000);
                            }else if(Integer.parseInt(edt_high.getText().toString()) > 70){
                                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "La altura no debe superar los 70cm.", 3000);
                            }else if(Integer.parseInt(edt_long.getText().toString()) > 70){
                                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "El largo no debe superar los 70cm.", 3000);
                            }else if(Integer.parseInt(edt_width.getText().toString()) > 70){
                                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "El ancho no debe superar los 70cm.", 3000);
                            }else{

                                int pesoVolumetrico = (Integer.parseInt(edt_high.getText().toString()) * Integer.parseInt(edt_long.getText().toString()) * Integer.parseInt(edt_width.getText().toString()))/5000;

                                if(pesoVolumetrico > 70){
                                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "El peso volumetrico no debe ser mayor que 70, verifique los datos ingresados.", 3000);
                                }else{

                                    int numCountrie = spn_countrie.getSelectedItemPosition();
                                    peso    = edt_weigth.getText().toString();
                                    alto    = edt_high.getText().toString();
                                    largo   = edt_long.getText().toString();
                                    ancho   = edt_width.getText().toString();

                                    if(numCountrie == 63){
                                        contEUA_Canada = 0;
                                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, "Cotizando...", 0);
                                        cotizar("internacional_paquete_eua_canada");

                                        //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();
                                    }else{
                                        contEUA_Canada = 0;
                                        cotizar("internacional_paquete");
                                    }

                                }


                            }





                        }
                    }
            } else {

                    if (lin_nacional_cp.getVisibility() == View.VISIBLE) {
                        if (edt_zc_origin.getText().toString().equals("") || edt_zc_origin.getText().toString().length() != 5) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noOrigin), 3000);
                            return;
                        } else if (edt_zc_destination.getText().toString().equals("") || edt_zc_destination.getText().toString().length() != 5) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noDestination), 3000);
                            return;
                        } else {

                            origen = edt_zc_origin.getText().toString();
                            destino = edt_zc_destination.getText().toString();
                            cotizar("nacional_sobre");
                            //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();

                        }
                    } else if (lin_internacional_pais.getVisibility() == View.VISIBLE) {
                        if(spn_countrie.getSelectedItemPosition() == 0){
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noCountry), 3000);
                            return;
                        }else{
                            int numCountrie = spn_countrie.getSelectedItemPosition();

                            if(numCountrie == 39 || numCountrie == 63){
                                contEUA_Canada = 0;
                                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, "Cotizando...", 0);
                                cotizar("internacional_sobre_eua_canada");

                                //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();
                            }else{
                                contEUA_Canada = 0;
                                cotizar("internacional_sobre");
                            }
                        }
                    }

                }

            //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        cb_packet.setChecked(true);
        cb_package.setChecked(false);
        tbtn_nat.setChecked(true);
        tbtn_inter.setChecked(false);
    }

    private void cotizar(String type){


        Map<String, String> requestData = new HashMap<>();


        switch (type){
            case "nacional_sobre":
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, "Cotizando...", 0);

                requestData.put("Peso","0.0");
                requestData.put("Alto","0.0");
                requestData.put("Largo","0.0");
                requestData.put("Ancho","0.0");

                requestData.put("EsPaquete","false");
                requestData.put("datosOrigen",origen);
                requestData.put("datosDestino",destino);

                typeSend = "nacional";

                RequestManager.sharedInstance().setListener(this);
                RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_NATIONAL_DELIVERY, requestData);

                break;

            case "nacional_paquete":
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, "Cotizando...", 0);

                requestData.put("Peso",peso);
                requestData.put("Alto",alto);
                requestData.put("Largo",largo);
                requestData.put("Ancho",ancho);

                requestData.put("EsPaquete","true");
                requestData.put("datosOrigen",origen);
                requestData.put("datosDestino",destino);

                typeSend = "nacional";

                RequestManager.sharedInstance().setListener(this);
                RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_NATIONAL_DELIVERY, requestData);

                break;

            case "internacional_sobre":
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, "Cotizando...", 0);


                requestData.put("peso","1");
                requestData.put("alto","0.0");
                requestData.put("largo","0.0");
                requestData.put("ancho","0.0");

                requestData.put("envio","0");
                requestData.put("servicio", "1");

                pais = Countries.getIdPaisById(context,""+spn_countrie.getSelectedItemPosition());

                requestData.put("paisDestino","" + pais.get("idpais"));

                typeSend = "internacional_sobre";

                //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();

                RequestManager.sharedInstance().setListener(this);
                RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_INTERNATIONAL_DELIVERY, requestData);

                break;


            case "internacional_sobre_eua_canada":


                requestData.put("peso","1");
                requestData.put("alto","0.0");
                requestData.put("largo","0.0");
                requestData.put("ancho","0.0");

                requestData.put("envio","0");
                if(contEUA_Canada == 0) {//Primer Envio
                    requestData.put("servicio", "1");
                }else{//Segundo Envio
                    requestData.put("servicio", "0");
                }

                pais = Countries.getIdPaisById(context,""+spn_countrie.getSelectedItemPosition());

                requestData.put("paisDestino","" + pais.get("idpais"));

                typeSend = "internacional_sobre_eua_canada";

                //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();

                RequestManager.sharedInstance().setListener(this);
                RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_INTERNATIONAL_DELIVERY, requestData);

                break;

            case "internacional_paquete":
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, "Cotizando...", 0);

                requestData.put("peso", peso);

                requestData.put("alto",alto);
                requestData.put("largo",largo);
                requestData.put("ancho",ancho);

                requestData.put("envio","1");
                requestData.put("servicio", "1");
                pais = Countries.getIdPaisById(context,""+spn_countrie.getSelectedItemPosition());

                requestData.put("paisDestino","" + pais.get("idpais"));

                typeSend = "internacional_paquete";

                Log.v(TAG,"Peso: " + requestData.get("peso") + ", Alto: "+ requestData.get("alto")+", Largo: "+requestData.get("largo")+", Ancho: "+requestData.get("ancho")+", Envio: "+requestData.get("envio")+", Servicio: "+requestData.get("servicio")+", PaisDestino: "+requestData.get("paisDestino"));

                //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();

                RequestManager.sharedInstance().setListener(this);
                RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_INTERNATIONAL_DELIVERY, requestData);

                break;

            case "internacional_paquete_eua_canada":


                requestData.put("peso", peso);

                requestData.put("alto",alto);
                requestData.put("largo",largo);
                requestData.put("ancho",ancho);

                requestData.put("envio","1");
                if(contEUA_Canada == 0) {//Primer Envio
                    requestData.put("servicio", "1");
                }else{//Segundo Envio
                    requestData.put("servicio", "0");
                }

                pais = Countries.getIdPaisById(context,""+spn_countrie.getSelectedItemPosition());

                requestData.put("paisDestino","" + pais.get("idpais"));

                typeSend = "internacional_paquete_eua_canada";

                Log.v(TAG,"Peso: " + requestData.get("peso") + ", Alto: "+ requestData.get("alto")+", Largo: "+requestData.get("largo")+", Ancho: "+requestData.get("ancho")+", Envio: "+requestData.get("envio")+", Servicio: "+requestData.get("servicio")+", PaisDestino: "+requestData.get("paisDestino"));

                //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();

                RequestManager.sharedInstance().setListener(this);
                RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_INTERNATIONAL_DELIVERY, requestData);

                break;

        }


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

            typeSend = "codigo_postal";

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

            final CPAListdapter adapter = new CPAListdapter(getActivity(),getActivity().getApplicationContext(),colonias,"cotizador");
            lv_dialog_colonia.setAdapter(adapter);

            lv_dialog_colonia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    positionCP = position;
                    view.setSelected(true);

                    adapter.setSelectionState(true, view);
                    adapter.setLastSelectedItemPosition(position);
                    cp_obtenido = lv_dialog_colonia.getItemAtPosition(positionCP).toString();

                }
            });

            cp_obtenido = lv_dialog_colonia.getItemAtPosition(positionCP).toString();

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
        Log.d(TAG,""+response);

        bundle = new Bundle();
        String currentTag = "";

        respCotizador = RequestManager.sharedInstance().getResponseArray();

        if(respCotizador != null) {

            if (response != null && response.length() > 0) {

                if (typeSend.equals("nacional")) {


                    Map<String, String> map = respCotizador.get(0);


                    String error = map.get("Error");

                    if (error.equals("E008")) {
                        DialogManager.sharedInstance().dismissDialog();
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noCpOrigin), 3000);
                    } else if (error.equals("E009")) {
                        DialogManager.sharedInstance().dismissDialog();
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noCpDestination), 3000);
                    } else {


                        String EsPaquete = map.get("EsPaquete");

                        if (EsPaquete.equals("false")) {

                            bundle.putString("type", "nacional_sobre");


                        } else if (EsPaquete.equals("true")) {

                            bundle.putString("type", "nacional_paquete");


                        }

                        auxResp = respCotizador;

                        bundle.putSerializable("auxResp", auxResp);
                        fragment = new FragmentDetailQuoatation();
                        fragment.addFragmentToStack(getActivity());
                        fragment.setArguments(bundle);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack(null);

                        new AsynTask().execute();

                    }
                }else if(typeSend.equals("internacional_sobre")){

                    respCotizador.get(0).put("DescripcionServicio", "Global Express");

                    auxResp.add(respCotizador.get(0));

                    bundle.putString("type", "internacional_sobre");

                    bundle.putString("destino",spn_countrie.getSelectedItem().toString());


                    bundle.putSerializable("auxResp", auxResp);
                    fragment = new FragmentDetailQuoatation();
                    fragment.addFragmentToStack(getActivity());
                    fragment.setArguments(bundle);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack(null);

                    new AsynTask().execute();

                }else if (typeSend.equals("internacional_sobre_eua_canada")) {





                        if (contEUA_Canada == 0) {
                            contEUA_Canada++;
                            respCotizador.get(0).put("DescripcionServicio", "Global Express");
                            auxResp.add(respCotizador.get(0));
                            cotizar("internacional_sobre_eua_canada");
                        } else {
                            respCotizador.get(0).put("DescripcionServicio","USA-Canadá-Estandar");
                            auxResp.add(respCotizador.get(0));
                            bundle.putString("type", "internacional_sobre_eua_canada");
                            bundle.putString("destino",spn_countrie.getSelectedItem().toString());

                            bundle.putSerializable("auxResp", auxResp);
                            fragment = new FragmentDetailQuoatation();
                            fragment.addFragmentToStack(getActivity());
                            fragment.setArguments(bundle);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.addToBackStack(null);

                            new AsynTask().execute();
                        }


                } else if(typeSend.equals("internacional_paquete_eua_canada")){

                    if (contEUA_Canada == 0) {
                        contEUA_Canada++;
                        respCotizador.get(0).put("DescripcionServicio", "Global Express");
                        auxResp.add(respCotizador.get(0));
                        cotizar("internacional_paquete_eua_canada");
                    } else {
                        respCotizador.get(0).put("DescripcionServicio", "USA-Canadá-Estandar");
                        auxResp.add(respCotizador.get(0));
                        bundle.putString("type", "internacional_paquete_eua_canada");

                        bundle.putString("destino",spn_countrie.getSelectedItem().toString());
                        bundle.putString("peso",peso);
                        bundle.putString("alto",alto);
                        bundle.putString("largo",largo);
                        bundle.putString("ancho",ancho);

                        bundle.putSerializable("auxResp", auxResp);
                        fragment = new FragmentDetailQuoatation();
                        fragment.addFragmentToStack(getActivity());
                        fragment.setArguments(bundle);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack(null);

                        new AsynTask().execute();
                    }

                }else if(typeSend.equals("internacional_paquete")){

                    respCotizador.get(0).put("DescripcionServicio","Global Express");

                    auxResp.add(respCotizador.get(0));

                    bundle.putString("type", "internacional_paquete");

                    bundle.putString("destino",spn_countrie.getSelectedItem().toString());
                    bundle.putString("peso",peso);
                    bundle.putString("alto",alto);
                    bundle.putString("largo",largo);
                    bundle.putString("ancho",ancho);

                    bundle.putSerializable("auxResp", auxResp);
                    fragment = new FragmentDetailQuoatation();
                    fragment.addFragmentToStack(getActivity());
                    fragment.setArguments(bundle);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack(null);

                    new AsynTask().execute();

                }

                if (typeSend.equals("codigo_postal")) {
                    showCP(RequestManager.sharedInstance().getResponseArray());
                }



            } else {
                try {
                    DialogManager.sharedInstance().dismissDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio), 2000);
                Log.v("FragmentQuotation", "El servidor devolvio null");
            }
        }else{
            try {
                DialogManager.sharedInstance().dismissDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio1), 2000);
            Log.v("FragmentQuotation", "respCotizador es igual a null");
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    class AsynTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            fragmentTransaction.replace(R.id.container, fragment, FragmentDetailQuoatation.TAG);
            fragmentTransaction.commit();
            return null;
        }


    }

}