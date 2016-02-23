package com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ImageView;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.estafeta.estafetamovilv1.Adapters.CPAListAdapter;
import com.estafeta.estafetamovilv1.Adapters.SpinnerAdapterPrefilled;
import com.estafeta.estafetamovilv1.Async_Request.METHOD;
import com.estafeta.estafetamovilv1.Async_Request.RequestManager;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.FragmentDialogGetDataContacts;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.FragmentFequentlyContacts;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;
import com.estafeta.estafetamovilv1.Utils.Utilities;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled.FragmentPrefilled.DATA_ADDRESSEE;
import com.estafeta.estafetamovilv1.communication.CommunicationBetweenFragments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.model.FrequentlyContacts;

/**
 *
 */
public class FragmentPrefilledAddressee extends TrackerFragment implements View.OnClickListener,
                                                                            FragmentDialogSave.listenerButtonSave,
                                                                            FragmentDialogGetDataContacts.contactsOptionPressed,
                                                                            FragmentDialogContinuePefilled.listenerButtonContinue,
                                                                            FragmentFequentlyContacts.setPressedButtonContinue{
    public boolean testAddresse = false;

    protected static final int REQ_GET_CONTACT = 0;

    private EditText    txt_addressee_name,
                        txt_business_name,
                        txt_addressee_street,
                        txt_no_ext,
                        txt_no_int,
                        txt_zip_code,
                        txt_addressee_phone,
                        txt_addressee_email,
                        txt_addressee_reference,
                        txt_addressee_nave,
                        txt_addressee_platform,
                        txt_city,
                        txt_colony;

    private ImageView   imgv_save_frequent,
                        imgv_download_prefilled,
                        imgv_search_zip_code,
                        imgv_diary;

    private Spinner     spn_addressee_colony,
                        spn_addressee_city,
                        spn_addressee_state,
                        spn_state;

    private Button      btn_generate_code,
                        btn_accept,
                        btn_search,
                        btn_cancel_cp,
                        btn_cancel;

    //Dialogs

    private TextView    lbl_dialog_estado,
                        lbl_dialog_ciudad;

    private List<Map<String,String>> data,respCotizador;

    private List<String>    items_list_colony,
                            items_list_city,
                            items_list_state;

    private SpinnerAdapterPrefilled     spinnerAdapterColony,
                                        spinnerAdapterCity,
                                        spinnerAdapterState;

    private Dialog      dialogCP,
                        dialogPrefilledCode;

    private LinearLayout    lin_capturar_datos,
                            lin_escoger_cp;

    private int             positionCP = 0;

    private ListView        lv_dialog_colonia;

    private boolean         search = true;

    private String          ciudadString,
                            coloniaString,
                            estadoString,
                            cp_obtenido,
                            typeSend = "",
                            zipCodeString,
                            ciudadObtenida = "";


    private long            mLastClickTime;

    private Map<String,String> dataAddressee;

    private CommunicationBetweenFragments listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (CommunicationBetweenFragments) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CommunicationBetweenFragments");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frgment_prefilled_addressee, container, false);
        if(view!=null) {

            //EditTexts
            txt_addressee_name = (EditText) view.findViewById(R.id.txt_addressee_name);
            txt_business_name = (EditText) view.findViewById(R.id.txt_business_name);
            txt_addressee_street = (EditText) view.findViewById(R.id.txt_addressee_street);
            txt_no_ext = (EditText) view.findViewById(R.id.txt_no_ext);
            txt_no_int = (EditText) view.findViewById(R.id.txt_no_int);
            txt_zip_code = (EditText) view.findViewById(R.id.txt_zip_code);
            txt_addressee_phone = (EditText) view.findViewById(R.id.txt_addresse_phone);
            txt_addressee_email = (EditText) view.findViewById(R.id.txt_addressee_email);
            txt_addressee_reference = (EditText) view.findViewById(R.id.txt_addressee_reference);
            txt_addressee_nave = (EditText) view.findViewById(R.id.txt_addressee_nave);
            txt_addressee_platform = (EditText) view.findViewById(R.id.txt_addressee_platform);
            //ImageViews
            imgv_save_frequent = (ImageView) view.findViewById(R.id.imgv_save_frequent);
            imgv_download_prefilled = (ImageView) view.findViewById(R.id.imgv_download_prefilled);
            imgv_search_zip_code = (ImageView) view.findViewById(R.id.imgv_search_zip_code);
            imgv_diary = (ImageView) view.findViewById(R.id.imgv_diary);
            //Spinners
            spn_addressee_colony = (Spinner) view.findViewById(R.id.spn_addressee_colony);
            spn_addressee_colony.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        checkImageSaveFrequently();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spn_addressee_city = (Spinner) view.findViewById(R.id.spn_addressee_city);
            spn_addressee_state = (Spinner) view.findViewById(R.id.spn_addressee_state);
            //Buttons
            btn_generate_code = (Button) view.findViewById(R.id.btn_generate_code);

            //Set Listeners
            imgv_save_frequent.setOnClickListener(this);
            imgv_download_prefilled.setOnClickListener(this);
            imgv_search_zip_code.setOnClickListener(this);
            imgv_diary.setOnClickListener(this);
            btn_generate_code.setOnClickListener(this);
            addTextWatchers();


            data = new ArrayList<>();

            items_list_colony = new ArrayList<>();
            items_list_colony.add("Colonia*");
            spinnerAdapterColony = new SpinnerAdapterPrefilled(getActivity(), items_list_colony);
            spn_addressee_colony.setAdapter(spinnerAdapterColony);

            items_list_city = new ArrayList<>();
            items_list_city.add("Ciudad, municipio, delegación*");
            spinnerAdapterCity = new SpinnerAdapterPrefilled(getActivity(), items_list_city);
            spn_addressee_city.setAdapter(spinnerAdapterCity);

            items_list_state = new ArrayList<>();
            items_list_state.add("Estado*");
            spinnerAdapterState = new SpinnerAdapterPrefilled(getActivity(), items_list_state);
            spn_addressee_state.setAdapter(spinnerAdapterState);


            //Set Hints
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_name), txt_addressee_name);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_business_name), txt_business_name);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_street), txt_addressee_street);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_no_ext), txt_no_ext);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_zip_code), txt_zip_code);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_phone), txt_addressee_phone);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_email), txt_addressee_email);

            initDialogCP();
            initial_txt_zip_code();

            if (testAddresse) {
                txt_addressee_name.setText("Fulanito de Tal");
                txt_business_name.setText("Anonimus");
                txt_addressee_street.setText("Ningun Lugar");
                txt_no_ext.setText("0");
                txt_zip_code.setText("52777");
                txt_addressee_phone.setText("52938400");
                txt_addressee_email.setText("fulanito_el_mejor@hotmail.com");
            }

            imgv_save_frequent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try {
                SVG svg = SVG.getFromResource(getActivity(), R.raw.guardar_gris);
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                imgv_save_frequent.setImageDrawable(drawable);
            } catch (SVGParseException e) {
            }

            imgv_download_prefilled.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try {
                SVG svg = SVG.getFromResource(getActivity(), R.raw.prellenado);
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                imgv_download_prefilled.setImageDrawable(drawable);
            } catch (SVGParseException e) {

            }

        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.imgv_search_zip_code:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                positionCP = 0;
                txt_city.setText("");
                txt_colony.setText("");
                openDialogCP();

                break;

            case R.id.imgv_diary:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                FragmentDialogGetDataContacts fdgdc = new FragmentDialogGetDataContacts();
                fdgdc.listener = this;
                fdgdc.show(getActivity().getSupportFragmentManager(), fdgdc.TAG);

                break;

            case R.id.imgv_download_prefilled:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                FragmentDialogContinuePefilled fdcp = new FragmentDialogContinuePefilled();
                fdcp.listener = this;
                fdcp.show(getActivity().getSupportFragmentManager(), fdcp.TAG);


                break;

            case R.id.imgv_save_frequent:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if(checkIfDatsIsComplete()){
                    if(FrequentlyContacts.checkIfExistContactInDB(getActivity(), getText(txt_addressee_name), getText(txt_business_name), getText(txt_addressee_street),
                            getText(txt_no_ext), getText(txt_no_int), getText(txt_zip_code), getText(txt_addressee_phone), getText(txt_addressee_email), getText(txt_addressee_reference),
                            getText(txt_addressee_nave), getText(txt_addressee_platform))){
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "Ya existe un contacto con esta información.", 3000);
                    }else {
                        FragmentDialogSave fds = new FragmentDialogSave();
                        fds.listener = this;
                        fds.question = FragmentDialogSave.QUESTION.SENDER;
                        fds.show(getActivity().getSupportFragmentManager(), fds.TAG);
                    }
                }else{
                    if(checkIfDataIsNull()){
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No se puede posponer un prellenado vacio.", 3000);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString("email_postpone_code", txt_addressee_email.getText().toString());

                        FragmentDialogPostponePrefilled fdpp = new FragmentDialogPostponePrefilled();
                        fdpp.setArguments(bundle);
                        fdpp.show(getActivity().getSupportFragmentManager(), fdpp.TAG);
                    }
                }

                break;

            case R.id.btn_generate_code:
                if(validateFields()){

                    saveDataAddressee();
                    dataAddressee.putAll(listener.getDataSender());

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dataAddressee", (Serializable) dataAddressee);

                    FragmentDialogQRCode fdqrc = new FragmentDialogQRCode();
                    fdqrc.setArguments(bundle);
                    fdqrc.show(getActivity().getSupportFragmentManager(), fdqrc.TAG);


                }
                break;

        }
    }


    private void initDialogCP(){

        dialogCP = new Dialog(getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth);
        dialogCP.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCP.setContentView(R.layout.dialog_quotation_search_zc);
        dialogCP.setCancelable(false);

        spn_state           = (Spinner) dialogCP.findViewById(R.id.spn_state);
        lbl_dialog_estado    = (TextView)dialogCP.findViewById(R.id.tv_dialog_estado);
        lbl_dialog_ciudad    = (TextView)dialogCP.findViewById(R.id.tv_dialog_ciudad);
        txt_city            = (EditText) dialogCP.findViewById(R.id.edt_city);
        txt_city.getBackground().setColorFilter(getResources().getColor(R.color.estafeta_soft_gray), PorterDuff.Mode.SRC_ATOP);

        txt_colony          = (EditText) dialogCP.findViewById(R.id.edt_colony);
        txt_colony.getBackground().setColorFilter(getResources().getColor(R.color.estafeta_soft_gray), PorterDuff.Mode.SRC_ATOP);
        btn_accept          = (Button) dialogCP.findViewById(R.id.btn_accept);
        btn_cancel_cp       = (Button)dialogCP.findViewById(R.id.btn_cancel_cp);
        btn_search          = (Button) dialogCP.findViewById(R.id.btn_search);
        btn_cancel          = (Button) dialogCP.findViewById(R.id.btn_cancel);
        lin_capturar_datos  = (LinearLayout)dialogCP.findViewById(R.id.lin_capturar_datos);
        lin_escoger_cp      = (LinearLayout)dialogCP.findViewById(R.id.lin_escoger_cp);
        lv_dialog_colonia   = (ListView)dialogCP.findViewById(R.id.lv_dialog_colonia);

        //Set Hint
        Utilities.setCustomHint(getActivity(), getString(R.string.city), txt_city);

        btn_cancel_cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCP.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(FRAGMENT_TAG.FRAG_PRELLENADO.toString(), "CANCEL");
                dialogCP.dismiss();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Log.d(FRAGMENT_TAG.FRAG_PRELLENADO.toString(), "ACCEPT");
                searchZC();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ciudadObtenida.equals("")){
                    lv_dialog_colonia.performItemClick(
                            lv_dialog_colonia.getAdapter().getView(0, null, null), 0, lv_dialog_colonia.getAdapter().getItemId(0));
                }

                dialogCP.dismiss();

                search = false;
                txt_zip_code.setText("" + cp_obtenido);

                items_list_colony.removeAll(items_list_colony);
                items_list_colony.add("Colonia*");
                for(Map<String,String> map : data){
                    items_list_colony.add(map.get("colonia"));
                }

                spinnerAdapterColony.notifyDataSetChanged();
                if(items_list_colony.size() > 1) {
                    spn_addressee_colony.setSelection(positionCP+1);
                }else if(items_list_colony.size() == 1){
                    spn_addressee_colony.setSelection(0);
                }

                items_list_city.removeAll(items_list_city);
                items_list_city.add("Ciudad, municipio, delegación*");
                int pos = 0;
                for(Map<String,String> map : data){
                    if(!items_list_city.contains(map.get("ciudad"))){
                        items_list_city.add(map.get("ciudad"));
                        if(!ciudadObtenida.equals("") && ciudadObtenida.equals(map.get("ciudad").toString())){
                            pos = items_list_city.size()-1;
                        }
                    }
                }
                spinnerAdapterCity.notifyDataSetChanged();
                if(items_list_city.size() > 1) {
                    spn_addressee_city.setSelection(pos);
                }else if(items_list_city.size() == 1){
                    spn_addressee_city.setSelection(0);
                }

                items_list_state.removeAll(items_list_state);
                items_list_state.add("Estado*");
                items_list_state.add(estadoString.toUpperCase());

                spinnerAdapterState.notifyDataSetChanged();

                if(items_list_state.size() > 1) {
                    spn_addressee_state.setSelection(1);
                }else if(items_list_state.size() == 1){
                    spn_addressee_state.setSelection(0);
                }

                //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * Open dialog to search for a zip code.
     */
    public void openDialogCP(){

        if(!dialogCP.isShowing()) {

            setStatesToSpinner(spn_state, getActivity());
            lin_escoger_cp.setVisibility(View.GONE);
            lin_capturar_datos.setVisibility(View.VISIBLE);

            dialogCP.show();
        }

    }


    /**
     * Search the zip code on the server.
     */
    private void searchZC() {

        ciudadString = txt_city.getText().toString();

        if (spn_state.getSelectedItemPosition() == 0) {

            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_estado), 3000);
        } else if (ciudadString == null || ciudadString.equals("")) {
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_ciudad), 3000);
        } else {
            ciudadString = txt_city.getText().toString();
            coloniaString = txt_colony.getText().toString();
            estadoString = spn_state.getSelectedItem().toString();


            if (estadoString.equalsIgnoreCase("Baja California Norte")) {
                estadoString = "BAJA CALIFORNIA";
            } else if (estadoString.equalsIgnoreCase("Estado de México")) {
                estadoString = "EDO. DE MEXICO";
            } else if (estadoString.equalsIgnoreCase("México, D.F.")) {
                estadoString = "DISTRITO FEDERAL";
            }


            if (Utilities.specialCharacteresInString(ciudadString)){
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No se permiten acentos ni caracteres especiales.", 3000);
                return;
            }
            if (Utilities.specialCharacteresInString(coloniaString)){
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No se permiten acentos ni caracteres especiales.", 3000);
                return;
            }


            Map<String, String> requestData = new HashMap<>();
            requestData = new HashMap<>();

            requestData.put("ciudad", convertNonAscii(ciudadString).trim());
            requestData.put("localidad", convertNonAscii(coloniaString).trim());

            requestData.put("pais", "MEXICO");
            requestData.put("estado", estadoString);

            typeSend = "código_postal";

            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando), 0);
            RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE, requestData,this);

        }
    }


    /**
     * It lets use the Android API contacts to find a zip code.
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
                String infoAddress[] = getInfoAddress(contactId);
                String[] infoContact = getContactName(contactId);
                String numberPhone = getContactNumberPhone(contactId);
                String email = getContactEmail(contactId);

                try {

                    txt_no_ext.setText("");
                    txt_no_int.setText("");
                    txt_business_name.setText("");
                    txt_addressee_reference.setText("");
                    txt_addressee_nave.setText("");
                    txt_addressee_platform.setText("");

                    if(!numberPhone.equals("")){
                        txt_addressee_phone.setText(numberPhone);
                    }else{
                        txt_addressee_phone.setText("");
                    }

                    if (infoContact.length != 0) {
                        if (!infoContact[2].equals("")) {
                            txt_addressee_name.setText(infoContact[2]);
                        } else if (!infoContact[1].equals("")) {
                            txt_addressee_name.setText(infoContact[1]);
                        } else if (!infoContact[0].equals("")) {
                            txt_addressee_name.setText(infoContact[0]);
                        } else {
                            txt_addressee_name.setText("");
                        }
                    }

                    if (!email.equals("")) {
                        txt_addressee_email.setText(email);
                    } else {
                        txt_addressee_email.setText("");
                    }

                    if (infoAddress.length != 0) {

                        if (infoAddress[1] != null && !infoAddress[1].equals("")) {
                            txt_addressee_street.setText(infoAddress[1]);
                        } else {
                            txt_addressee_street.setText("");
                        }

                        try {
                            if (infoAddress[0] != null) { // No es null
                                if (infoAddress[0].trim().length() > 0) { // Que longitud tiene?
                                    Double.parseDouble(infoAddress[0]); // Es numero?
                                    if (infoAddress[0].trim().length() == 5) { // es igual a cinco?
                                        if (requestCode == REQ_GET_CONTACT) {
                                            spn_addressee_colony.setSelection(0);
                                            txt_zip_code.setText("" + infoAddress[0]);

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
                            if (requestCode == REQ_GET_CONTACT) {
                                txt_zip_code.setText("");
                            }
                            clearSpinnersZipCode();
                            //DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.noPostalCode), 3000);
                        }
                    }
                }catch(Exception e){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No se puede obtener información de este contacto.", 3000);
                }

                break;
            case Activity.RESULT_CANCELED:

            default:
        }
    }

    /**
     * Gets the zip code of a contact.
     * @param contactId
     * @return
     */
    private String[] getInfoAddress(String contactId) {

        String[] infoAddress = new String[2];
        String where = ContactsContract.Data.CONTACT_ID + " =  ? "
                + " AND "+ ContactsContract.Data.MIMETYPE +" = ? ";

        String[] projection = new String[] { ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE };
        String[] params = new String[] {
                contactId,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
        Cursor cursor = getActivity().getContentResolver().query(
                ContactsContract.Data.CONTENT_URI, null, where, params,
                null);
        if (cursor.moveToFirst()) {
            String postcode = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
            //String country = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
            //String city = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            String street = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));

            infoAddress[0] = postcode;
            infoAddress[1] = street;
        }
        cursor.close();
        return infoAddress;
    }

    private String[] getContactName(String contactId){
        String[] info = new String[3];

        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
        String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, contactId };
        Cursor nameCur = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
        while (nameCur.moveToNext()) {
            String given = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
            String family = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
            String display = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
            String email = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            info[0] = given;
            info[1] = family;
            info[2] = display;
        }
        nameCur.close();

        return info;
    }

    private String getContactEmail(String contactId){

        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?";
        String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, contactId };
        Cursor nameCur = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, null);
        while (nameCur.moveToNext()) {
            String email = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            return email;
        }
        nameCur.close();

        return "";
    }

    private String getContactNumberPhone(String contactId){

        Cursor nameCur = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (nameCur.moveToNext()) {
            if (Integer.parseInt(nameCur.getString(nameCur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                        new String[]{contactId}, null);
                while (cursor.moveToNext()) {
                    String phone = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    return phone;
                }
                cursor.close();
            }
        }
        nameCur.close();

        return "";
    }


    private void clearSpinnersZipCode(){

        if(spn_addressee_colony.getCount() > 1) {

            items_list_colony.removeAll(items_list_colony);
            items_list_colony.add("Colonia*");
            spinnerAdapterColony.notifyDataSetChanged();
        }

        if(spn_addressee_city.getCount() > 1) {
            items_list_city.removeAll(items_list_city);
            items_list_city.add("Ciudad, municipio, delegación*");
            spinnerAdapterCity.notifyDataSetChanged();
        }

        if(spn_addressee_state.getCount() > 1) {
            items_list_state.removeAll(items_list_state);
            items_list_state.add("Estado*");
            spinnerAdapterState.notifyDataSetChanged();
        }

    }



    private void initial_txt_zip_code(){

        txt_zip_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 5){
                    if(search) {
                        zipCodeString = txt_zip_code.getText().toString();
                        if (zipCodeString == null || zipCodeString.equals("")) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_codigo), 3000);
                        } else if (zipCodeString.length() < 5) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_codigo), 3000);

                        } else {

                            typeSend = "search_colony_city_state";
                            //MapString Params...
                            //Query from Zip Code
                            Map<String, String> requestData = new HashMap<>();
                            requestData.put("pais", "Mexico");
                            requestData.put("codigoPostal", zipCodeString);
                            //Send params to RequestManager
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando), 0);
                            RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE_ADDRESSES, requestData, FragmentPrefilledAddressee.this);

                        }
                    }else{
                        search = true;
                    }
                }else if(s.length() == 4){

                    clearSpinnersZipCode();

                }
            }
        });

    }


    /**
     * This method shows the response of the zip code search.
     * @param resp
     */
    public void showCP(ArrayList<Map<String,String>> resp){

        try {

            if(data.size() != 0 && data != null) {
                data.removeAll(data);
            }
            data = resp;
            final CPAListAdapter adapter = new CPAListAdapter(getActivity(),getActivity().getApplicationContext(),resp,"cotizador");
            lv_dialog_colonia.setAdapter(adapter);

            lv_dialog_colonia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    positionCP = position;
                    lv_dialog_colonia.setSelected(true);

                    adapter.setSelectionState(true, view);
                    adapter.setLastSelectedItemPosition(position);
                    cp_obtenido = ((Map<String,String>)lv_dialog_colonia.getItemAtPosition(positionCP)).get("cp");
                    ciudadObtenida = ((Map<String,String>)lv_dialog_colonia.getItemAtPosition(positionCP)).get("ciudad");

                }
            });

            cp_obtenido = ((Map<String,String>)lv_dialog_colonia.getItemAtPosition(positionCP)).get("cp");

            lbl_dialog_ciudad.setText(ciudadString);

            lbl_dialog_estado.setText(estadoString);

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
    public void decodeResponse(String response) {
        Log.d(FRAGMENT_TAG.FRAG_PRELLENADO.toString(), "" + response);

        respCotizador = RequestManager.sharedInstance().getResponseArray();

        if(respCotizador != null) {

            if (response != null && response.length() > 0) {

                if (typeSend.equals("código_postal")) {
                    ArrayList<Map<String, String>> resp;
                    resp = RequestManager.sharedInstance().getResponseArray();

                    if (resp != null && resp.size() > 0)
                        showCP(resp);
                    else {
                        clearSpinnersZipCode();
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio), 3000);
                    }
                }else if (typeSend.equals("search_colony_city_state")) {

                    ArrayList<Map<String, String>> resp;
                    resp = RequestManager.sharedInstance().getResponseArray();
                    if (resp != null && resp.size() > 0) {

                        items_list_colony.removeAll(items_list_colony);
                        items_list_colony.add("Colonia*");
                        for(Map<String,String> map : resp){
                            items_list_colony.add(map.get("colonia"));
                        }

                        spinnerAdapterColony.notifyDataSetChanged();
                       /* SpinnerAdapterPrefilled spinnerAdapterColony = new SpinnerAdapterPrefilled(getActivity(), items_list_colony);
                        spn_sender_colony.setAdapter(spinnerAdapterColony);*/
                        if(items_list_colony.size() > 1) {
                            spn_addressee_colony.setSelection(1);
                        }else if(items_list_colony.size() == 1){
                            spn_addressee_colony.setSelection(0);
                        }

                        items_list_city.removeAll(items_list_city);
                        items_list_city.add("Ciudad, municipio, delegación*");
                        items_list_city.add(resp.get(0).get("ciudad"));
                        spinnerAdapterCity.notifyDataSetChanged();
                        /*SpinnerAdapterPrefilled spinnerAdapterCity = new SpinnerAdapterPrefilled(getActivity(), items_list_city);
                        spn_sender_city.setAdapter(spinnerAdapterCity);*/
                        if(items_list_city.size() > 1) {
                            spn_addressee_city.setSelection(1);
                        }else if(items_list_city.size() == 1){
                            spn_addressee_city.setSelection(0);
                        }

                        items_list_state.removeAll(items_list_state);
                        items_list_state.add("Estado*");
                        items_list_state.add(resp.get(0).get("estado").trim());
                        spinnerAdapterState.notifyDataSetChanged();
                        /*SpinnerAdapterPrefilled spinnerAdapterState = new SpinnerAdapterPrefilled(getActivity(), items_list_state);
                        spn_sender_state.setAdapter(spinnerAdapterState);*/
                        if(items_list_state.size() > 1) {
                            spn_addressee_state.setSelection(1);
                        }else if(items_list_state.size() == 1){
                            spn_addressee_state.setSelection(0);
                        }


                        try {
                            DialogManager.sharedInstance().dismissDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }else {
                        clearSpinnersZipCode();
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio), 3000);
                    }

                }

            }else {
                try {
                    if (typeSend.equals("search_colony_city_state")) {
                        txt_zip_code.setText("");
                        clearSpinnersZipCode();
                    }
                    DialogManager.sharedInstance().dismissDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_service), 3000);
                Log.v("FragmentQuotation", "El servidor devolvio null");
            }

        }else{
            try {
                if (typeSend.equals("search_colony_city_state")) {
                    txt_zip_code.setText("");
                    clearSpinnersZipCode();
                }
                DialogManager.sharedInstance().dismissDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio1), 3000);
            Log.v("FragmentQuotation", "respCotizador es igual a null");
        }
    }


    @Override
    public void optionPressed(boolean isContacts) {
        if(isContacts){
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            Intent intent = new Intent(Intent.ACTION_PICK, uri);
            startActivityForResult(intent, REQ_GET_CONTACT);
        }else{
            if(FrequentlyContacts.getCountFrequentlyContacts(getActivity())>0) {
                FragmentFequentlyContacts ffc = new FragmentFequentlyContacts();
                ffc.listener = this;
                ffc.show(getActivity().getSupportFragmentManager(),FRAGMENT_TAG.FRAG_FRECUENTES.toString());
            }else{
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No tienes contactos frecuentes guardados", 3000);
            }
        }
    }

    @Override
    public void buttonSavePressed() {

        FrequentlyContacts.insert(getActivity(), getText(txt_addressee_name), getText(txt_business_name), getText(txt_addressee_street),
                getText(txt_no_ext),getText(txt_no_int),getText(txt_zip_code), getText(txt_addressee_phone), getText(txt_addressee_email),
                getText(txt_addressee_reference), getText(txt_addressee_nave), getText(txt_addressee_platform));

        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SUCCESS, "El contacto fue guardado con exito", 3000);

    }

    @Override
    public void buttonContinuePressed(String code) {
        Toast.makeText(getActivity(), "Módulo en desarrollo.", Toast.LENGTH_SHORT).show();
    }

    private boolean validateFields(){

        if(txt_addressee_name.getText().toString().equals("")){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            txt_addressee_name.requestFocus();
            return false;
        } else if(txt_business_name.getText().toString().equals("")){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            txt_business_name.requestFocus();
            return false;
        } else if(txt_addressee_street.getText().toString().equals("")){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            txt_addressee_street.requestFocus();
            return false;
        } else if(txt_no_ext.getText().toString().equals("")){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            txt_no_ext.requestFocus();
            return false;
        } else if(txt_zip_code.getText().toString().equals("")){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            txt_zip_code.requestFocus();
            return false;
        } else if(txt_zip_code.getText().toString().length() != 5){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            txt_zip_code.requestFocus();
            return false;
        } else if(spn_addressee_colony.getSelectedItem().toString().equals(items_list_colony.get(0))){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            return false;
        } else if(spn_addressee_city.getSelectedItem().toString().equals(items_list_city.get(0))){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            return false;
        } else if(spn_addressee_state.getSelectedItem().toString().equals(items_list_state.get(0))){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            return false;
        } else if(txt_addressee_phone.getText().toString().equals("")){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            txt_addressee_phone.requestFocus();
            return false;
        } else if(!Utilities.validateEmail(getActivity(),txt_addressee_email.getText().toString())){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            txt_addressee_email.requestFocus();
            return false;
        }

        return true;
    }


    private boolean checkIfDatsIsComplete(){

        if(txt_addressee_name.getText().toString().equals("")){
            return false;
        } else if(txt_business_name.getText().toString().equals("")){
            return false;
        } else if(txt_addressee_street.getText().toString().equals("")){
            return false;
        } else if(txt_no_ext.getText().toString().equals("")){
            return false;
        } else if(txt_addressee_phone.getText().toString().equals("")){
            return false;
        }  else if(txt_zip_code.getText().toString().equals("") && txt_zip_code.getText().toString().length() != 5){
            return false;
        } else if(spn_addressee_colony.getSelectedItem().toString().equals(items_list_colony.get(0))){
            return false;
        } else if(spn_addressee_city.getSelectedItem().toString().equals(items_list_city.get(0))){
            return false;
        } else if(spn_addressee_state.getSelectedItem().toString().equals(items_list_state.get(0))){
            return false;
        } else if(!Utilities.validateEmail(getActivity(),txt_addressee_email.getText().toString())){
            return false;
        }

        return true;
    }

    private boolean checkIfDataIsNull(){

        if(!txt_addressee_name.getText().toString().equals("")){
            return false;
        } else if(!txt_business_name.getText().toString().equals("")){
            return false;
        } else if(!txt_addressee_street.getText().toString().equals("")){
            return false;
        } else if(!txt_no_ext.getText().toString().equals("")){
            return false;
        } else if(!txt_addressee_phone.getText().toString().equals("")){
            return false;
        } else if(!txt_zip_code.getText().toString().equals("") && txt_zip_code.getText().toString().length() != 5){
            return false;
        } else if(!txt_addressee_email.getText().toString().equals("")){
            return false;
        }

        return true;
    }

    private void checkImageSaveFrequently(){
        if(checkIfDatsIsComplete()){
            imgv_save_frequent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try
            {
                SVG svg = SVG.getFromResource(getActivity(), R.raw.guardar);
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                imgv_save_frequent.setImageDrawable(drawable);
            }
            catch(SVGParseException e)
            {}

        }else{
            imgv_save_frequent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try
            {
                SVG svg = SVG.getFromResource(getActivity(), R.raw.guardar_gris);
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                imgv_save_frequent.setImageDrawable(drawable);
            }
            catch(SVGParseException e) {
            }
        }
    }

    private void addTextWatchers(){

        txt_addressee_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txt_addressee_name.setError(null);
                if (s.length() <= 1) {
                    checkImageSaveFrequently();
                }
            }
        });

        txt_business_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txt_business_name.setError(null);
                if (s.length() <= 1) {
                    checkImageSaveFrequently();
                }
            }
        });

        txt_addressee_street.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txt_addressee_street.setError(null);
                if (s.length() <= 1) {
                    checkImageSaveFrequently();
                }
            }
        });

        txt_no_ext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txt_no_ext.setError(null);
                if (s.length() <= 1) {
                    checkImageSaveFrequently();
                }
            }
        });

        txt_addressee_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txt_addressee_phone.setError(null);
                if (s.length() <= 1) {
                    checkImageSaveFrequently();
                }
            }
        });


        txt_addressee_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txt_addressee_email.setError(null);
                if (s.length() >2) {
                    checkImageSaveFrequently();
                }
            }
        });

    }

    private void saveDataAddressee(){
        dataAddressee = new HashMap<>();
        dataAddressee.put(DATA_ADDRESSEE.ADDRESSEE_NAME.toString(), txt_addressee_name.getText().toString());
        dataAddressee.put(DATA_ADDRESSEE.ADDRESSEE_BUSINESS_NAME.toString(), txt_business_name.getText().toString());
        dataAddressee.put(DATA_ADDRESSEE.STREET_ADDRESSEE.toString(), txt_addressee_street.getText().toString());
        dataAddressee.put(DATA_ADDRESSEE.NO_INT_ADDRESSEE.toString(),txt_no_int.getText().toString());
        dataAddressee.put(DATA_ADDRESSEE.ZIP_CODE_ADDRESSEE.toString(),txt_zip_code.getText().toString());
        dataAddressee.put(DATA_ADDRESSEE.NO_EXT_ADDRESSEE.toString(),txt_no_ext.getText().toString());
        dataAddressee.put(DATA_ADDRESSEE.COLONY_ADDRESSEE.toString(),spn_addressee_colony.getSelectedItem().toString());
        dataAddressee.put(DATA_ADDRESSEE.CITY_ADDRESSEE.toString(),spn_addressee_city.getSelectedItem().toString());
        dataAddressee.put(DATA_ADDRESSEE.STATE_ADDRESSEE.toString(),spn_addressee_state.getSelectedItem().toString());
        dataAddressee.put(DATA_ADDRESSEE.PHONE_ADDRESSEE.toString(), txt_addressee_phone.getText().toString());
        dataAddressee.put(DATA_ADDRESSEE.EMAIL_ADDRESSEE.toString(), txt_addressee_email.getText().toString());
        dataAddressee.put(DATA_ADDRESSEE.REFERENCE_ADDRESSEE.toString(), txt_addressee_reference.getText().toString());
        dataAddressee.put(DATA_ADDRESSEE.NAVE_ADDRESSEE.toString(), txt_addressee_nave.getText().toString());
        dataAddressee.put(DATA_ADDRESSEE.PLATFORM_ADDRESSEE.toString(), txt_addressee_platform.getText().toString());

    }


    private String getText(EditText txt_view){

        String text = txt_view.getText().toString();

        return text;
    }

    @Override
    public void pressedButtonContinue(Map<String, String> dataContact) {

        spn_addressee_colony.setSelection(0);

        txt_addressee_name.setText(dataContact.get(FrequentlyContacts.NAME_CONTACT));
        txt_business_name.setText(dataContact.get(FrequentlyContacts.BUSINESS_NAME_CONTACT));
        txt_addressee_street.setText(dataContact.get(FrequentlyContacts.STREET_CONTACT));
        txt_no_ext.setText(dataContact.get(FrequentlyContacts.NO_EXT_CONTACT));
        txt_no_int.setText(dataContact.get(FrequentlyContacts.NO_INT_CONTACT));
        txt_zip_code.setText(dataContact.get(FrequentlyContacts.CP_CONTACT));
        txt_addressee_phone.setText(dataContact.get(FrequentlyContacts.PHONE_CONTACT));
        txt_addressee_email.setText(dataContact.get(FrequentlyContacts.EMAIL_CONTACT));
        txt_addressee_reference.setText(dataContact.get(FrequentlyContacts.REFERENCE_CONTACT));
        txt_addressee_nave.setText(dataContact.get(FrequentlyContacts.NAVE_CONTACT));
        txt_addressee_platform.setText(dataContact.get(FrequentlyContacts.PLATFORM_CONTACT));
    }
}
