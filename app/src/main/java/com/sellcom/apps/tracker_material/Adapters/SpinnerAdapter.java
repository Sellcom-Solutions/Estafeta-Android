package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jonathan.vazquez on 21/05/15.
 */
public class SpinnerAdapter extends BaseAdapter {

    public enum SPINNER_TYPE {
        STATES ("states");

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
        return mItems.size()-1;
    }

    public Object getItem(int position) {
        return mItems .get(position);
    }

    @Override

    public View getView(int i, View view, ViewGroup viewGroup) {
        Map<String,String>  mItem    = mItems.get(i);

        if (view == null || !view.getTag().toString().equals("NON_DIALOG")) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_spinner_head, null);
            view.setTag("NON_DROPDOWN");
        }

        TextView main_text = (TextView) view .findViewById(R.id.txt_spn_item_head);
        //main_text.setBackground(mContext.getResources().getDrawable(R.drawable.underline_red));
        switch (type){
            case STATES:
                main_text.setText(mItem.get("state"));
                break;

            default:
                break;
        }

        return view;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup parent) {

        Map<String,String>  mItem    = mItems.get(i);

        if (view == null || !view.getTag().toString().equals("DIALOG")) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_spinner, null);
            view.setTag("DIALOG");
        }


        TextView main_text = (TextView) view .findViewById(R.id.txt_spn_item);

        switch (type){
            case STATES:
                main_text.setText(mItem.get("state"));
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
