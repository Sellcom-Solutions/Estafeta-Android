package com.sellcom.apps.tracker_material.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.FavoriteListAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import database.model.Favorites;
import database.model.History;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFavorites extends TrackerFragment implements FavoriteListAdapter.delete {
    public static final String TAG = "FRAG_FAVORITES";
    Context context;

    View view;
    ListView lst_favorite;
    LinearLayout lin_margin,lin_delete,lin_toolbar;
    TextView footer;
    SwitchCompat swch_notifica;

    ImageView imgbtn_delete;
    boolean notify;
    FavoriteListAdapter listAdapter;
    ArrayList<Map<String, String>> codes_info = new ArrayList<>();
    ArrayList<Map<String, String>> list_delete = new ArrayList<>();

    private MenuItem delete;

    SharedPreferences sharedPref;

    private Toolbar mToolbar;
    private ActionBar actionBarActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogManager.sharedInstance().dismissDialog();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorites, container, false);

        sharedPref        = getActivity().getPreferences(Context.MODE_PRIVATE);
        notify = sharedPref.getBoolean("notify", false);

        SharedPreferences.Editor editor     = sharedPref.edit();
        editor.putBoolean("notify", notify);
        editor.commit();



        TrackerFragment.section_index = 5;
        context = getActivity();

        lin_toolbar = (LinearLayout)view.findViewById(R.id.lin_toolbar);
        lin_toolbar.setVisibility(View.GONE);

        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.Color1SwitchStyle);


        swch_notifica= (SwitchCompat)view.findViewById((R.id.swch_notifica));
        swch_notifica.setChecked(notify);



        lst_favorite = (ListView) view.findViewById(R.id.liv_favorite);
        lin_margin = (LinearLayout)view.findViewById(R.id.lin_margin);
        lin_delete = (LinearLayout)view.findViewById(R.id.lin_delete);
        footer = (TextView)view.findViewById(R.id.txv_footer);
        imgbtn_delete = (ImageView)view.findViewById(R.id.imgbtn_delete);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String currentYear = formatter.format(new Date());
        footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));


        actionBarActivity = ((ActionBarActivity) getActivity()).getSupportActionBar();


        imgbtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(list_delete.size()>0){

                    for(int i = 0; i<list_delete.size(); i++){
                        History.delete(context,list_delete.get(i).get("id_favoritos"));
                        Favorites.delete(context,list_delete.get(i).get("id_favoritos"));
                    }


                    codes_info = Favorites.getAll(context);
                    codes_info = Favorites.getAll(context);
                    if(codes_info == null){
                        codes_info = new ArrayList<>();
                        delete.setEnabled(false);

                        swch_notifica.setEnabled(false);
                    }else{
                        delete.setEnabled(true);
                        swch_notifica.setEnabled(true);
                    }

                    listAdapter = new FavoriteListAdapter(getActivity(), context, codes_info, getActivity().getSupportFragmentManager(),"favorite");
                    listAdapter.setDelete(FragmentFavorites.this);
                    lst_favorite.setAdapter(listAdapter);

                    ViewGroup.MarginLayoutParams lv = (ViewGroup.MarginLayoutParams) lst_favorite.getLayoutParams();
                    float d = context.getResources().getDisplayMetrics().density;
                    lv.setMargins(0, 0, 0, (int) (5 * d));
                    lst_favorite.setLayoutParams(lv);

                    lin_delete.setVisibility(View.GONE);
                    footer.setVisibility(View.VISIBLE);

                    lin_toolbar.setVisibility(View.GONE);
                    actionBarActivity.show();

                }else{
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,"Debe escoger un elemento",3000);
                }

            }
        });

        codes_info = Favorites.getAll(context);
        Log.e(TAG,"codes_info:  ---->  "+codes_info.size());
        Log.e(TAG,"codes_info:  ---->  "+codes_info);
        swch_notifica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    listAdapter.notifyDataSetChanged();
                    notify = isChecked;

                    SharedPreferences.Editor editor     = sharedPref.edit();
                    editor.putBoolean("notify", notify);
                    editor.commit();

                    for(int i = 0; i <codes_info.size(); i++) {

                        codes_info.get(i).put("notifica", String.valueOf(notify));
                        Log.e(TAG, "Activada codes_info:  ---->  " + codes_info.get(i));
                    }

                    return;
                } else {
                    listAdapter.notifyDataSetChanged();
                    notify = isChecked;

                    SharedPreferences.Editor editor     = sharedPref.edit();
                    editor.putBoolean("notify", notify);
                    editor.commit();

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
        listAdapter = new FavoriteListAdapter(getActivity(), context, codes_info, getActivity().getSupportFragmentManager(),"favorite");
        listAdapter.setDelete(this);
        lst_favorite.setAdapter(listAdapter);
        swch_notifica.setEnabled(true);

        try {
            DialogManager.sharedInstance().dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }



        return view;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (!((MainActivity) getActivity()).isDrawerOpen) {
            menu.clear();
            inflater.inflate(R.menu.menu_favorites, menu);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        lin_toolbar.setVisibility(View.GONE);
        actionBarActivity.show();

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        delete = menu.findItem(R.id.add_favorite);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "Selected:  " + item.getItemId());



        switch (item.getItemId()) {

            case R.id.add_favorite:

                list_delete = new ArrayList<>();

                lin_toolbar.setVisibility(View.VISIBLE);

                actionBarActivity.hide();




                mToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar_favorites);
                mToolbar.setNavigationIcon(R.drawable.ic_arrow_left);
                mToolbar.setTitle("0 seleccionado(s)");
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        listAdapter = new FavoriteListAdapter(getActivity(), context, codes_info, getActivity().getSupportFragmentManager(), "favorite");
                        listAdapter.setDelete(FragmentFavorites.this);
                        lst_favorite.setAdapter(listAdapter);

                        swch_notifica.setEnabled(true);

                        ViewGroup.MarginLayoutParams lv = (ViewGroup.MarginLayoutParams) lst_favorite.getLayoutParams();
                        float d = context.getResources().getDisplayMetrics().density;
                        lv.setMargins(0, 0, 0, (int) (5 * d));
                        lst_favorite.setLayoutParams(lv);

                        lin_delete.setVisibility(View.GONE);
                        footer.setVisibility(View.VISIBLE);

                        lin_toolbar.setVisibility(View.GONE);
                        actionBarActivity.show();
                    }
                });

                mToolbar.setBackgroundResource(R.color.estafeta_text);

                listAdapter = new FavoriteListAdapter(getActivity(), context, codes_info, getActivity().getSupportFragmentManager(),"delete");
                listAdapter.setDelete(FragmentFavorites.this);
                lst_favorite.setAdapter(listAdapter);

                swch_notifica.setEnabled(false);

                footer.setVisibility(View.GONE);
                lin_delete.setVisibility(View.VISIBLE);

                ViewGroup.MarginLayoutParams lv = (ViewGroup.MarginLayoutParams) lst_favorite.getLayoutParams();
                float d = context.getResources().getDisplayMetrics().density;
                lv.setMargins(0, 0, 0, (int) (30 * d));
                lst_favorite.setLayoutParams(lv);

                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void deleteFavoriteById(Map<String, String> favoriteDelete) {

        list_delete.add(favoriteDelete);
        if(list_delete.size() == 1){
            mToolbar.setTitle(list_delete.size()+ " seleccionado(s)");
        }else {
            mToolbar.setTitle(list_delete.size()+ " seleccionado(s)");
        }

    }

    @Override
    public void cancelDeleteById(Map<String, String> favoriteDelete) {

        for (int i=0; i<list_delete.size(); i++){

            if(favoriteDelete.get("id_favoritos").equalsIgnoreCase(list_delete.get(i).get("id_favoritos"))){
                list_delete.remove(i);
            }

        }

        if(list_delete.size() == 1){
            mToolbar.setTitle(list_delete.size()+ " seleccionado(s)");
        }else {
            mToolbar.setTitle(list_delete.size()+ " seleccionado(s)");
        }

    }

    @Override
    public void changeReference() {
        codes_info = Favorites.getAll(context);
        listAdapter = new FavoriteListAdapter(getActivity(), context, codes_info, getActivity().getSupportFragmentManager(),"favorite");
        listAdapter.setDelete(FragmentFavorites.this);
        lst_favorite.setAdapter(listAdapter);
    }


}
