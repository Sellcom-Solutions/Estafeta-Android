package com.sellcom.apps.tracker_material.Fragments;


import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.content.Context;


import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.RastreoListAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rebecalopezmartinez on 21/05/15.
 */
public class FragmentRastreo extends TrackerFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    String TAG= "FRAG_RASTREO";

    private FragmentManager         fragmentManager;
    private FragmentTransaction     fragmentTransaction;
    private TrackerFragment         fragment;

    ImageButton                     info;
    Button                          rastreo;
    Button                          escanear;
    ImageButton                     agregar;
    EditText                        codigo;

    ListView lst_rastreo;
    RastreoListAdapter lstAdapter;
    ArrayList<Map<String,String>> codes_array;
    Context context;

    public FragmentRastreo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_rastreo, container, false);

        info        = (ImageButton)view.findViewById(R.id.btn_help);
        rastreo     = (Button)view.findViewById(R.id.btn_rastrear);
        escanear    = (Button) view.findViewById(R.id.btn_escanear);
        agregar     = (ImageButton) view.findViewById(R.id.btn_agregar);
        lst_rastreo = (ListView)view.findViewById(R.id.liv_rastreo);
        codigo      = (EditText)view.findViewById(R.id.edt_codigo);


       codes_array              = new ArrayList<Map<String,String>>();
        //String [] values = {"prueba","prueba2","prueba3"};*/


        /*lstAdapter = new RastreoListAdapter(getActivity(),values);
        lst_rastreo.setAdapter(lstAdapter);
        lst_rastreo.setOnItemClickListener(this);*/

        info.setOnClickListener(this);
        escanear.setOnClickListener(this);
        rastreo.setOnClickListener(this);
        agregar.setOnClickListener(this);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (!((MainActivity) getActivity()).isDrawerOpen) {
            menu.clear();
            inflater.inflate(R.menu.rastreo, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "Selected:  " + item.getItemId());

        switch (item.getItemId()) {
            case R.id.add_favorite:
                Log.d(TAG,"Agregar a favoritos");

                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_help:
                fragmentManager = getActivity().getSupportFragmentManager();
                FragmentDialogHelp fdh = new FragmentDialogHelp();
                fdh.show(fragmentManager,"FRAG_DIALOG_HELP");
                break;

            case R.id.btn_escanear:
                break;

            case R.id.btn_rastrear:
                Bundle bundle= new Bundle();
                fragmentManager =getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new FragmentRastreoEfectuado();
                fragment.addFragmentToStack(getActivity());
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, fragment, TAG);
                fragmentTransaction.commit();
                break;

            case R.id.btn_agregar:
                addCode();
                break;
        }

    }
    public void addCode(){
        String codigoStr = codigo.getText().toString();

        if(codigoStr.length()== 10 || codigoStr.length()== 22){
            if(codes_array.size()>0) {
                if (codes_array.size() <= 9){
                    for (int i = 0; i < codes_array.size(); i++) {
                        Map<String,String> aux = new HashMap<>();
                        aux = codes_array.get(i);
                        String c = aux.get("codigo");
                        if(c.equals(codigoStr)){
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.error_cod_repetido));
                            Handler handler = null;
                            handler = new Handler();
                            handler.postDelayed(new Runnable(){
                                public void run(){
                                    DialogManager.sharedInstance().dismissDialog();
                                }
                            }, 1000);
                            return;
                        }

                    }
                }
                else {
                    agregar.setEnabled(false);
                    codigo.setEnabled(false);
                    escanear.setEnabled(false);
                }
            }

            Map<String,String> item = new HashMap<>();
            item.put("codigo",codigoStr);
            item.put("favorito","0");
            codes_array.add(item);

            lstAdapter = new RastreoListAdapter(getActivity(),codes_array);
            lst_rastreo.setAdapter(lstAdapter);
            lst_rastreo.setOnItemClickListener(this);
        }
        else {
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.error_tamaño_codigo));
            Handler handler = null;
            handler = new Handler();
            handler.postDelayed(new Runnable(){
                public void run(){
                    DialogManager.sharedInstance().dismissDialog();
                }
            }, 1000);

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
