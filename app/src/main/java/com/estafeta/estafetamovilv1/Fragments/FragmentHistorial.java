package com.estafeta.estafetamovilv1.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.estafeta.estafetamovilv1.Adapters.HistorialOperacionesListAdapter;
import com.estafeta.estafetamovilv1.Async_Request.METHOD;
import com.estafeta.estafetamovilv1.Async_Request.RequestManager;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.Map;

import database.model.Buys;

/**
 * Created by juan.guerra on 14/07/2015.
 * @deprecated
 */
public class FragmentHistorial  extends TrackerFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View view;

    Context context;

    private ListView listv;

    private ToggleButton tbtn_prellenados;
    private ToggleButton tbtn_compras;
    private ArrayList<Map<String,String>> values;

    private FragmentManager fragmentManager;
    private HistorialOperacionesListAdapter lstAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_operations, container, false);
        context = getActivity();
        initVariables();

        return view;
    }

    public void initVariables(){
        listv = (ListView) view.findViewById(R.id.listV);
        tbtn_compras = (ToggleButton)view.findViewById(R.id.tbtn_compras);
        tbtn_prellenados = (ToggleButton)view.findViewById(R.id.tbtn_prellenados);

        tbtn_compras.setOnClickListener(this);
        tbtn_prellenados.setOnClickListener(this);

        tbtn_prellenados.performClick();
        listv.setOnItemClickListener(this);


    }

    private void initOriginData(){

        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando), 0);
        RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE_ADDRESSES, null,this);


    }




    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }





    public void handleBackPressed(){

    }



    @Override
    public void decodeResponse(String response){



    }



    @Override
    public void onClick(View v) {
        boolean isChecked;
        isChecked = ((ToggleButton)v).isChecked();
        switch (v.getId()){
        case R.id.tbtn_compras:
        if (isChecked){
        tbtn_prellenados.setChecked(false);
            llenarComprados();
        }
        else
            tbtn_compras.setChecked(true);
        break;

        case R.id.tbtn_prellenados:
        if (isChecked){
        tbtn_compras.setChecked(false);
            llenarPrellenados();
        }

        else
        tbtn_prellenados.setChecked(true);
        break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("fdjnvr","CLICKED");
        Map<String,String> element = (Map<String, String>) lstAdapter.getItem(position);
        if (tbtn_compras.isChecked()){//Click a prellenados
            showDialogComprado(element.get(Buys.REFERENCIA));
        }
        else{
            //showDialogPrellenado(element.get(Buys.REFERENCIA));
        }

    }
    public void llenarPrellenados(){
        //values = (ArrayList<Map<String, String>>) Buys.getAll(context);
        //lstAdapter = new HistorialOperacionesListAdapter(context,values,HistorialOperacionesListAdapter.PRELLENADOS);
        //listv.setAdapter(lstAdapter);
        //listv.setOnItemClickListener(this);

    }

    public void llenarComprados(){
        values = (ArrayList<Map<String, String>>) Buys.getAll(context);
        lstAdapter = new HistorialOperacionesListAdapter(context,values,HistorialOperacionesListAdapter.COMPRAS);
        listv.setAdapter(lstAdapter);


    }

    public void showDialogComprado(String parameter){
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentComprobanteCompra fcc = new FragmentComprobanteCompra();
        Bundle b = new Bundle();
        b.putString(Buys.REFERENCIA,parameter);
        fcc.setArguments(b);
        fcc.show(fragmentManager,"FRAG_DIALOG_CP");
    }

    public void showDialogPrellenado(String parameter){

    }

}