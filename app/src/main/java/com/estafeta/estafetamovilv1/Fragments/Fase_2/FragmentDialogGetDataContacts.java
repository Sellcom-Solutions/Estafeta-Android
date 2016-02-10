package com.estafeta.estafetamovilv1.Fragments.Fase_2;


import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
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
public class FragmentDialogGetDataContacts extends DialogFragment implements View.OnClickListener{

    private Button      btn_cancel;

    private TextView    lbl_contacts,
                        lbl_frequently;

    public contactsOptionPressed listener;

    private long            mLastClickTime;

    public static final String TAG = "FragmentDialogGetDataContacts";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_get_data_contacts, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        //Buttons
        btn_cancel      = (Button) view.findViewById(R.id.btn_cancel);
        //TextViews
        lbl_contacts    = (TextView) view.findViewById(R.id.lbl_contacts);
        lbl_frequently  = (TextView) view.findViewById(R.id.lbl_frequently);

        //Set Listeners
        btn_cancel.setOnClickListener(this);
        lbl_contacts.setOnClickListener(this);
        lbl_frequently.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                dismiss();
                break;
            case R.id.lbl_contacts:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                listener.optionPressed(true);
                dismiss();
                break;
            case R.id.lbl_frequently:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                listener.optionPressed(false);
                dismiss();
                break;
        }
    }

    public interface contactsOptionPressed{
        void optionPressed(boolean isContacts);
    }
}
