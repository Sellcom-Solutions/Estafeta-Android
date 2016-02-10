package com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.estafeta.estafetamovilv1.R;

/**
 *
 */
public class FragmentDialogSave extends DialogFragment implements View.OnClickListener{

    private Button  btn_cancel,
                    btn_save;

    private TextView lbl_question_previous_save;

    public listenerButtonSave listener;

    public static final String TAG = "FragmentDialogSave";

    public enum QUESTION {
        SENDER, ADDRESSEE
    }

    public QUESTION question;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_save, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        lbl_question_previous_save  = (TextView) view.findViewById(R.id.lbl_question_previous_save);

        btn_cancel                  = (Button) view.findViewById(R.id.btn_cancel);
        btn_save                    = (Button) view.findViewById(R.id.btn_save);

        btn_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        if(question == QUESTION.SENDER){
            lbl_question_previous_save.setText(getString(R.string.question_previous_save_sender));
        }else if(question == QUESTION.ADDRESSEE){
            lbl_question_previous_save.setText(getString(R.string.question_previous_save_addreessee));
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.btn_cancel:

                dismiss();

                break;

            case R.id.btn_save:

                listener.buttonSavePressed();
                dismiss();

                break;

        }
    }


    public interface listenerButtonSave{

        void buttonSavePressed();

    }
}
