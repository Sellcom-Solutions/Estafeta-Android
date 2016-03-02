package com.estafeta.estafetamovilv1.Fragments.Fase_2.OperationHistory;


import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ListView;
import android.widget.LinearLayout;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.estafeta.estafetamovilv1.Adapters.OperationHistoryPrefilledAdapter;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled.FragmentDialogQRCode;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import database.model.PrefilledHistory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOperationHistory extends TrackerFragment implements View.OnClickListener{


    private TextView        footer;

    private ToggleButton    tbtn_prefilled,
                            tbtn_buys;

    private ListView        lv_prefilled,
                            lv_buys;

    private ImageView       imgv_no_prefilled;

    private LinearLayout    lin_no_prefilled;

    private long            mLastClickTime;

    private OperationHistoryPrefilledAdapter operationHistoryPrefilledAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_operation_history, container, false);
        if(view != null){

            TrackerFragment.section_index = 4;

            tbtn_prefilled          = (ToggleButton) view.findViewById(R.id.tbtn_prefilled);
            tbtn_buys               = (ToggleButton) view.findViewById(R.id.tbtn_buys);

            lv_prefilled            = (ListView) view.findViewById(R.id.lv_prefilled);
            lv_buys                 = (ListView) view.findViewById(R.id.lv_buys);

            imgv_no_prefilled       = (ImageView) view.findViewById(R.id.imgv_no_prefilled);
            lin_no_prefilled        = (LinearLayout) view.findViewById(R.id.lin_no_prefilled);

            tbtn_prefilled.setOnClickListener(this);
            tbtn_buys.setOnClickListener(this);
            tbtn_prefilled.setChecked(true);
            tbtn_buys.setChecked(false);

            ImageView imageView = (ImageView) view.findViewById(R.id.imgv_operation_history);
            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try {
                SVG svg = SVG.getFromResource(getActivity(), R.raw.historial_head);
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                imageView.setImageDrawable(drawable);
            } catch (SVGParseException e) {
            }

            imgv_no_prefilled.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try {
                SVG svg = SVG.getFromResource(getActivity(), R.raw.sad);
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                imgv_no_prefilled.setImageDrawable(drawable);
            } catch (SVGParseException e) {
            }

            if(PrefilledHistory.getCountPrefilledHistoryContacts(getActivity()) > 0) {
                lv_prefilled.setVisibility(View.VISIBLE);
                lv_buys.setVisibility(View.GONE);
                operationHistoryPrefilledAdapter = new OperationHistoryPrefilledAdapter(getActivity(), PrefilledHistory.getAllInMaps(getActivity()));
                lv_prefilled.setAdapter(operationHistoryPrefilledAdapter);
            }else {
                lv_prefilled.setVisibility(View.GONE);
                lin_no_prefilled.setVisibility(View.VISIBLE);
            }




            lv_prefilled.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    Bundle bundle = new Bundle();
                    bundle.putString("origin","history");
                    bundle.putSerializable("dataAddressee", (Serializable) operationHistoryPrefilledAdapter.getItem(position));

                    FragmentDialogQRCode fdqrc = new FragmentDialogQRCode();
                    fdqrc.setArguments(bundle);
                    fdqrc.show(getActivity().getSupportFragmentManager(), fdqrc.TAG);

                }
            });


            footer = (TextView) view.findViewById(R.id.footer);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            String currentYear = formatter.format(new Date());
            footer.setText("©2012-" + currentYear + " " + getString(R.string.footer));

        }
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tbtn_prefilled:

                if (((ToggleButton) v).isChecked()) {
                    tbtn_buys.setChecked(false);
                    lv_buys.setVisibility(View.GONE);
                    lv_prefilled.setVisibility(View.VISIBLE);

                } else {
                    tbtn_prefilled.setChecked(true);
                }
                break;

            case R.id.tbtn_buys:

                Toast.makeText(getActivity(), "Módulo en Desarrollo.", Toast.LENGTH_SHORT).show();

                tbtn_buys.setChecked(false);
                /*if (((ToggleButton) v).isChecked()) {
                    tbtn_prefilled.setChecked(false);
                    lv_prefilled.setVisibility(View.GONE);
                    lv_buys.setVisibility(View.VISIBLE);

                } else {
                    tbtn_buys.setChecked(true);
                }*/
                break;
        }
    }
}
