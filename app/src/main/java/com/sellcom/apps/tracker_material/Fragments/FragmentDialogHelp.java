package com.sellcom.apps.tracker_material.Fragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sellcom.apps.tracker_material.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDialogHelp extends DialogFragment implements View.OnClickListener {

    String  TAG = "FRAG_DIALOG_HELP";
    Button cerrar;
    Button btn_cod_guia;
    Button btn_cod_factura;
    Button btn_cod_ticket;
    ImageView im_dialog_help;
    Bundle savedInstanceState;

    int imgType;

    public FragmentDialogHelp() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_NoTitleBar);
        setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_help, container, false);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimations;

        im_dialog_help = (ImageView) view.findViewById(R.id.im_dialog_help);

        cerrar = (Button)view.findViewById(R.id.btn_cerrar);
        cerrar.setOnClickListener(this);

        btn_cod_guia = (Button)view.findViewById(R.id.btn_guia);
        btn_cod_guia.setOnClickListener(this);
        btn_cod_guia.setBackgroundColor(getResources().getColor(R.color.estafeta_soft_gray));

        btn_cod_factura = (Button) view.findViewById(R.id.btn_factura);
        btn_cod_factura.setOnClickListener(this);

        btn_cod_ticket = (Button) view.findViewById(R.id.btn_ticket);
        btn_cod_ticket.setOnClickListener(this);

        selectedButton(imgType);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cerrar:
                imgType = 1;
                getDialog().dismiss();
                break;

            case R.id.btn_guia:
                btn_cod_guia.setBackgroundColor(getResources().getColor(R.color.estafeta_soft_gray));
                btn_cod_factura.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                btn_cod_ticket.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                im_dialog_help.setImageResource(R.drawable.help_guia);
                imgType = 1;
                break;


            case R.id.btn_factura:
                btn_cod_factura.setBackgroundColor(getResources().getColor(R.color.estafeta_soft_gray));
                btn_cod_guia.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                btn_cod_ticket.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                im_dialog_help.setImageResource(R.drawable.help_factura);
                imgType = 2;
                break;

            case R.id.btn_ticket:
                btn_cod_ticket.setBackgroundColor(getResources().getColor(R.color.estafeta_soft_gray));
                btn_cod_factura.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                btn_cod_guia.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                im_dialog_help.setImageResource(R.drawable.help_ticket);
                imgType = 3;
                break;

        }

    }

    public void selectedButton(int button){

        switch (button){
            case 1:
                btn_cod_guia.setBackgroundColor(getResources().getColor(R.color.estafeta_soft_gray));
                btn_cod_factura.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                btn_cod_ticket.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                im_dialog_help.setImageResource(R.drawable.help_guia);
                break;
            case 2:
                btn_cod_factura.setBackgroundColor(getResources().getColor(R.color.estafeta_soft_gray));
                btn_cod_guia.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                btn_cod_ticket.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                im_dialog_help.setImageResource(R.drawable.help_factura);
                break;
            case 3:
                btn_cod_ticket.setBackgroundColor(getResources().getColor(R.color.estafeta_soft_gray));
                btn_cod_factura.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                btn_cod_guia.setBackgroundColor(getResources().getColor(R.color.dialog_help));
                im_dialog_help.setImageResource(R.drawable.help_ticket);
                break;
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        savedInstanceState = new Bundle();

        ViewGroup viewGroup = (ViewGroup) getView();
        viewGroup.removeAllViewsInLayout();
        View view = onCreateView(getActivity().getLayoutInflater(), viewGroup, savedInstanceState);
        viewGroup.addView(view);

    }


}
