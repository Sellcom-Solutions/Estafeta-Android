package com.estafeta.estafetamovilv1.Fragments.Fase_2.QuoteAndBuy;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.estafeta.estafetamovilv1.Adapters.SpinnerAdapterPrefilled;
import com.estafeta.estafetamovilv1.Async_Request.METHOD;
import com.estafeta.estafetamovilv1.Async_Request.RequestManager;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.FragmentDialogGetDataContacts;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.FragmentFequentlyContacts;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled.FragmentDialogSave;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;
import com.estafeta.estafetamovilv1.Utils.Utilities;
import com.estafeta.estafetamovilv1.communication.CommunicationBetweenFragments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.model.FrequentlyContacts;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentQuotationBuyAddressee extends TrackerFragment implements View.OnClickListener,
                                                                                FragmentDialogSave.listenerButtonSave,
                                                                                FragmentDialogGetDataContacts.contactsOptionPressed,
                                                                                FragmentFequentlyContacts.setPressedButtonContinue{



    private boolean statusImageRed = false;

    protected static final int REQ_GET_CONTACT = 0;

    private EditText txt_addressee_name,
            txt_business_name,
            txt_addressee_street,
            txt_no_ext,
            txt_no_int,
            txt_zip_code,
            txt_addressee_phone,
            txt_addressee_email,
            txt_city,
            txt_colony;

    private ImageView imgv_save_frequent,
            imgv_diary;

    private Spinner spn_addressee_colony,
            spn_addressee_city,
            spn_addressee_state;

    private Button btn_next;


    private List<String> items_list_colony,
            items_list_city,
            items_list_state;

    private SpinnerAdapterPrefilled spinnerAdapterColony,
            spinnerAdapterCity,
            spinnerAdapterState;

    private String          typeSend = "",
            zipCodeString,
            cp_origin = "";

    private List<Map<String,String>> respCotizador;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().getString("cp_destiny") != null){
            cp_origin = getArguments().getString("cp_destiny");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quotation_buy_addressee, container, false);
        if(view != null) {

            //Buttons
            btn_next = (Button) view.findViewById(R.id.btn_next);

            //ImageView
            imgv_save_frequent = (ImageView) view.findViewById(R.id.imgv_save_frequent);
            imgv_diary = (ImageView) view.findViewById(R.id.imgv_diary);
            //EditText
            txt_addressee_name = (EditText) view.findViewById(R.id.txt_addressee_name);
            txt_business_name = (EditText) view.findViewById(R.id.txt_business_name);
            txt_addressee_street = (EditText) view.findViewById(R.id.txt_addressee_street);
            txt_no_ext = (EditText) view.findViewById(R.id.txt_no_ext);
            txt_no_int = (EditText) view.findViewById(R.id.txt_no_int);
            txt_zip_code = (EditText) view.findViewById(R.id.txt_zip_code);
            txt_addressee_phone = (EditText) view.findViewById(R.id.txt_addressee_phone);
            txt_addressee_email = (EditText) view.findViewById(R.id.txt_addressee_email);
            //Spinners
            spn_addressee_colony = (Spinner) view.findViewById(R.id.spn_addressee_colony);
            spn_addressee_city = (Spinner) view.findViewById(R.id.spn_addressee_city);
            spn_addressee_state = (Spinner) view.findViewById(R.id.spn_addressee_state);

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

            //Set Listeners
            imgv_diary.setOnClickListener(this);
            imgv_save_frequent.setOnClickListener(this);
            btn_next.setOnClickListener(this);

            addTextWatchers();

            //Set Hints
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_name), txt_addressee_name);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_business_name), txt_business_name);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_street), txt_addressee_street);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_no_ext), txt_no_ext);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_phone), txt_addressee_phone);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_email), txt_addressee_email);

            imgv_save_frequent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try {
                SVG svg = SVG.getFromResource(getActivity(), R.raw.guardar_gris);
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                imgv_save_frequent.setImageDrawable(drawable);

                statusImageRed = false;
            } catch (SVGParseException e) {
            }

            initial_txt_zip_code();

            txt_zip_code.setText(cp_origin);

        }
        return view;
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
                if (s.length() == 5) {
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
                        RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE_ADDRESSES, requestData, FragmentQuotationBuyAddressee.this);

                    }

                }
            }
        });

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

                    if (infoAddress.length != 0) {

                        try {
                            if (infoAddress[0] != null) { // No es null
                                if (infoAddress[0].length() > 0) { // Que longitud tiene?
                                    if (infoAddress[0].length() == 5) { // es igual a cinco?
                                        if (requestCode == REQ_GET_CONTACT) {
                                            if(infoAddress[0].equals("")){
                                                Double.parseDouble(infoAddress[0]); // Es numero?
                                            }else if(!txt_zip_code.getText().toString().equals(infoAddress[0])){
                                                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No se puede obtener información de este contacto. El código postal de este contacto no corresponde con el código postal registrado.", 3000);
                                                return;
                                            }
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
                            e.printStackTrace();
                        }

                        if (infoAddress[1] != null && !infoAddress[1].equals("")) {
                            txt_addressee_street.setText(infoAddress[1]);
                        } else {
                            txt_addressee_street.setText("");
                        }
                    }

                    txt_no_ext.setText("");
                    txt_no_int.setText("");
                    txt_business_name.setText("");

                    if(numberPhone!= null && !numberPhone.equals("")){
                        txt_addressee_phone.setText(numberPhone);
                    }else{
                        txt_addressee_phone.setText("");
                    }
                    if (infoContact.length != 0) {
                        if (infoContact[2] != null && !infoContact[2].equals("")) {
                            txt_addressee_name.setText(infoContact[2]);
                        } else if (infoContact[1] != null && !infoContact[1].equals("")) {
                            txt_addressee_name.setText(infoContact[1]);
                        } else if (infoContact[0] != null && !infoContact[0].equals("")) {
                            txt_addressee_name.setText(infoContact[0]);
                        } else {
                            txt_addressee_name.setText("");
                        }
                    }
                    if (email != null && !email.equals("")) {
                        txt_addressee_email.setText(email);
                    } else {
                        txt_addressee_email.setText("");
                    }

                }catch(Exception e){
                    e.printStackTrace();
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


    @Override
    public void decodeResponse(String response) {
        Log.d(FRAGMENT_TAG.FRAG_PRELLENADO.toString(), "" + response);

        respCotizador = RequestManager.sharedInstance().getResponseArray();

        if(respCotizador != null) {

            if (response != null && response.length() > 0) {

                if (typeSend.equals("search_colony_city_state")) {

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
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio), 3000);
                        Handler handler = new Handler ();//Para dar un tiempo
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                getActivity().onBackPressed();
                            }
                        }, 500);
                    }

                }

            }else {
                try {
                    if (typeSend.equals("search_colony_city_state")) {

                    }
                    DialogManager.sharedInstance().dismissDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().onBackPressed();
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_service), 3000);
                    return;
                }
                getActivity().onBackPressed();
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_service), 3000);

                Log.v("FragmentQuotation", "El servidor devolvio null");
            }

        }else{
            try {
                if (typeSend.equals("search_colony_city_state")) {
                    txt_zip_code.setText("");
                }
                DialogManager.sharedInstance().dismissDialog();
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().onBackPressed();
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio1), 3000);
                return;
            }
            getActivity().onBackPressed();
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio1), 3000);
            Log.v("FragmentQuotation", "respCotizador es igual a null");
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
                if (s.length() >= 0) {
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
                if (s.length() >= 0) {
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
                if (s.length() >= 0) {
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
                if (s.length() >= 0) {
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
                if (s.length() >= 0) {
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
                if (s.length() >= 0) {
                    checkImageSaveFrequently();
                }
            }
        });

    }

    private void checkImageSaveFrequently(){
        if(checkIfDatsIsComplete()){

            if(!statusImageRed) {
                imgv_save_frequent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                try {
                    SVG svg = SVG.getFromResource(getActivity(), R.raw.guardar);
                    Drawable drawable = new PictureDrawable(svg.renderToPicture());
                    imgv_save_frequent.setImageDrawable(drawable);
                    statusImageRed = true;
                } catch (SVGParseException e) {
                }
            }

        }else{
            if(statusImageRed) {
                imgv_save_frequent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                try {
                    SVG svg = SVG.getFromResource(getActivity(), R.raw.guardar_gris);
                    Drawable drawable = new PictureDrawable(svg.renderToPicture());
                    imgv_save_frequent.setImageDrawable(drawable);
                    statusImageRed = false;

                } catch (SVGParseException e) {
                }
            }
        }
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
        } else if(txt_zip_code.getText().toString().equals("") && txt_zip_code.getText().toString().length() != 5){
            return false;
        } else if(spn_addressee_colony.getSelectedItem().toString().equals(items_list_colony.get(0))){
            return false;
        } else if(spn_addressee_city.getSelectedItem().toString().equals(items_list_city.get(0))){
            return false;
        } else if(spn_addressee_state.getSelectedItem().toString().equals(items_list_state.get(0))){
            return false;
        } else if(txt_addressee_phone.getText().toString().equals("")){
            return false;
        } else if(txt_addressee_email.getText().toString().equals("")){
            return false;
        }

        return true;
    }

    private boolean validateFields(){

        if(txt_addressee_name.getText().toString().equals("")){
            txt_addressee_name.setError(getActivity().getResources().getText(R.string.error_empty_field).toString());
            txt_addressee_name.requestFocus();
            return false;
        } else if(txt_business_name.getText().toString().equals("")){
            txt_business_name.setError(getActivity().getResources().getText(R.string.error_empty_field).toString());
            txt_business_name.requestFocus();
            return false;
        } else if(txt_addressee_street.getText().toString().equals("")){
            txt_addressee_street.setError(getActivity().getResources().getText(R.string.error_empty_field).toString());
            txt_addressee_street.requestFocus();
            return false;
        } else if(txt_no_ext.getText().toString().equals("")){
            txt_no_ext.setError(getActivity().getResources().getText(R.string.error_empty_field).toString());
            txt_no_ext.requestFocus();
            return false;
        } else if(txt_addressee_phone.getText().toString().equals("")){
            txt_addressee_phone.setError(getActivity().getResources().getText(R.string.error_empty_field).toString());
            txt_addressee_phone.requestFocus();
            return false;
        }else if(txt_addressee_email.getText().toString().equals("")){
            txt_addressee_email.setError(getActivity().getResources().getText(R.string.error_empty_field).toString());
            txt_addressee_email.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if(validateFields()){

                    saveDataAddressee();
                    dataAddressee.putAll(listener.getDataSenderQuotation());

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dataAddresseeQuotation", (Serializable) dataAddressee);

                    Toast.makeText(getActivity(), "Módulo en desarrollo.", Toast.LENGTH_SHORT).show();

                }
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

            case R.id.imgv_save_frequent:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if(checkIfDatsIsComplete()){
                    if(FrequentlyContacts.checkIfExistContactInDB(getActivity(), getText(txt_addressee_name), getText(txt_business_name), getText(txt_addressee_street),
                            getText(txt_no_ext), getText(txt_no_int), getText(txt_zip_code), getText(txt_addressee_phone), getText(txt_addressee_email), "", "", "")){
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "Ya existe un contacto con esta información.", 3000);
                    }else {
                        FragmentDialogSave fds = new FragmentDialogSave();
                        fds.listener = this;
                        fds.question = FragmentDialogSave.QUESTION.SENDER;
                        fds.show(getActivity().getSupportFragmentManager(), fds.TAG);
                    }
                }else{
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "Debe completar la información para guardar un contacto.", 3000);
                }

                break;
        }
    }

    private String getText(EditText txt_view){

        String text = txt_view.getText().toString();

        return text;
    }


    private void saveDataAddressee(){
        dataAddressee = new HashMap<>();
        dataAddressee.put(FragmentQuotationBuy.DATA_ADDRESSEE_QUOTATION.ADDRESSEE_NAME.toString(),txt_addressee_name.getText().toString());
        dataAddressee.put(FragmentQuotationBuy.DATA_ADDRESSEE_QUOTATION.ADDRESSEE_BUSINESS_NAME.toString(), txt_business_name.getText().toString());
        dataAddressee.put(FragmentQuotationBuy.DATA_ADDRESSEE_QUOTATION.STREET_ADDRESSEE.toString(), txt_addressee_street.getText().toString());
        dataAddressee.put(FragmentQuotationBuy.DATA_ADDRESSEE_QUOTATION.NO_INT_ADDRESSEE.toString(),txt_no_int.getText().toString());
        dataAddressee.put(FragmentQuotationBuy.DATA_ADDRESSEE_QUOTATION.ZIP_CODE_ADDRESSEE.toString(),txt_zip_code.getText().toString());
        dataAddressee.put(FragmentQuotationBuy.DATA_ADDRESSEE_QUOTATION.NO_EXT_ADDRESSEE.toString(),txt_no_ext.getText().toString());
        dataAddressee.put(FragmentQuotationBuy.DATA_ADDRESSEE_QUOTATION.COLONY_ADDRESSEE.toString(),spn_addressee_colony.getSelectedItem().toString());
        dataAddressee.put(FragmentQuotationBuy.DATA_ADDRESSEE_QUOTATION.CITY_ADDRESSEE.toString(),spn_addressee_city.getSelectedItem().toString());
        dataAddressee.put(FragmentQuotationBuy.DATA_ADDRESSEE_QUOTATION.STATE_ADDRESSEE.toString(),spn_addressee_state.getSelectedItem().toString());
        dataAddressee.put(FragmentQuotationBuy.DATA_ADDRESSEE_QUOTATION.PHONE_ADDRESSEE.toString(),txt_addressee_phone.getText().toString());
        dataAddressee.put(FragmentQuotationBuy.DATA_ADDRESSEE_QUOTATION.EMAIL_ADDRESSEE.toString(),txt_addressee_email.getText().toString());

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
                Bundle b = new Bundle();
                b.putString("cp_quote",txt_zip_code.getText().toString());
                ffc.setArguments(b);
                ffc.listener = this;
                ffc.show(getActivity().getSupportFragmentManager(),FRAGMENT_TAG.FRAG_FRECUENTES.toString());
            }else{
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No tienes contactos frecuentes guardados", 3000);
            }
        }

    }

    @Override
    public void pressedButtonContinue(Map<String, String> dataContact) {

        txt_addressee_name.setText(dataContact.get(FrequentlyContacts.NAME_CONTACT));
        txt_business_name.setText(dataContact.get(FrequentlyContacts.BUSINESS_NAME_CONTACT));
        txt_addressee_street.setText(dataContact.get(FrequentlyContacts.STREET_CONTACT));
        txt_no_ext.setText(dataContact.get(FrequentlyContacts.NO_EXT_CONTACT));
        txt_no_int.setText(dataContact.get(FrequentlyContacts.NO_INT_CONTACT));
        txt_addressee_phone.setText(dataContact.get(FrequentlyContacts.PHONE_CONTACT));
        txt_addressee_email.setText(dataContact.get(FrequentlyContacts.EMAIL_CONTACT));

    }

    @Override
    public void buttonSavePressed() {
        FrequentlyContacts.insert(getActivity(), getText(txt_addressee_name), getText(txt_business_name), getText(txt_addressee_street),
                getText(txt_no_ext),getText(txt_no_int),getText(txt_zip_code), getText(txt_addressee_phone), getText(txt_addressee_email), "", "", "");

        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SUCCESS, "El contacto fue guardado con exito", 3000);
    }



}
