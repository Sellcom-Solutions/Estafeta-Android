package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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

        lst_favorite = (ListView) view.findViewById(R.id.liv_favorite);
        codes_info = Favorites.getAll(context);
        Log.e(TAG,"codes_info:  ---->  "+codes_info.size());
        // codes_info= ArrayList<Map<String,String>> getAll(Context context)

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
