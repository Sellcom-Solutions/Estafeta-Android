package com.estafeta.estafetamovilv1.Fragments.Fase_2;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListView;

import com.estafeta.estafetamovilv1.Activities.MainActivity;
import com.estafeta.estafetamovilv1.Adapters.FrequentlyContactsAdapter;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled.FragmentDialogGetDataFrequentlyContacts;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.QuoteAndBuy.FragmentDialogWrongData;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.model.FrequentlyContacts;

/**
 *
 */
public class FragmentFequentlyContacts extends DialogFragment implements FragmentDialogGetDataFrequentlyContacts.setPressedButtonContinue{


    private TextView    footer;
    private List<Map<String,String>> listFrequentlyContacts;
    private ListView    lv_frequently_contacts;
    private MenuItem    close;

    private Toolbar mToolbar;
    private ActionBar actionBarActivity;

    public setPressedButtonContinue listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listFrequentlyContacts = new ArrayList<>();
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fequently_contacts, container, false);
        if(view != null){

            lv_frequently_contacts      = (ListView) view.findViewById(R.id.lv_frequently_contacts);


//            ((MainActivity) getActivity()).hideDrawer(false);

            mToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
            mToolbar.setNavigationIcon(null);
            mToolbar.inflateMenu(R.menu.menu_frequently_contacts);
            mToolbar.setTitle("Contactos frecuentes");
            mToolbar.setPadding(100, 0, 0, 0);

            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem arg0) {
                    switch (arg0.getItemId()) {
                        case R.id.close:
                            //((MainActivity) getActivity()).hideDrawer(true);
                            dismiss();
                            return true;
                    }
                    return false;
                }
            });

            listFrequentlyContacts = FrequentlyContacts.getAllInMaps(getActivity());


            FrequentlyContactsAdapter adapter = new FrequentlyContactsAdapter(getActivity(), getActivity().getSupportFragmentManager(),listFrequentlyContacts);
            lv_frequently_contacts.setAdapter(adapter);


            footer      = (TextView)view.findViewById(R.id.footer);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            String currentYear = formatter.format(new Date());
            footer.setText("©2012-" + currentYear + " " + getString(R.string.footer));


        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!((MainActivity) getActivity()).isDrawerOpen) {
            menu.clear();
            inflater.inflate(R.menu.menu_frequently_contacts, menu);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        close = menu.findItem(R.id.close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:
                close.setEnabled(false);
                //((MainActivity) getActivity()).hideDrawer(true);
                actionBarActivity.show();
                dismiss();

                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismiss();
        //((MainActivity) getActivity()).hideDrawer(true);
    }

    @Override
    public void pressedButtonContinue(Map<String, String> dataContacs){
        //((MainActivity) getActivity()).hideDrawer(true);
        if(getArguments()!= null && getArguments().getString("cp_quote") != null){
            if(!getArguments().getString("cp_quote").equals(dataContacs.get(FrequentlyContacts.CP_CONTACT))){
                FragmentDialogWrongData fdwd = new FragmentDialogWrongData();
                fdwd.setArguments(getArguments());
                fdwd.show(getActivity().getSupportFragmentManager(),FragmentDialogWrongData.TAG);
            }else{

                listener.pressedButtonContinue(dataContacs);
                dismiss();
            }

        }else {

            listener.pressedButtonContinue(dataContacs);
            dismiss();
        }

    }

    public interface setPressedButtonContinue{
        void pressedButtonContinue(Map<String,String> dataContact);
    }
}
