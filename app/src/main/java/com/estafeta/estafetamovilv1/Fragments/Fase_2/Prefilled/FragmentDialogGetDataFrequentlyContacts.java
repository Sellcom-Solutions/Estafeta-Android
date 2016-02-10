package com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.estafeta.estafetamovilv1.Fragments.Fase_2.FragmentFequentlyContacts;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.util.Map;

/**
 *
 */
public class FragmentDialogGetDataFrequentlyContacts extends DialogFragment implements View.OnClickListener{

    private Button      btn_cancel,
                        btn_continue;
    private TextView    lbl_question_previous_get_data;

    private String      contact = "";

    public setPressedButtonContinue listener;

    public static final String TAG = "FragmentDialogGetDataFrequentlyContacts";

    Map<String,String> dataContact;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);

        listener = (FragmentFequentlyContacts) getActivity().getSupportFragmentManager().findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_FRECUENTES.toString());

        if(getArguments()!=null && getArguments().getSerializable("dataContact") != null)
            dataContact = (Map<String, String>) getArguments().getSerializable("dataContact");

        contact = dataContact.get("name_contact");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_get_data_frequently_contacts, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(view != null) {

            btn_cancel                          = (Button) view.findViewById(R.id.btn_cancel);
            btn_continue                        = (Button) view.findViewById(R.id.btn_continue);

            btn_cancel.setOnClickListener(this);
            btn_continue.setOnClickListener(this);

            lbl_question_previous_get_data      = (TextView) view.findViewById(R.id.lbl_question_previous_get_data);

            lbl_question_previous_get_data.setText(Html.fromHtml(getString(R.string.get_data)+" <b>"+contact+"</b>?"));

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

                listener.pressedButtonContinue(dataContact);
                dismiss();

                break;

        }
    }

    public interface setPressedButtonContinue{
        void pressedButtonContinue(Map<String,String> dataContact);
    }
}
