package com.sellcom.apps.tracker_material.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.sellcom.apps.tracker_material.R;

import static com.sellcom.apps.tracker_material.R.color.estafeta_red;

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

    public FragmentDialogHelp() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_help, container, false);

        im_dialog_help = (ImageView) view.findViewById(R.id.im_dialog_help);

        cerrar = (Button)view.findViewById(R.id.btn_cerrar);
        cerrar.setOnClickListener(this);

        btn_cod_guia = (Button)view.findViewById(R.id.btn_guia);
        btn_cod_guia.setOnClickListener(this);

        btn_cod_factura = (Button) view.findViewById(R.id.btn_factura);
        btn_cod_factura.setOnClickListener(this);

        btn_cod_ticket = (Button) view.findViewById(R.id.btn_ticket);
        btn_cod_ticket.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cerrar:
                getDialog().dismiss();
                break;

            case R.id.btn_guia:
                btn_cod_guia.setTextColor(getResources().getColor(R.color.estafeta_red));
                btn_cod_factura.setTextColor(getResources().getColor(R.color.estafeta_text));
                btn_cod_ticket.setTextColor(getResources().getColor(R.color.estafeta_text));
                im_dialog_help.setImageResource(R.drawable.help_guia);
                break;


            case R.id.btn_factura:
                btn_cod_factura.setTextColor(getResources().getColor(R.color.estafeta_red));
                btn_cod_guia.setTextColor(getResources().getColor(R.color.estafeta_text));
                btn_cod_ticket.setTextColor(getResources().getColor(R.color.estafeta_text));
                im_dialog_help.setImageResource(R.drawable.help_factura);
                break;

            case R.id.btn_ticket:
                btn_cod_ticket.setTextColor(getResources().getColor(R.color.estafeta_red));
                btn_cod_factura.setTextColor(getResources().getColor(R.color.estafeta_text));
                btn_cod_guia.setTextColor(getResources().getColor(R.color.estafeta_text));
                im_dialog_help.setImageResource(R.drawable.help_ticket);
                break;

        }

    }
}
