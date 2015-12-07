package com.estafeta.estafetamovilv1.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;
import com.estafeta.estafetamovilv1.Utils.Utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by juan.guerra on 08/07/2015
 * @deprecated
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

    private Map<String,Map<String,String>> fromInflate;


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
        STREET_NUMBER ("street_number"),
        DATA ("data");

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
        fromInflate = (Map<String,Map<String,String>>)b.getSerializable(EXTRAS.DATA.toString());
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
        String items[];
        ArrayAdapter<String> adapter;
        switch (version){
            case ORIGIN:
                items = new String[1];
                items[0] = fromInflate.get(FragmentQuotationBuy.ESTADO_ORIGEN).get("0");
                adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, items);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                estados.setAdapter(adapter);
                break;

            case DESTINY:
                items = new String[1];
                items[0] = fromInflate.get(FragmentQuotationBuy.ESTADO_DESTINO).get("0");
                adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, items);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                estados.setAdapter(adapter);
                break;

        }
    }

    private void inflateMunicipios(){
        String items[];
        ArrayAdapter<String> adapter;
        switch (version){
            case ORIGIN:
                items = new String[1];
                items[0] = fromInflate.get(FragmentQuotationBuy.MUNICIPIO_ORIGEN).get("0");
                adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, items);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                municipios.setAdapter(adapter);
                break;

            case DESTINY:
                items = new String[1];
                items[0] = fromInflate.get(FragmentQuotationBuy.MUNICIPIO_DESTINO).get("0");
                adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, items);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                municipios.setAdapter(adapter);
                break;

        }

    }

    private void inflateColonias(){
        String items[];
        ArrayAdapter<String> adapter;
        Map<String,String> cols;
        switch (version){
            case ORIGIN:
                cols = fromInflate.get(FragmentQuotationBuy.COLONIA_ORIGEN);
                items = new String[cols.size()];
                for (int index = 0; index < cols.size(); index++){
                    items[index] = cols.get(String.valueOf(index));
                }
                adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, items);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                colonias.setAdapter(adapter);
                break;

            case DESTINY:
                cols = fromInflate.get(FragmentQuotationBuy.COLONIA_DESTINO);
                items = new String[cols.size()];
                for (int index = 0; index < cols.size(); index++){
                    items[index] = cols.get(String.valueOf(index));
                }
                adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, items);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                colonias.setAdapter(adapter);
                break;

        }

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
        String lastColony = data.get((EXTRAS.COLONY.toString()));
        for (int i = 0; i< colonias.getCount(); i++){
            if (colonias.getItemAtPosition(i).toString().equals(lastColony)){
                colonias.setSelection(i);
                break;
            }
        }





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
        if ( !(Utilities.validateNumber(context, phone.getText().toString(), phone)) )
            return false;
        if ( !(Utilities.validateEmail(context, email.getText().toString(), email)) )
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
