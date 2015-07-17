package com.sellcom.apps.tracker_material.Fragments;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anel 01/07/2015.
 */
public class FragmentDialogFavorite  extends TrackerFragment implements View.OnClickListener {

    String TAG = "FRAG_DIALOG_FAVORITE";

    Context context;

    TextView fav_no_guia;
    TextView fav_codigo;
    TextView fav_reference;
    TextView fav_origen;
    TextView fav_destino;
    TextView fav_cp_destino;
    TextView fav_estatus;
    TextView fav_fecha;
    TextView fav_recibio;

    Map<String, String> data = new HashMap<>();
    Map<String, String> codes_info = new HashMap<>();

    TrackerFragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        //setCancelable(false);
        fragmentManager = getActivity().getSupportFragmentManager();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_dialog_favorite, container, false);

        fav_reference = (TextView) view.findViewById(R.id.fav_reference);
        fav_no_guia = (TextView) view.findViewById(R.id.fav_no_guia);
        fav_codigo = (TextView) view.findViewById(R.id.fav_cod_rastreo);
        fav_origen = (TextView) view.findViewById(R.id.fav_origen);
        fav_destino = (TextView) view.findViewById(R.id.fav_destino);
        fav_cp_destino = (TextView) view.findViewById(R.id.fav_cp_destino);
        fav_estatus = (TextView) view.findViewById(R.id.fav_estatus);
        fav_fecha = (TextView) view.findViewById(R.id.fav_fecha);
        fav_recibio = (TextView) view.findViewById(R.id.fav_recibio);


        final FloatingActionButton btn_call = (FloatingActionButton) view.findViewById(R.id.btn_fav_call);
        final FloatingActionButton btn_share = (FloatingActionButton) view.findViewById(R.id.btn_fav_share);

        btn_call.setOnClickListener(this);
        btn_share.setOnClickListener(this);


        codes_info = (Map<String, String>) getArguments().getSerializable("code_array");
        Log.d(TAG, "size: " + codes_info.size());


        if(!(codes_info.get("referencia") == null)){
            fav_reference.setText(" " + codes_info.get("referencia"));
        }

        fav_no_guia.setText(" "+codes_info.get("no_guia"));
        fav_codigo.setText(" "+codes_info.get("codigo_rastreo"));
        fav_origen.setText(" "+codes_info.get("origen" ));
        fav_destino.setText(" "+codes_info.get("destino"));
        fav_cp_destino.setText(" "+codes_info.get("cp_destino"));
        fav_estatus.setText(" "+codes_info.get("estatus"));
        fav_fecha.setText(codes_info.get("fechaHoraEntrega"));
        fav_recibio.setText(" "+codes_info.get("recibio"));

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {

        }
        setHasOptionsMenu(true);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (!((MainActivity) getActivity()).isDrawerOpen) {
            menu.clear();
            inflater.inflate(R.menu.menu_history, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "Selected:  " + item.getItemId());
        switch (item.getItemId()) {

            case R.id.add_history:

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("codes_info", (java.io.Serializable) codes_info);
                    bundle.putString("origin","detalle_favorito");

                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragment = new FragmentHistory();
                    fragment.addFragmentToStack(getActivity());
                    fragment.setArguments(bundle);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.container, fragment, TAG);
                    fragmentTransaction.commit();

                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fav_call:
                Log.d(TAG, "Action Call");
                String phoneNumber = "018003782338";
                onClickTelefono(phoneNumber);
                Log.d("Cancel", "" + getId());
                break;

            case R.id.btn_fav_share:
                Log.d("SHARE", "" + getId());

                Log.d(TAG, "Action Share");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String sendText = "Referencia: "+ fav_reference.getText()+". "
                        +"No.Guía: "+ fav_no_guia.getText()+". "
                        +"Código Rastreo: "+fav_codigo.getText()+". "
                        +"Estatus: "+fav_estatus.getText()+". "
                        +"Origen: "+fav_origen.getText()+". "
                        +"Destino: "+fav_destino.getText()+". "
                        +"CP: "+fav_cp_destino.getText()+". "
                        +"Fecha: "+fav_fecha.getText()+". "
                        +"Recibio: "+fav_recibio.getText()+". ";

          //      sendIntent.putExtra(Intent.EXTRA_SUBJECT, sendText);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }

    }

    public void onClickTelefono(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
