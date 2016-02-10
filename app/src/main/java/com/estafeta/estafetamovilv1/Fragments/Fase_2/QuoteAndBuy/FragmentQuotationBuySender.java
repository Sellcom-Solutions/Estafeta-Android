package com.estafeta.estafetamovilv1.Fragments.Fase_2.QuoteAndBuy;


import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.estafeta.estafetamovilv1.Adapters.SpinnerAdapterPrefilled;
import com.estafeta.estafetamovilv1.Async_Request.METHOD;
import com.estafeta.estafetamovilv1.Async_Request.RequestManager;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;
import com.estafeta.estafetamovilv1.Utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentQuotationBuySender extends TrackerFragment {

    private EditText        txt_sender_name,
                            txt_business_name,
                            txt_sender_street,
                            txt_no_ext,
                            txt_no_int,
                            txt_zip_code,
                            txt_sender_phone,
                            txt_sender_email,
                            txt_city,
                            txt_colony;


    private ImageView       imgv_save_frequent,
                            imgv_diary;

    private Spinner         spn_sender_colony,
                            spn_sender_city,
                            spn_sender_state;


    private List<String>    items_list_colony,
                            items_list_city,
                            items_list_state;

    private SpinnerAdapterPrefilled     spinnerAdapterColony,
                                        spinnerAdapterCity,
                                        spinnerAdapterState;

    private String          typeSend = "",
                            zipCodeString,
                            cp_origin = "";

    private List<Map<String,String>> respCotizador;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().getString("cp_origin") != null){
            cp_origin = getArguments().getString("cp_origin");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quotation_buy_sender, container, false);
        if(view != null) {

            //ImageView
            imgv_save_frequent = (ImageView) view.findViewById(R.id.imgv_save_frequent);
            imgv_diary = (ImageView) view.findViewById(R.id.imgv_diary);
            //EditText
            txt_sender_name = (EditText) view.findViewById(R.id.txt_sender_name);
            txt_business_name = (EditText) view.findViewById(R.id.txt_business_name);
            txt_sender_street = (EditText) view.findViewById(R.id.txt_sender_street);
            txt_no_ext = (EditText) view.findViewById(R.id.txt_no_ext);
            txt_no_int = (EditText) view.findViewById(R.id.txt_no_int);
            txt_zip_code = (EditText) view.findViewById(R.id.txt_zip_code);
            txt_sender_phone = (EditText) view.findViewById(R.id.txt_sender_phone);
            txt_sender_email = (EditText) view.findViewById(R.id.txt_sender_email);
            //Spinners
            spn_sender_colony = (Spinner) view.findViewById(R.id.spn_sender_colony);
            spn_sender_city = (Spinner) view.findViewById(R.id.spn_sender_city);
            spn_sender_state = (Spinner) view.findViewById(R.id.spn_sender_state);

            items_list_colony = new ArrayList<>();
            items_list_colony.add("Colonia*");
            spinnerAdapterColony = new SpinnerAdapterPrefilled(getActivity(), items_list_colony);
            spn_sender_colony.setAdapter(spinnerAdapterColony);

            items_list_city = new ArrayList<>();
            items_list_city.add("Ciudad, municipio, delegación*");
            spinnerAdapterCity = new SpinnerAdapterPrefilled(getActivity(), items_list_city);
            spn_sender_city.setAdapter(spinnerAdapterCity);

            items_list_state = new ArrayList<>();
            items_list_state.add("Estado*");
            spinnerAdapterState = new SpinnerAdapterPrefilled(getActivity(), items_list_state);
            spn_sender_state.setAdapter(spinnerAdapterState);

            //Set Hints
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_name), txt_sender_name);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_business_name), txt_business_name);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_street), txt_sender_street);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_no_ext), txt_no_ext);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_phone), txt_sender_phone);
            Utilities.setCustomHint(getActivity(), getString(R.string.prefilled_email), txt_sender_email);

            imgv_save_frequent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try {
                SVG svg = SVG.getFromResource(getActivity(), R.raw.guardar_gris);
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                imgv_save_frequent.setImageDrawable(drawable);
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
                                RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE_ADDRESSES, requestData, FragmentQuotationBuySender.this);

                            }

                }
            }
        });

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
                            spn_sender_colony.setSelection(1);
                        }else if(items_list_colony.size() == 1){
                            spn_sender_colony.setSelection(0);
                        }

                        items_list_city.removeAll(items_list_city);
                        items_list_city.add("Ciudad, municipio, delegación*");
                        items_list_city.add(resp.get(0).get("ciudad"));
                        spinnerAdapterCity.notifyDataSetChanged();
                        /*SpinnerAdapterPrefilled spinnerAdapterCity = new SpinnerAdapterPrefilled(getActivity(), items_list_city);
                        spn_sender_city.setAdapter(spinnerAdapterCity);*/
                        if(items_list_city.size() > 1) {
                            spn_sender_city.setSelection(1);
                        }else if(items_list_city.size() == 1){
                            spn_sender_city.setSelection(0);
                        }

                        items_list_state.removeAll(items_list_state);
                        items_list_state.add("Estado*");
                        items_list_state.add(resp.get(0).get("estado").trim());
                        spinnerAdapterState.notifyDataSetChanged();
                        /*SpinnerAdapterPrefilled spinnerAdapterState = new SpinnerAdapterPrefilled(getActivity(), items_list_state);
                        spn_sender_state.setAdapter(spinnerAdapterState);*/
                        if(items_list_state.size() > 1) {
                            spn_sender_state.setSelection(1);
                        }else if(items_list_state.size() == 1){
                            spn_sender_state.setSelection(0);
                        }


                        try {
                            DialogManager.sharedInstance().dismissDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }else {
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio), 3000);
                        getActivity().onBackPressed();
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
                    return;
                }
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_service), 3000);
                getActivity().onBackPressed();
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
                return;
            }
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio1), 3000);
            getActivity().onBackPressed();
            Log.v("FragmentQuotation", "respCotizador es igual a null");
        }
    }

}
