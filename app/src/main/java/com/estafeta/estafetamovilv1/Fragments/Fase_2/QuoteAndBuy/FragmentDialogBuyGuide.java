package com.estafeta.estafetamovilv1.Fragments.Fase_2.QuoteAndBuy;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.estafeta.estafetamovilv1.Fragments.FragmentDetailQuoatation;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;
import com.estafeta.estafetamovilv1.Utils.Utilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDialogBuyGuide extends DialogFragment implements View.OnClickListener{


    private Button      btn_cancel,
                        btn_continue;
    private TextView    lbl_question_previous_continue_buy;

    private String      total = "";

    public setPressedButtonContinue listener;

    public static final String TAG = "FragmentDialogGetDataFrequentlyContacts";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);

        listener = (FragmentDetailQuoatation) getActivity().getSupportFragmentManager().findFragmentByTag(FragmentDetailQuoatation.TAG);

        if(getArguments()!=null && getArguments().getSerializable("total") != null)
            total = getArguments().getString("total");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_buy_guide, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(view != null){

            btn_cancel                          = (Button) view.findViewById(R.id.btn_cancel);
            btn_continue                        = (Button) view.findViewById(R.id.btn_continue);

            btn_cancel.setOnClickListener(this);
            btn_continue.setOnClickListener(this);

            lbl_question_previous_continue_buy      = (TextView) view.findViewById(R.id.lbl_question_previous_continue_buy);

            try {
                Double totalNum = Double.parseDouble(total);
                totalNum = totalNum + (totalNum * 0.15);
                lbl_question_previous_continue_buy.setText(Html.fromHtml("El costo total más IVA resulta en la siguiente cantidad:" + " <b>" + Utilities.setReceiptMoneyNumberFormat(totalNum, 2) + " mxn</b>"));
            }catch (Exception e){
                e.printStackTrace();
            }


        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_continue:

                listener.pressedButtonContinue();
                dismiss();

                break;

        }
    }

    public interface setPressedButtonContinue{
        void pressedButtonContinue();
    }

}
