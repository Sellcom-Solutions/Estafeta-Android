package com.sellcom.apps.tracker_material.Fragments;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sellcom.apps.tracker_material.R;

/**
 * Created by anel 01/07/2015.
 */
public class FragmentDialogFavorite  extends DialogFragment implements View.OnClickListener {

    String TAG = "FRAG_DIALOG_FAVORITE";
    Button fav_call;
    Button fav_share;
    Button fav_closed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        //setCancelable(false);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_favorite, container, false);


        fav_call=(Button)view.findViewById(R.id.btn_fav_call);
        fav_share=(Button)view.findViewById(R.id.btn_fav_share);
        fav_closed=(Button)view.findViewById(R.id.btn_fav_closed);


        fav_call.setOnClickListener(this);
        fav_share.setOnClickListener(this);
        fav_closed.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fav_call:
                Log.d(TAG, "Action Call");
                String phoneNumber = "018003782338";
                onClickTelefono(phoneNumber);
                Log.d("Cancel", "" + getId());
                break;

            case R.id.btn_fav_share:
                Log.d("SHARE", "" + getId());
                getDialog().dismiss();
                break;

            case R.id.btn_fav_closed:
                Log.d("Closed","" + getId());
                getDialog().dismiss();
                break;
        }

    }

    public void onClickTelefono(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
