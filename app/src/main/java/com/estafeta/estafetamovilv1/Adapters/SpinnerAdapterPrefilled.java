package com.estafeta.estafetamovilv1.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estafeta.estafetamovilv1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo.figueroa on 22/01/16.
 */
public class SpinnerAdapterPrefilled extends BaseAdapter {


    private List<String> items_list = new ArrayList<>();
    private Context context;

    public SpinnerAdapterPrefilled(Context context,List<String> items_list){
        this.items_list = items_list;
        this.context    = context;
    }

    @Override
    public int getCount() {
        return items_list.size();
    }

    @Override
    public Object getItem(int position) {
        return items_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        if(position == 0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_spinner_head, null);

        TextView tv = (TextView) view.findViewById(R.id.txt_spn_item_head);
        tv.setText(items_list.get(position));

        return view;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_spinner, null);

        if(position == 0) {
            TextView tv = (TextView) view.findViewById(R.id.txt_spn_item);
            //items_list.get(position).replace("*","");
            //tv.setTypeface(tv.getTypeface(),Typeface.BOLD);
            tv.setTextColor(Color.GRAY);
            tv.setText(items_list.get(position));
        } else
            ((TextView) view.findViewById(R.id.txt_spn_item)).setText(items_list.get(position));

        return view;
    }

}
