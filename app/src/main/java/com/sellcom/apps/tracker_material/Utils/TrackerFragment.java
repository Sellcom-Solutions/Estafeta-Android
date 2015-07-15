package com.sellcom.apps.tracker_material.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Spinner;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.SpinnerAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.model.Codes;
import database.model.Countries;
import database.model.States;

public class TrackerFragment extends Fragment implements UIResponseListenerInterface {
    public boolean  isFromDrawer    = true;
    public static int      section_index   = 0;
    public String   tag             = "";

    public SpinnerAdapter   spinnerAdapter,
                            spinnerAdapterCountries;

    public enum FRAGMENT_TAG {
        FRAG_RASTREO("rastreo"),
        FRAG_RASTREO_EFECTUADO("rastreo_efectuado"),
        FRAG_RASTREO_DIALOGO("rastreo_dialogo"),
        FRAG_HISTORY("history"),
        FRAG_FAVORITES("favorites"),
        FRAG_DIALOG_FAVORITE_EDIT("dialog_favorites_edit"),
        FRAG_DIALOG_FAVORITE("dialog_favorites"),
        FRAG_OFFICES ("officinas"),
        FRAG_CODIGO_POSTAL("codigo_postal"),
        FRAG_QUOTATION("cotizador"),
        FRAG_AVISO_PRIVACIDAD("aviso_privacidad"),
        FRAG_HISTORIAL("historial");

        private final String name;

        private FRAGMENT_TAG(String s) {
            name = s;
        }

        public boolean equalsName(String otherName){
            return (otherName == null)? false:name.equals(otherName);
        }
        public String toString(){
            return name;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(section_index);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).onSectionAttached(section_index);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {
        RequestManager.sharedInstance().setListener(this);
        //RequestManager.sharedInstance().makeRequestWithDataAndMethodIncludeCredentials(params, method, includeCredentials);
    }

    @Override
    public void decodeResponse(String stringResponse) {

    }


    public void addFragmentToStack(Activity activity){

        ((MainActivity) activity).incrementDepthCounter();

    }


    //created by jose luis at 27/05/2015
    public void setStatesToSpinner(Spinner spinner, Context context) {
        //get the states from database

        ArrayList<Map<String,String>> states = new ArrayList<Map<String,String>>();
        ArrayList<Map<String,String>> auxStates = States.getStatesNames(context);

        Map<String,String> mapa  = new HashMap<String,String>();
        mapa.put("ZNOMBRE","Estado*");
        states.add(mapa);


        Map<String,String> auxMapa;
        for(int i = 0; i<auxStates.size(); i++){

            auxMapa  = new HashMap<String,String>();
            auxMapa.put("ZNOMBRE",auxStates.get(i).get("ZNOMBRE"));
            states.add(auxMapa);

        }


        //set the spinnerAdapter with the values states
        spinnerAdapter = new SpinnerAdapter(getActivity(), states, SpinnerAdapter.SPINNER_TYPE.STATES);
        spinner.setAdapter(spinnerAdapter);

    }

    //created by hugo.figueroa at 16/06/2015
    public void setCountriesToSpinner(Spinner spinner, Context context) {
        //get the states from database

        ArrayList<Map<String,String>> countries = new ArrayList<Map<String,String>>();
        ArrayList<Map<String,String>> auxCountries = Countries.getCountriesNames(context);

        Map<String,String> mapa  = new HashMap<String,String>();
        mapa.put("NOMBREPAIS_ESP","País Destino*");
        countries.add(mapa);


        Map<String,String> auxMapa;
        for(int i = 0; i<auxCountries.size(); i++){

            auxMapa  = new HashMap<String,String>();
            auxMapa.put("NOMBREPAIS_ESP",auxCountries.get(i).get("nombrepais_esp"));
            countries.add(auxMapa);

        }


        //set the spinnerAdapter with the values states
        spinnerAdapterCountries = new SpinnerAdapter(getActivity(), countries, SpinnerAdapter.SPINNER_TYPE.COUNTRIES);
        spinner.setAdapter(spinnerAdapterCountries);

    }

    public static String convertNonAscii(String s) {
        final String UNICODE =
                "\u00C0\u00E0\u00C8\u00E8\u00CC\u00EC\u00D2\u00F2\u00D9\u00F9"
                        + "\u00C1\u00E1\u00C9\u00E9\u00CD\u00ED\u00D3\u00F3\u00DA\u00FA\u00DD\u00FD"
                        + "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177"
                        + "\u00C3\u00E3\u00D5\u00F5\u00D1\u00F1"
                        + "\u00C4\u00E4\u00CB\u00EB\u00CF\u00EF\u00D6\u00F6\u00DC\u00FC\u0178\u00FF"
                        + "\u00C5\u00E5"
                        + "\u00C7\u00E7"
                        + "\u0150\u0151\u0170\u0171"
                ;
        final String PLAIN_ASCII =
                "AaEeIiOoUu"    // grave
                        + "AaEeIiOoUuYy"  // acute
                        + "AaEeIiOoUuYy"  // circumflex
                        + "AaOoNn"        // tilde
                        + "AaEeIiOoUuYy"  // umlaut
                        + "Aa"            // ring
                        + "Cc"            // cedilla
                        + "OoUu"          // double acute
                ;

        if (s == null) return null;
        StringBuffer sb = new StringBuffer();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            int pos = UNICODE.indexOf(c);
            if (pos > -1){
                sb.append(PLAIN_ASCII.charAt(pos));
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
