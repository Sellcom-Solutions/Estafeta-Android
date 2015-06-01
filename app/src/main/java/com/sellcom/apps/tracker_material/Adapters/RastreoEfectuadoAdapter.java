package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.List;
import java.util.Map;

/**
 * Created by rebecalopezmartinez on 30/05/15.
 */
public class RastreoEfectuadoAdapter extends BaseAdapter{
    String TAG = "RASTREO_EFECTUADO_ADAPTER_LOG";

    Context context;
    //private List<Map<String,String>> codigos;
    String []codigos;
    private List<Map<String,String>> codigos_copy;

    public  RastreoEfectuadoAdapter (Context context, /*List<Map<String,String>> */String []codigos){
        this.context        = context;
        this.codigos       = codigos;
        //this.codigos_copy  = new ArrayList<>(codigos);
    }

    class CodigosViewHolder{
        TextView        no_guia;
        TextView        codigo;
        TextView        status;
        ImageButton     btn_favoritos;
        ImageView       img_status;
        int             position;

    }
    @Override
    public int getCount() {
        return codigos.length;
    }

    @Override
    public Object getItem(int position) {
        // return codigos.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CodigosViewHolder   holder;
        if (convertView == null){
            holder                  = new CodigosViewHolder();
            convertView             = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_rastreo_efectuado,parent,false);
            holder.codigo           = (TextView)convertView.findViewById(R.id.txt_codigo_rastreo);
            holder.no_guia          = (TextView)convertView.findViewById(R.id.txt_no_guia);
            holder.status           = (TextView)convertView.findViewById(R.id.txt_estatus);
            holder.btn_favoritos    = (ImageButton)convertView.findViewById(R.id.btn_favorito);
            holder.img_status       = (ImageView) convertView.findViewById(R.id.img_status);

            convertView.setTag(holder);
        }
        else{
            holder  = (CodigosViewHolder)convertView.getTag();
        }

        holder.codigo.setText(codigos[position]);

        return convertView;
    }
}
