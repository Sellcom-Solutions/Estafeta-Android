package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rebecalopezmartinez on 23/06/15.
 */
public class HistoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<Map<String,String>> history;

    public HistoryAdapter(Context context, ArrayList<Map<String,String>> history){
        this.context = context;
        this.history = history;
    }

    class HistoryViewHolder{
        TextView    date_time,
                    place,
                    comments;
        int position;
    }

    @Override
    public int getCount() {
        return history.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final HistoryViewHolder   holder;
        if (convertView == null){
            holder              = new HistoryViewHolder();
            convertView         = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_history,parent,false);
            holder.date_time    = (TextView)convertView.findViewById(R.id.history_date);
            holder.place        = (TextView)convertView.findViewById(R.id.history_place);
            holder.comments     =(TextView)convertView.findViewById(R.id.history_comments);
            convertView.setTag(holder);
        }
        else{
            holder  = (HistoryViewHolder)convertView.getTag();
        }

        try {
            Map<String, String> item = new HashMap<>();
            item = history.get(position);
            Log.d("HistoryAdapter", "size"+item.size());
            String dateStr = item.get("H_eventDateTime");
            holder.date_time.setText(dateStr);
            String placeStr = item.get("H_eventPlaceName");
            holder.place.setText(placeStr);
            String commentStr = item.get("H_eventDescriptionSPA");
            holder.comments.setText(commentStr);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
