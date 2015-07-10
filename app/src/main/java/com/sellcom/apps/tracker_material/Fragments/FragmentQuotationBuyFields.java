package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.Adapters.OnCardClickListener;
import com.sellcom.apps.tracker_material.Adapters.SpinnerAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.model.States;

/**
 * Created by juan.guerra on 08/07/2015.
 */
public class FragmentQuotationBuyFields extends TrackerFragment{

    private View view;
    Context context;

    private Spinner estados;
    private Spinner municipios;
    private Spinner colonias;
    private int version;

    private EditText name;
    private EditText phone;
    private EditText email;
    private EditText bussines_name;
    private EditText street;
    private EditText number;

    private Button next;

    private String CP;
    private Map<String,String> data;
    public static final int ORIGIN = 0;
    public static final int DESTINY = 1;


    public static enum EXTRAS{
        NAME ("name"),
        PHONE ("phone"),
        EMAIL ("email"),
        BUSSINESS_NAME ("bussiness_name"),
        STATE ("state"),
        DISTRICT ("district"),
        COLONY ("colony"),
        STREET ("street"),
        VERSION ("version"),
        STREET_NUMBER ("street_number");
        private final String name;
        private EXTRAS(String s) {
            name = s;
        }
        public boolean equalsName(String otherName){ return (otherName == null)? false:name.equals(otherName); }
        public String toString(){ return name; }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quotation_buy_fields, container, false);
        initvariables();
        Bundle b = getArguments();
        CP = b.getString(FragmentQuotationBuy.EXTRAS.CP.toString());
        version = b.getInt(EXTRAS.VERSION.toString());
        data = (Map<String, String>) b.getSerializable(FragmentQuotationBuy.EXTRAS.DATOS.toString());
        setParametersToVariables();
        setPreviousData();
        return view;
    }

    private void initvariables(){
        //Spinners
        context = getActivity();
        estados = (Spinner)view.findViewById(R.id.spn_state_quotation);
        municipios = (Spinner)view.findViewById(R.id.spn_district_quotation);
        colonias = (Spinner)view.findViewById(R.id.spn_colony_quotation);

        //EditTexts
        name = (EditText)view.findViewById(R.id.edt_sender_name);
        phone = (EditText)view.findViewById(R.id.edt_phone_number);
        email = (EditText)view.findViewById(R.id.edt_email);
        bussines_name = (EditText)view.findViewById(R.id.edt_bussines_name);
        street = (EditText)view.findViewById(R.id.edt_street);
        number = (EditText)view.findViewById(R.id.edt_number_origin);

        //Button
        next = (Button)view.findViewById(R.id.btn_next);
        next.setOnClickListener((View.OnClickListener) getParentFragment());

    }

    private void setParametersToVariables(){
        inflarSpinners();
        setHintToEditTexts();

    }

    private void inflarSpinners(){
        inflateEstados();
        inflateMunicipios();
        inflateColonias();
    }

    private void inflateEstados(){
        ArrayList<Map<String,String>> states = new ArrayList<Map<String,String>>();
        ArrayList<Map<String,String>> auxStates = States.getStatesNames(context);
        Map<String,String> mapa  = new HashMap<String,String>();
        String items[] = new String[auxStates.size()];
        states.add(mapa);
        Map<String,String> auxMapa;
        for(int i = 0; i<auxStates.size(); i++){
            auxMapa  = new HashMap<String,String>();
            auxMapa.put("ZNOMBRE",auxStates.get(i).get("ZNOMBRE"));
            items[i] = auxStates.get(i).get("ZNOMBRE");
            states.add(auxMapa);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        estados.setAdapter(adapter);
    }

    private void inflateMunicipios(){
        ArrayList<Map<String,String>> states = new ArrayList<Map<String,String>>();
        ArrayList<Map<String,String>> auxStates = States.getStatesNames(context);
        Map<String,String> mapa  = new HashMap<String,String>();
        String items[] = new String[auxStates.size()];
        states.add(mapa);
        Map<String,String> auxMapa;
        for(int i = 0; i<auxStates.size(); i++){
            auxMapa  = new HashMap<String,String>();
            auxMapa.put("ZNOMBRE",auxStates.get(i).get("ZNOMBRE"));
            items[i] = auxStates.get(i).get("ZNOMBRE");
            states.add(auxMapa);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        municipios.setAdapter(adapter);

    }

    private void inflateColonias(){
        ArrayList<Map<String,String>> states = new ArrayList<Map<String,String>>();
        ArrayList<Map<String,String>> auxStates = States.getStatesNames(context);
        Map<String,String> mapa  = new HashMap<String,String>();
        String items[] = new String[auxStates.size()];
        states.add(mapa);
        Map<String,String> auxMapa;
        for(int i = 0; i<auxStates.size(); i++){
            auxMapa  = new HashMap<String,String>();
            auxMapa.put("ZNOMBRE",auxStates.get(i).get("ZNOMBRE"));
            items[i] = auxStates.get(i).get("ZNOMBRE");
            states.add(auxMapa);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        colonias.setAdapter(adapter);
    }

    private void setHintToEditTexts(){
        switch (version){
            case ORIGIN:
                name.setHint(R.string.sender_name);
                next.setText(R.string.next);
                break;

            case DESTINY:
                name.setHint(R.string.receiver_name);
                next.setText(R.string.continue_buy);
                break;
        }
    }

    private void setPreviousData(){
        if (data == null)
            return;
        name.setText(data.get(EXTRAS.NAME.toString()));
        phone.setText(data.get(EXTRAS.PHONE.toString()));
        email.setText(data.get(EXTRAS.EMAIL.toString()));
        bussines_name.setText(data.get(EXTRAS.BUSSINESS_NAME.toString()));
        street.setText(data.get(EXTRAS.STREET.toString()));
        number.setText(data.get(EXTRAS.STREET_NUMBER.toString()));
    }



    public Map<String,String> getData(){
        data = new HashMap<String,String>();
        data.put(EXTRAS.NAME.toString(),name.getText().toString());
        data.put(EXTRAS.PHONE.toString(),phone.getText().toString());
        data.put(EXTRAS.EMAIL.toString(),email.getText().toString());
        data.put(EXTRAS.BUSSINESS_NAME.toString(),bussines_name.getText().toString());
        data.put(EXTRAS.STATE.toString(), estados.getSelectedItem().toString());
        data.put(EXTRAS.DISTRICT.toString(),municipios.getSelectedItem().toString());
        data.put(EXTRAS.COLONY.toString(),colonias.getSelectedItem().toString());
        data.put(EXTRAS.STREET.toString(),street.getText().toString());
        data.put(EXTRAS.STREET_NUMBER.toString(),number.getText().toString());
        data.put(EXTRAS.STATE.toString(),estados.getSelectedItem().toString());
        data.put(EXTRAS.DISTRICT.toString(),municipios.getSelectedItem().toString());
        data.put(EXTRAS.COLONY.toString(),colonias.getSelectedItem().toString());
        return data;
    }


    public boolean validateData(){
        if ( !(Utilities.validateCommonField(context, name.getText().toString(), name)) )
            return false;
        if ( !(Utilities.validateCommonField(context, phone.getText().toString(), phone)) )
            return false;
        if ( !(Utilities.validateCommonField(context, email.getText().toString(), email)) )
            return false;
        if ( !(Utilities.validateCommonField(context, bussines_name.getText().toString(), bussines_name)) )
            return false;
        if ( !(Utilities.validateCommonField(context, street.getText().toString(), street)) )
            return false;
        if ( !(Utilities.validateCommonField(context, number.getText().toString(), number)) )
            return false;
        return true;
    }

    public int getVersion() { //0 -> Origen, 1 -> Destino
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


}
