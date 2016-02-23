package com.estafeta.estafetamovilv1.Fragments.Fase_2.QuoteAndBuy;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estafeta.estafetamovilv1.R;

import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 */
public class FragmentDialogWrongData extends DialogFragment {

    public static final String TAG = "FragmentDialogWrongData";


    private Button      btn_accept;
    private TextView    lbl_wrong_code;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_wrong_data, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(view != null){

            btn_accept      = (Button)view.findViewById(R.id.btn_accept);
            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            lbl_wrong_code  = (TextView)view.findViewById(R.id.lbl_wrong_code);
            if (getArguments() != null && getArguments().getString("cp_quote") != null)
                lbl_wrong_code.setText(Html.fromHtml(getString(R.string.wrong_code1) + " <b>"+getArguments().getString("cp_quote")+"</b>"+getString(R.string.wrong_code2)));
            else
                lbl_wrong_code.setText(Html.fromHtml(getString(R.string.wrong_code1) +getString(R.string.wrong_code2)));

        }
        return view;
    }

}
