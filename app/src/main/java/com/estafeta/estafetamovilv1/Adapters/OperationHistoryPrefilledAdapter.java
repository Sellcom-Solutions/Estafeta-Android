package com.estafeta.estafetamovilv1.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.estafeta.estafetamovilv1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import database.model.PrefilledHistory;

/**
 * Created by hugo.figueroa on 25/02/16.
 */
public class OperationHistoryPrefilledAdapter extends BaseAdapter{

    private Activity activity;
    private List<Map<String,String>> listOperationPrefilled;
    public OperationHistoryPrefilledAdapter(Activity activity, List<Map<String,String>> listOperationPrefilled){
        this.activity                   = activity;
        this.listOperationPrefilled     = listOperationPrefilled;
    }


    @Override
    public int getCount() {
        return listOperationPrefilled.size();
    }

    @Override
    public Object getItem(int position) {
        return listOperationPrefilled.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_operation_history_prefilled,parent,false);

        TextView lbl_timestamp      = (TextView) view.findViewById(R.id.lbl_timestamp);
        TextView lbl_origin         = (TextView) view.findViewById(R.id.lbl_origin);
        TextView lbl_destiny        = (TextView) view.findViewById(R.id.lbl_destiny);


        String[] dateInString = listOperationPrefilled.get(position).get(PrefilledHistory.TIMESTAMP).split(" ");
        //String[] time = dateInString[1].split(".");

        lbl_timestamp.setText(dateInString[0]+"\n"+dateInString[1]);
        lbl_origin.setText(listOperationPrefilled.get(position).get(PrefilledHistory.CP_ORIGIN_SENDER) + ", "+listOperationPrefilled.get(position).get(PrefilledHistory.ORIGIN_SENDER));
        lbl_destiny.setText(listOperationPrefilled.get(position).get(PrefilledHistory.CP_DESTINY_ADDRESSEE) + ", "+listOperationPrefilled.get(position).get(PrefilledHistory.DESTINY_ADDRESSEE));



        return view;
    }
}
