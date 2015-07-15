package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sellcom.apps.tracker_material.Adapters.FavoriteListAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.Map;

import database.model.Favorites;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFavorites extends TrackerFragment {
    String TAG = "FRAG_FAVORITES";
    Context context;
    ListView lst_favorite;
    Switch swch_notifica;
    Boolean notify;
    FavoriteListAdapter listAdapter;
    ArrayList<Map<String, String>> codes_info = new ArrayList<>();


    public FragmentFavorites() {
        // Required empty public constructor
    }
    class CodigosViewHolder{
        TextView            no_guia;
        TextView            codigo;
        Button              edit;
        LinearLayout        linear_favorite;
        int                 position;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        context = getActivity();
        notify=false;

        swch_notifica= (Switch)view.findViewById((R.id.swch_notifica));
        swch_notifica.setChecked(notify);
        lst_favorite = (ListView) view.findViewById(R.id.liv_favorite);
        codes_info = Favorites.getAll(context);
        Log.e(TAG,"codes_info:  ---->  "+codes_info.size());
        Log.e(TAG,"codes_info:  ---->  "+codes_info);
        swch_notifica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(context, "Alarma Activada ", Toast.LENGTH_SHORT).show();
                    listAdapter.notifyDataSetChanged();
                    notify = isChecked;

                    for(int i = 0; i <codes_info.size(); i++) {

                        codes_info.get(i).put("notifica", String.valueOf(notify));
                        Log.e(TAG, "codes_info:  ---->  " + codes_info.get(i));
                    }

                    return;
                } else {
                    Toast.makeText(context, "Alarma Desactivada ", Toast.LENGTH_SHORT).show();
                    listAdapter.notifyDataSetChanged();
                    notify = isChecked;

                    for(int i = 0; i <codes_info.size(); i++) {

                        codes_info.get(i).put("notifica", String.valueOf(notify));
                        Log.e(TAG, "codes_info:  ---->  " + codes_info.get(i));
                    }
                    return;
                }

            }
        });

        if (swch_notifica.isChecked()) {
          //  Toast.makeText(context, "Switch is currently ON", Toast.LENGTH_SHORT).show();
        } else {
          //  Toast.makeText(context, "Switch is currently OFF", Toast.LENGTH_SHORT).show();
        }


        listAdapter = new FavoriteListAdapter(getActivity(), context, codes_info, getActivity().getSupportFragmentManager());
        lst_favorite.setAdapter(listAdapter);

        try {
            DialogManager.sharedInstance().dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "fallo fragment", Toast.LENGTH_SHORT).show();
        }



        return view;
    }


}
