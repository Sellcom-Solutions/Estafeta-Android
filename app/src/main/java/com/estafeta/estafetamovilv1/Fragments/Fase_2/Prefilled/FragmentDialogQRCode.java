package com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import com.estafeta.estafetamovilv1.Activities.MainActivity;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled.FragmentPrefilled.DATA_SENDER;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled.FragmentPrefilled.DATA_ADDRESSEE;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 */
public class FragmentDialogQRCode extends DialogFragment implements View.OnClickListener{


    private TextView    lbl_sender_name,
                        lbl_origin,
                        lbl_sender_zip_code,
                        lbl_addressee_name,
                        lbl_destiny,
                        lbl_addressee_zip_code;

    private Button      btn_save_as,
                        btn_share,
                        btn_close;
    private Bitmap      bitmap_qr;

    private ImageView   imgv_qr_code;

    public static final String TAG = "FragmentDialogQRCode";

    private Map<String,String> dataPrefilled;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);

        if(getArguments()!=null && getArguments().getSerializable("dataAddressee") != null)
            dataPrefilled = (Map<String, String>) getArguments().getSerializable("dataAddressee");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_qrcode, container, false);
        //TextViews
        lbl_sender_name         = (TextView) view.findViewById(R.id.lbl_sender_name);
        lbl_origin              = (TextView) view.findViewById(R.id.lbl_origin);
        lbl_sender_zip_code     = (TextView) view.findViewById(R.id.lbl_sender_zip_code);
        lbl_addressee_name      = (TextView) view.findViewById(R.id.lbl_addressee_name);
        lbl_destiny             = (TextView) view.findViewById(R.id.lbl_destiny);
        lbl_addressee_zip_code  = (TextView) view.findViewById(R.id.lbl_addressee_zip_code);
        //Buttons
        btn_save_as             = (Button) view.findViewById(R.id.btn_save_as);
        btn_share               = (Button) view.findViewById(R.id.btn_share);
        btn_close               = (Button) view.findViewById(R.id.btn_close);
        //ImageViews
        imgv_qr_code            = (ImageView) view.findViewById(R.id.imgv_qr_code);

        //Set Listeners
        btn_save_as.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        populateDialog();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save_as:
                if(bitmap_qr != null)
                    if(saveImageToInternalStorage(bitmap_qr)){
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SUCCESS, "QR guardado con exito.", 3000);
                    }else
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "Ocurrio un error al guardar la imagen.", 3000);
                else
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "Ocurrio un error al guardar la imagen.", 3000);

                break;
            case R.id.btn_share:
                Toast.makeText(getActivity(),"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_close:
                getActivity().onBackPressed();
                /*TrackerFragment fragment = (TrackerFragment) getActivity().getSupportFragmentManager().findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_PRELLENADO.toString());
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment, TrackerFragment.FRAGMENT_TAG.FRAG_PRELLENADO.toString()).commit();*/
                dismiss();
                break;
        }
    }




    public boolean saveImageToInternalStorage(Bitmap image) {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Estafeta_QR";
        //Toast.makeText(getActivity(),path,Toast.LENGTH_SHORT).show();
        File file1 = new File(path);
        if(!file1.exists())
            file1.mkdirs();

        OutputStream fOut = null;
        try {
            String imageName = "estafeta_qr_" + dataPrefilled.get(DATA_SENDER.ZIP_CODE_SENDER.toString())+"_"+ dataPrefilled.get(DATA_ADDRESSEE.ZIP_CODE_ADDRESSEE.toString()) + ".png";

            File file = new File(path+"/", imageName);

            fOut = new FileOutputStream(file);
            if (!image.compress(Bitmap.CompressFormat.PNG, 90, fOut)) {
                Log.e("Log", "error while saving bitmap " + path);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return true;
    }


    private void populateDialog(){

        lbl_sender_name.setText(""+dataPrefilled.get(DATA_SENDER.SENDER_NAME.toString().trim()));
        lbl_origin.setText(""+dataPrefilled.get(DATA_SENDER.STATE_SENDER.toString().trim())+", "+dataPrefilled.get(DATA_SENDER.CITY_SENDER.toString().trim()));
        lbl_sender_zip_code.setText(""+dataPrefilled.get(DATA_SENDER.ZIP_CODE_SENDER.toString().trim()));

        lbl_addressee_name.setText(""+dataPrefilled.get(DATA_ADDRESSEE.ADDRESSEE_NAME.toString().trim()));
        lbl_destiny.setText(""+dataPrefilled.get(DATA_ADDRESSEE.STATE_ADDRESSEE.toString().trim())+", "+dataPrefilled.get(DATA_ADDRESSEE.CITY_ADDRESSEE.toString().trim()));
        lbl_addressee_zip_code.setText(""+dataPrefilled.get(DATA_ADDRESSEE.ZIP_CODE_ADDRESSEE.toString().trim()));

        generateQRCode();

    }

    private void generateQRCode(){

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix matrix = writer.encode(
                    "La información de este código aun esta por definirse, saludos." , BarcodeFormat.QR_CODE, 400, 400
            );

            bitmap_qr = toBitmap(matrix);
            imgv_qr_code.setImageBitmap(bitmap_qr);
            // Now what??
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    /**
     * Writes the given Matrix on a new Bitmap object.
     * @param matrix the matrix to write.
     * @return the new {@link Bitmap}-object.
     */
    public static Bitmap toBitmap(BitMatrix matrix){
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
}
