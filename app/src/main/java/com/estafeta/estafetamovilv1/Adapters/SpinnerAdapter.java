package com.estafeta.estafetamovilv1.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.estafeta.estafetamovilv1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jonathan.vazquez on 21/05/15.
 * This adapter is for all spinners of the application
 */
public class SpinnerAdapter extends BaseAdapter {

    /**
     * This enum contains the types of spinner used in the application.
     */
    public enum SPINNER_TYPE {
        STATES ("states"),
        COUNTRIES ("countries");

        private final String name;

        private SPINNER_TYPE(String s) {
            name = s;
        }

        public boolean equalsName(String otherName){
            return (otherName == null)? false:name.equals(otherName);
        }

        public String toString(){
            return name;
        }

    }

    private Context         mContext;
    private SPINNER_TYPE    type;

    private List<Map<String,String>> mItems = new ArrayList<Map<String,String>>();

    public     SpinnerAdapter(Context context, List<Map<String,String>> items, SPINNER_TYPE type) {
        this.mContext            = context;
        this.mItems              = items;
        this.type                = type;
    }

    public int getCount() {
        return mItems.size();
    }


    /**
     * Disables the item of the position 0.
     */
    @Override
    public boolean isEnabled(int position) {
        if(position == 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * This method determines the parameter that must return to the method getItem call.
     * @return Return the item required depending on the type of spinner.
     */
    @Override
    public Object getItem(int position) {

        String item = "";

        switch (type){
            case STATES:
                item =  mItems.get(position).get("ZNOMBRE");
                break;
            case COUNTRIES:
                item =  mItems.get(position).get("NOMBREPAIS_ESP");
                break;

            default:
                break;
        }
        return item;
    }

    /**
     * It provides a particular style item of the position 0 of the spinner.
     *
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Map<String,String>  mItem    = mItems.get(i);

        if (view == null || !view.getTag().toString().equals("NON_DIALOG")) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_spinner_head, null);
            view.setTag("NON_DROPDOWN");
        }

        TextView main_text = (TextView) view .findViewById(R.id.txt_spn_item_head);
        if(i == 0){
            main_text.setTextColor(mContext.getResources().getColor(R.color.estafeta_red));
        }else{
            main_text.setTextColor(mContext.getResources().getColor(R.color.estafeta_text));
        }

        switch (type){
            case STATES:
                main_text.setText(mItem.get("ZNOMBRE"));
                break;
            case COUNTRIES:
                main_text.setText(mItem.get("NOMBREPAIS_ESP"));
                break;

            default:
                break;
        }

        return view;
    }

    /**
     * It provides a particular style for all items except the spinner 0.
     */

    @Override
    public View getDropDownView(int i, View view, ViewGroup parent) {
        Map<String,String>  mItem    = mItems.get(i);

        if (view == null || !view.getTag().toString().equals("DIALOG")) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_spinner, null);
            view.setTag("DIALOG2");
        }

        TextView main_text = (TextView) view .findViewById(R.id.txt_spn_item);

        if(i == 0){
            main_text.setTextColor(Color.GRAY);
        }




        switch (type){
            case STATES:
                main_text.setText(mItem.get("ZNOMBRE"));
                break;
            case COUNTRIES:
                main_text.setText(mItem.get("NOMBREPAIS_ESP"));
                break;

            default:
                break;
        }
        return view;
    }


    public long getItemId(int position) {
        return position;
    }
}
