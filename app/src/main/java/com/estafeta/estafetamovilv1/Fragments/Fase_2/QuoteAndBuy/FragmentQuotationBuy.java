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

    private Map<String,String>  dataQuotation;

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

            lbl_total_coste.setText(dataQuotation.get("total_coste")+" mxn");

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

}
