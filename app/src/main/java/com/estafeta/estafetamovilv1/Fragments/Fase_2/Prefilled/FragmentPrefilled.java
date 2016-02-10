package com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * This class allows you to start or continue a prefilled for one shipment.
 *
 */
public class FragmentPrefilled extends TrackerFragment{

    public boolean test = false;

    private TrackerFragment     fragment;
    private Map<String,String>   dataSender;

    private TextView        footer;

    public enum DATA_SENDER {
        SENDER_NAME("sender_name"),
        BUSINESS_NAME("business_name"),
        STREET_SENDER("street_sender"),
        NO_EXT_SENDER("no_ext_sender"),
        NO_INT_SENDER("no_int_sender"),
        ZIP_CODE_SENDER("zip_code_sender"),
        COLONY_SENDER("colony_sender"),
        CITY_SENDER("city_sender"),
        STATE_SENDER("state_sender"),
        PHONE_SENDER("phone_sender"),
        EMAIL_SENDER("email_sender");

        private final String name;

        private DATA_SENDER(String s) {
            name = s;
        }

        public String toString(){
            return name;
        }
    }

    public enum DATA_ADDRESSEE {
        ADDRESSEE_NAME("addressee_name"),
        ADDRESSEE_BUSINESS_NAME("addressee_business_name"),
        STREET_ADDRESSEE("street_addressee"),
        NO_EXT_ADDRESSEE("no_ext_addressee"),
        NO_INT_ADDRESSEE("no_int_addressee"),
        ZIP_CODE_ADDRESSEE("zip_code_addressee"),
        COLONY_ADDRESSEE("colony_addressee"),
        CITY_ADDRESSEE("city_addressee"),
        STATE_ADDRESSEE("state_addressee"),
        PHONE_ADDRESSEE("phone_addressee"),
        EMAIL_ADDRESSEE("email_addressee"),
        REFERENCE_ADDRESSEE("reference_addressee"),
        NAVE_ADDRESSEE("nave_addressee"),
        PLATFORM_ADDRESSEE("platform_addressee"),;

        private final String name;

        private DATA_ADDRESSEE(String s) {
            name = s;
        }

        public String toString(){
            return name;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prefilled, container, false);
        if(view!=null) {

            TrackerFragment.section_index = 2;

            ImageView imageView = (ImageView) view.findViewById(R.id.imgv_prefilled_head);
            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try {
                SVG svg = SVG.getFromResource(getActivity(), R.raw.prellenado_header);
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                imageView.setImageDrawable(drawable);
            } catch (SVGParseException e) {
            }

            footer = (TextView) view.findViewById(R.id.footer);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            String currentYear = formatter.format(new Date());
            footer.setText("©2012-" + currentYear + " " + getString(R.string.footer));

            fragment = new FragmentPrefilledSender();
            if (test) {
                ((FragmentPrefilledSender) fragment).testSender = true;
            }
            addFragmentPrefilledToStack(getActivity(), fragment, FRAGMENT_TAG.FRAG_REMITENTE.toString(), false);
        }
        return view;
    }


    public Map<String, String> getDataSender() {
        return dataSender;
    }



    public void setDataSender(Map<String, String> dataSender) {
        this.dataSender = dataSender;
    }

    @Override
    public void onResume() {
        super.onResume();
        TrackerFragment.section_index = 2;
    }
}
