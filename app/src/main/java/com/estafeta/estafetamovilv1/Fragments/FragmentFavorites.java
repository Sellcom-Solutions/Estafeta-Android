package com.estafeta.estafetamovilv1.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import com.estafeta.estafetamovilv1.Activities.MainActivity;
import com.estafeta.estafetamovilv1.Adapters.FavoriteListAdapter;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import database.model.Favorites;
import database.model.History;

/**
 *This class shows codes stored in 'Favorites'.
 */
public class FragmentFavorites extends TrackerFragment implements FavoriteListAdapter.delete {
    public static final String TAG = "FRAG_FAVORITES";
    Context context;


    View view;
    ListView lst_favorite;
    LinearLayout lin_margin,lin_delete,lin_toolbar;
    TextView footer;

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

        TrackerFragment.section_index = 6;
        context = getActivity();

        lin_toolbar = (LinearLayout)view.findViewById(R.id.lin_toolbar);
        lin_toolbar.setVisibility(View.GONE);




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
                    if(codes_info == null){
                        codes_info = new ArrayList<>();
                        delete.setEnabled(false);

                    }else{
                        delete.setEnabled(true);
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
        Log.d(TAG,"codes_info size:  ---->  "+codes_info.size());

        listAdapter = new FavoriteListAdapter(getActivity(), context, codes_info, getActivity().getSupportFragmentManager(),"favorite");
        listAdapter.setDelete(this);
        lst_favorite.setAdapter(listAdapter);

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

    /**
     * In this method a little toolbar functionality is handled.
     */
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

    /**
     * Change the contents of the toolbar depending on the items to be removed.
     * @param favoriteDelete
     */
    @Override
    public void deleteFavoriteById(Map<String, String> favoriteDelete) {

        list_delete.add(favoriteDelete);
        if(list_delete.size() == 1){
            mToolbar.setTitle(list_delete.size()+ " seleccionado(s)");
        }else {
            mToolbar.setTitle(list_delete.size()+ " seleccionado(s)");
        }

    }

    /**
     * Change the contents of the toolbar depending on the items to be removed.
     * @param favoriteDelete
     */
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

    /**
     * Receives a notification that the change list to refresh.
     */
    @Override
    public void changeReference() {

        codes_info = Favorites.getAll(context);
        listAdapter = new FavoriteListAdapter(getActivity(), context, codes_info, getActivity().getSupportFragmentManager(),"favorite");
        listAdapter.setDelete(FragmentFavorites.this);
        lst_favorite.setAdapter(listAdapter);
    }


}
