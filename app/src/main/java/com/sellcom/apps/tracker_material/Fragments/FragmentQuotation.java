package com.sellcom.apps.tracker_material.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.SpinnerAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jonathan.vazquez on 25/05/15.
 */
public class FragmentQuotation extends TrackerFragment implements View.OnClickListener{

    public static final String TAG = "FRAG_QUOTATION";


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
                            edt_dummy;

    private ImageButton     btn_zc_ori_search,
                            btn_zc_ori_phonebook,
                            btn_zc_dest_search,
                            btn_zc_dest_phonebook;

    private Button          btn_quote;

    private LinearLayout    ll_for_package;

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
            edt_dummy  = (EditText) view.findViewById(R.id.edt_dummy);

            btn_zc_ori_search       = (ImageButton) view.findViewById(R.id.btn_zc_ori_search);
            btn_zc_ori_phonebook    = (ImageButton) view.findViewById(R.id.btn_zc_ori_phonebook);
            btn_zc_dest_search      = (ImageButton) view.findViewById(R.id.btn_zc_dest_search);
            btn_zc_dest_phonebook   = (ImageButton) view.findViewById(R.id.btn_zc_dest_phonebook);

            btn_quote               = (Button) view.findViewById(R.id.btn_quote);

            ll_for_package      = (LinearLayout) view.findViewById(R.id.ll_for_package);

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

        }
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tbtn_nat:
                if (((ToggleButton) v).isChecked()) {
                    tbtn_inter.setChecked(false);
                }
                else{
                    tbtn_nat.setChecked(true);
                }
                break;

            case R.id.tbtn_inter:
                if (((ToggleButton) v).isChecked()) {
                    tbtn_nat.setChecked(false);
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
                searchZipCode();
                break;

            case R.id.btn_zc_ori_phonebook:
                openPhonebook();
                break;

            case R.id.btn_zc_dest_search:
                searchZipCode();
                break;

            case R.id.btn_zc_dest_phonebook:
                openPhonebook();
                break;

            case R.id.btn_quote:
                requestQuote();
                break;
        }
    }

    private void searchZipCode(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentQuotationSearchZC dialog = new FragmentQuotationSearchZC();
        dialog.show(fragmentManager, FragmentQuotationSearchZC.TAG);

    }

    private void openPhonebook(){


    }

    private void requestQuote(){

    }



}
