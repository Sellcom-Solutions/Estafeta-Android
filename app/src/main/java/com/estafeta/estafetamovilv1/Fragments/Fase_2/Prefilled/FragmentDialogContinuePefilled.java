package com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled;


import android.graphics.PorterDuff;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Button;

import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;

/**
 *
 */
public class FragmentDialogContinuePefilled extends DialogFragment implements View.OnClickListener{

    private TextView    txt_prefilled_code;

    private Button      btn_cancel_prefilled_code,
                        btn_continue;

    public listenerButtonContinue listener;

    public static final String TAG = "FragmentDialogContinuePefilled";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_continue_pefilled, container, false);
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        //TextViews
        txt_prefilled_code          = (EditText) view.findViewById(R.id.txt_prefilled_code);
        //Buttons
        btn_cancel_prefilled_code   = (Button) view.findViewById(R.id.btn_cancel);
        btn_continue                = (Button) view.findViewById(R.id.btn_continue);

        //Set Listeners
        btn_cancel_prefilled_code.setOnClickListener(this);
        btn_continue.setOnClickListener(this);

        txt_prefilled_code.getBackground().setColorFilter(getResources().getColor(R.color.estafeta_red), PorterDuff.Mode.SRC_ATOP);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_continue:
                if(validate()) {
                    listener.buttonContinuePressed(txt_prefilled_code.getText().toString());
                    dismiss();
                }

                break;
        }
    }

    private boolean validate(){
        if (txt_prefilled_code.getText().toString().equals("")) {
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.verifique), 3000);
            return false;
        }
        return true;
    }

    public interface listenerButtonContinue{
        void buttonContinuePressed(String code);
    }

}
