package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Buys;
import database.model.States;

/**
 * Created by juan.guerra on 13/07/2015.
 */
public class FragmentComprobanteCompra extends DialogFragment implements View.OnClickListener{

    private View view;
    Context context;

    private TextView
                            remitente,
                            origen,
                            cpo,
                            destinatario,
                            destino,
                            cpD,
                            garantia,
                            costo,
                            referencia,
                            tipo_servicio;

    private Button close;

    private Map<String,String> compra;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comprobante_compra, container, false);

        initvariables();
        Bundle b = getArguments();
        String noReferencia = b.getString(Buys.REFERENCIA);

        Map <String,String> arguments = new HashMap<>();
        arguments.put(Buys.REFERENCIA,noReferencia);


        compra = Buys.getByArguments(getActivity(),arguments).get(0);
        setData();
        return view;
    }

    private void initvariables(){
        context = getActivity();
        remitente = (TextView)view.findViewById(R.id.txt_remitente);
        origen = (TextView)view.findViewById(R.id.txt_origen);
        cpo = (TextView)view.findViewById(R.id.txt_cpO);
        destinatario = (TextView)view.findViewById(R.id.txt_destinatario);
        destino = (TextView)view.findViewById(R.id.txt_destino);
        cpD = (TextView)view.findViewById(R.id.txt_cpD);
        garantia = (TextView)view.findViewById(R.id.txt_garantia);
        costo = (TextView)view.findViewById(R.id.txt_costo);
        referencia = (TextView)view.findViewById(R.id.txt_referencia);
        tipo_servicio = (TextView)view.findViewById(R.id.txt_tipo_servicio);
        close = (Button)view.findViewById(R.id.btn_close);
        close.setOnClickListener(this);
    }


    private void setData(){
        remitente.setText(compra.get(Buys.NOMBRE_REMITENTE));
        origen.setText(compra.get(Buys.ORIGEN));
        cpo.setText(compra.get(Buys.CPO));
        destinatario.setText(compra.get(Buys.NOMBRE_DESTINATARIO));
        destino.setText(compra.get(Buys.DESTINO));
        cpD.setText(compra.get(Buys.CPD));
        garantia.setText(compra.get(Buys.GARANTIA));
        costo.setText(compra.get(Buys.COSTO));
        referencia.setText(compra.get(Buys.REFERENCIA));
        tipo_servicio.setText(compra.get(Buys.TIPO_SERVICIO));
    }


    @Override
    public void onClick(View v) {
        //this.dismiss();
        getDialog().dismiss();
    }
}
