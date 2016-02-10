package com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.Utilities;

import java.util.Map;

/**
 *
 */
public class FragmentDialogPostponePrefilled extends DialogFragment implements View.OnClickListener{


    private TextView    lbl_postpone_code;
    private EditText    txt_add_email,
                        txt_add_other_email;
    private Button      btn_close,
                        btn_share,
                        btn_send;
    private ImageView   imgv_selected_email_1,
                        imgv_selected_email_2;
    private LinearLayout    lin_add_other_email;

    private String      email;

    public static final String TAG = "FragmentDialogPostponePrefilled";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);

        if(getArguments()!=null && getArguments().getString("email_postpone_code") != null)
            email = getArguments().getString("email_postpone_code");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_postpone_prefilled, container, false);
        if(view != null){
            //TextView
            lbl_postpone_code               = (TextView) view.findViewById(R.id.lbl_postpone_code);
            //EditText
            txt_add_email                   = (EditText) view.findViewById(R.id.txt_add_email);
            txt_add_other_email             = (EditText) view.findViewById(R.id.txt_add_other_email);
            txt_add_other_email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() > 0){
                        imgv_selected_email_1.setImageBitmap(null);
                        imgv_selected_email_2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                        try
                        {
                            SVG svg = SVG.getFromResource(getActivity(), R.raw.check);
                            Drawable drawable = new PictureDrawable(svg.renderToPicture());
                            imgv_selected_email_2.setImageDrawable(drawable);
                        }
                        catch(SVGParseException e)
                        {}

                    }else{
                        imgv_selected_email_2.setImageBitmap(null);
                        imgv_selected_email_1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                        try
                        {
                            SVG svg = SVG.getFromResource(getActivity(), R.raw.check);
                            Drawable drawable = new PictureDrawable(svg.renderToPicture());
                            imgv_selected_email_1.setImageDrawable(drawable);
                        }
                        catch(SVGParseException e)
                        {}
                    }
                }
            });
            //Button
            btn_close                       = (Button) view.findViewById(R.id.btn_close);
            btn_share                       = (Button) view.findViewById(R.id.btn_share);
            btn_send                        = (Button) view.findViewById(R.id.btn_send);
            //ImageView
            imgv_selected_email_1           = (ImageView) view.findViewById(R.id.imgv_selected_email_1);
            imgv_selected_email_2           = (ImageView) view.findViewById(R.id.imgv_selected_email_2);
            //LienarLayout
            lin_add_other_email             = (LinearLayout) view.findViewById(R.id.lin_add_other_email);

            //Listeners
            btn_close.setOnClickListener(this);
            btn_share.setOnClickListener(this);
            btn_send.setOnClickListener(this);

            imgv_selected_email_1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try
            {
                SVG svg = SVG.getFromResource(getActivity(), R.raw.check);
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                imgv_selected_email_1.setImageDrawable(drawable);
            }
            catch(SVGParseException e)
            {}

            txt_add_email.getBackground().setColorFilter(getResources().getColor(R.color.estafeta_red), PorterDuff.Mode.SRC_ATOP);
            txt_add_other_email.getBackground().setColorFilter(getResources().getColor(R.color.estafeta_red), PorterDuff.Mode.SRC_ATOP);

            if(Utilities.validateEmail(email.trim())){
                txt_add_email.setText(email);
            }

            checkEmail();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_close:

                dismiss();

                break;
            case R.id.btn_share:

                Toast.makeText(getActivity(), "Módulo en desarrollo.", Toast.LENGTH_SHORT).show();
                dismiss();

                break;
            case R.id.btn_send:

                Toast.makeText(getActivity(), "Módulo en desarrollo.", Toast.LENGTH_SHORT).show();
                dismiss();

                break;
        }
    }

    private void checkEmail(){
        if(txt_add_email.getText().toString().equals("")){
            txt_add_email.setEnabled(true);
            lin_add_other_email.setVisibility(View.GONE);

        }else{
            txt_add_email.setEnabled(false);
            lin_add_other_email.setVisibility(View.VISIBLE);
        }
    }
}
