package com.estafeta.estafetamovilv1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;

import com.estafeta.estafetamovilv1.Fragments.Fase_2.FragmentFequentlyContacts;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled.FragmentDialogGetDataFrequentlyContacts;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import database.model.FrequentlyContacts;

/**
 * Created by hugo.figueroa on 03/02/16.
 */
public class FrequentlyContactsAdapter extends BaseAdapter{

    private Activity         context;
    private List<Map<String,String>> listFrequentlyContacts;
    private List<CheckBox>   listCheckBox;
    private TextView        lbl_name_contact,
                            lbl_cp_contact;
    private FragmentManager fragmentManager;


    public FrequentlyContactsAdapter(Activity context, FragmentManager fragmentManager, List<Map<String,String>> listFrequentlyContacts){
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.listFrequentlyContacts = listFrequentlyContacts;
        listCheckBox = new LinkedList<>();

    }

    @Override
    public int getCount() {
        return listFrequentlyContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return listFrequentlyContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_frequently_contacts, parent, false);

        //TextViews
        lbl_name_contact    = (TextView) view.findViewById(R.id.lbl_name_contact);
        lbl_cp_contact      = (TextView) view.findViewById(R.id.lbl_cp_contact);
        //CheckBox
        CheckBox btn_favorito        = (CheckBox) view.findViewById(R.id.btn_favorito);
        if(listFrequentlyContacts.get(position).get(FrequentlyContacts.STATUS_CONTACT).equalsIgnoreCase("TRUE")){
            btn_favorito.setChecked(true);
        }else{
            btn_favorito.setChecked(false);
        }

        btn_favorito.setId(position);
        btn_favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox btnFav = (CheckBox) v;
                if (!btnFav.isChecked()) {
                    Log.e("frequently_contacts","no_check");
                    listCheckBox.get(v.getId()).setChecked(false);
                    FrequentlyContacts.updateStatusById(context, listFrequentlyContacts.get(v.getId()).get(FrequentlyContacts.ID_FREQUENTLY), false);

                    int count = FrequentlyContacts.getCountFrequentlyContactsFavorites(context);
                    Log.d("size: ","size: "+count);
                    if (count == 4) {
                        for (int i = 0; i < listCheckBox.size(); i++) {
                            if (!listCheckBox.get(i).isChecked()) {
                                listCheckBox.get(i).setEnabled(true);
                            }
                        }
                    }

                } else {
                    Log.e("frequently_contacts","check");
                    listCheckBox.get(v.getId()).setChecked(true);
                    int count = FrequentlyContacts.getCountFrequentlyContactsFavorites(context);
                    if (count < 5) {
                        FrequentlyContacts.updateStatusById(context, listFrequentlyContacts.get(v.getId()).get(FrequentlyContacts.ID_FREQUENTLY), true);

                        if (count == 4) {
                            for (int i = 0; i < listCheckBox.size(); i++) {
                                if (!listCheckBox.get(i).isChecked()) {
                                    listCheckBox.get(i).setEnabled(false);
                                }
                            }
                        }
                    }
                }
            }
        });


        listCheckBox.add(btn_favorito);
        if(listCheckBox.size() == getCount()){
            int check = 0;
            for(int i = 0; i < listCheckBox.size(); i++){
                if(listCheckBox.get(i).isChecked()){
                    check++;
                }
            }

            if(check==5){
                for (int i = 0; i < listCheckBox.size(); i++) {
                    if (!listCheckBox.get(i).isChecked()) {
                        listCheckBox.get(i).setEnabled(false);
                    }
                }
            }
        }

        //LinearLayout
        final LinearLayout lin_delete          = (LinearLayout) view.findViewById(R.id.lin_delete);
        lin_delete.setId(position);
        lin_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("frequently_contacts","POSITION: "+v.getId());
                if(listCheckBox.get(v.getId()).isChecked()){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,"No se pueden eliminar contactos marcados como favoritos.",3000);
                    if(lin_delete.getVisibility() == View.VISIBLE){
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_to_right);
                        lin_delete.startAnimation(animation);
                        lin_delete.setVisibility(View.GONE);
                    }
                }else{
                    listCheckBox.remove(v.getId());
                    FrequentlyContacts.delete(context,listFrequentlyContacts.get(v.getId()).get(FrequentlyContacts.ID_FREQUENTLY));
                    listFrequentlyContacts.removeAll(listFrequentlyContacts);
                    listFrequentlyContacts = FrequentlyContacts.getAllInMaps(context);
                    if(listFrequentlyContacts == null){
                        listFrequentlyContacts = new ArrayList<Map<String, String>>();
                    }
                    notifyDataSetChanged();
                }
            }
        });

        final LinearLayout lin_container_frequently = (LinearLayout) view.findViewById(R.id.lin_container_frequently);
        lin_container_frequently.setId(position);
        lin_container_frequently.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(lin_delete.getVisibility() == View.VISIBLE){
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_to_right);
                    lin_delete.startAnimation(animation);
                    lin_delete.setVisibility(View.GONE);
                }else {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_right);
                    lin_delete.startAnimation(animation);
                    lin_delete.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        lin_container_frequently.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lin_delete.getVisibility() == View.VISIBLE){
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_to_right);
                    lin_delete.startAnimation(animation);
                    lin_delete.setVisibility(View.GONE);
                }

                Bundle b = new Bundle();
                b.putSerializable("dataContact", (Serializable) listFrequentlyContacts.get(v.getId()));

                FragmentDialogGetDataFrequentlyContacts fdgdfc = new FragmentDialogGetDataFrequentlyContacts();
                fdgdfc.setArguments(b);
                fdgdfc.show(fragmentManager,FragmentDialogGetDataFrequentlyContacts.TAG);

            }
        });

        lbl_name_contact.setText(listFrequentlyContacts.get(position).get("name_contact"));
        lbl_cp_contact.setText("C.P. " + listFrequentlyContacts.get(position).get("cp_contact"));


        return view;
    }

}
