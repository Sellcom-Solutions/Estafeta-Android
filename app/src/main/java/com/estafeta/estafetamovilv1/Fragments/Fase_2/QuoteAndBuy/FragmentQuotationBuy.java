package com.estafeta.estafetamovilv1.Fragments.Fase_2.QuoteAndBuy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;
import com.estafeta.estafetamovilv1.Utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentQuotationBuy extends TrackerFragment {

    public static final String TAG = "FRAG_QUOTATION_BUY";

    private TrackerFragment     fragment;

    private TextView    footer,
                        lbl_origin,
                        lbl_destiny,
                        lbl_total_coste;

    private Map<String,String>  dataSender,
                                dataQuotation;

    public enum DATA_SENDER_QUOTATION {
        SENDER_NAME("sender_name"),
        BUSINESS_NAME("business_name"),
        STREET_SENDER("street_sender"),
        NO_EXT_SENDER("no_ext_sender"),
        NO_INT_SENDER("no_int_sender"),
        ZIP_CODE_SENDER("zip_code_sender"),
        COLONY_SENDER("colony_sender"),
        CITY_SENDER("city_sender"),
        STATE_SENDER("state_sender"),
        PHONE_SENDER("phone_sender"),
        EMAIL_SENDER("email_sender");

        private final String name;

        private DATA_SENDER_QUOTATION(String s) {
            name = s;
        }

        public String toString(){
            return name;
        }
    }

    public enum DATA_ADDRESSEE_QUOTATION {
        ADDRESSEE_NAME("addressee_name"),
        ADDRESSEE_BUSINESS_NAME("addressee_business_name"),
        STREET_ADDRESSEE("street_addressee"),
        NO_EXT_ADDRESSEE("no_ext_addressee"),
        NO_INT_ADDRESSEE("no_int_addressee"),
        ZIP_CODE_ADDRESSEE("zip_code_addressee"),
        COLONY_ADDRESSEE("colony_addressee"),
        CITY_ADDRESSEE("city_addressee"),
        STATE_ADDRESSEE("state_addressee"),
        PHONE_ADDRESSEE("phone_addressee"),
        EMAIL_ADDRESSEE("email_addressee");

        private final String name;

        private DATA_ADDRESSEE_QUOTATION(String s) {
            name = s;
        }

        public String toString(){
            return name;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().getSerializable("dataQuotation") != null){
            dataQuotation = (Map<String,String>) getArguments().getSerializable("dataQuotation");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quotation_buy, container, false);
        if(view != null){

            lbl_origin      = (TextView) view.findViewById(R.id.lbl_origin);
            lbl_destiny     = (TextView) view.findViewById(R.id.lbl_destiny);
            lbl_total_coste = (TextView) view.findViewById(R.id.lbl_total_coste);

            lbl_origin.setText(dataQuotation.get("cp_origin")+", "
                    + dataQuotation.get("city_origin")+", "+dataQuotation.get("state_origin"));

            lbl_destiny.setText(dataQuotation.get("cp_destiny")+", "
                    + dataQuotation.get("city_destiny")+", "+dataQuotation.get("state_destiny"));

            Double totalNum = Double.parseDouble(dataQuotation.get("total_coste"));
            totalNum = totalNum + (totalNum * 0.15);

            lbl_total_coste.setText(Utilities.setReceiptMoneyNumberFormat(totalNum, 2)+" mxn");

            footer                          = (TextView)view.findViewById(R.id.footer);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            String currentYear = formatter.format(new Date());
            footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));


            fragment = new FragmentQuotationBuySender();
            Bundle b = new Bundle();
            b.putString("cp_origin",dataQuotation.get("cp_origin"));
            b.putString("cp_destiny",dataQuotation.get("cp_destiny"));
            fragment.setArguments(b);
            addFragmentQuotationBuyToStack(getActivity(), fragment, FRAGMENT_TAG.FRAG_REMITENTE_QUOTATION_BUY.toString(), false);


        }
        return view;
    }

    public Map<String, String> getDataSenderQuotation() {
        return dataSender;
    }

    public void setDataSenderQuotation(Map<String, String> dataSender) {
        this.dataSender = dataSender;
    }

}
