package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hugo.figueroa on 20/07/15.
 */
public class RastreoAdapter extends BaseSwipeAdapter {


    String TAG = "RASTREO_SWYPE_ADAPTER_LOG";

    Context context;
    private ArrayList<Map<String,String>> codigos;
    setNumCodes setNumCodes;
    TextView tipo_codigo;
    TextView        no_codigo;
    TextView        txv_delete;


    public  RastreoAdapter (Context context, ArrayList<Map<String,String>> codigos){
        this.context        = context;
        this.codigos       = codigos;
    }


    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup viewGroup) {

        View view       = LayoutInflater.from(context).inflate(R.layout.item_rastreo_swype, null);
            tipo_codigo          = (TextView)view.findViewById(R.id.li_tipo_codigo);
            no_codigo            = (TextView)view.findViewById(R.id.li_codigo);
            txv_delete            = (TextView)view.findViewById(R.id.txv_delete);


        return view;
    }

    @Override
    public void fillValues(final int position, View convertView) {

        final SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
        //swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, convertView.findViewById(R.id.bottom_wrapper));
        swipeLayout.setLeftSwipeEnabled(false);

        txv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close();
                setNumCodes.removeCode(position);

            }
        });

        try {


            Map<String, String> cod = new HashMap<>();
            cod = codigos.get(position);
            final String codStr = cod.get("codigo");
            if(codStr.length() == 10)
                tipo_codigo.setText(context.getString(R.string.cod_rastreo) );
            else {
                tipo_codigo.setText(context.getString(R.string.no_guia));

            }

            int size = 10-codigos.size();
            setNumCodes.setCodes(size);

            no_codigo.setText(codStr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        return codigos.size();
    }

    @Override
    public Object getItem(int position) {
        return codigos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public interface setNumCodes{
        public void setCodes(int restantes);
        public void removeCode(int position);
    }

    public void setCodesNumbers(setNumCodes listener) {
        setNumCodes = listener;
    }

}
