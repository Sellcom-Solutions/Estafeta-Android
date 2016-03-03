package com.estafeta.estafetamovilv1.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import android.widget.Button;

import com.estafeta.estafetamovilv1.Fragments.Fase_2.OperationHistory.FragmentOperationHistory;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled.FragmentDialogQRCode;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled.FragmentPrefilled;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled.FragmentPrefilledSender;
import com.estafeta.estafetamovilv1.Fragments.Fase_2.QuoteAndBuy.FragmentQuotationBuy;
import com.estafeta.estafetamovilv1.communication.CommunicationBetweenFragments;
import com.google.android.gms.analytics.HitBuilders;
import com.estafeta.estafetamovilv1.Async_Request.DecisionDialogWithListener;
import com.estafeta.estafetamovilv1.Async_Request.RequestManager;

import com.estafeta.estafetamovilv1.Fragments.FragmentAR;
import com.estafeta.estafetamovilv1.Fragments.FragmentAvisoPrivacidad;
import com.estafeta.estafetamovilv1.Fragments.FragmentCodigoPostal;
import com.estafeta.estafetamovilv1.Fragments.FragmentQuotation;
import com.estafeta.estafetamovilv1.Fragments.FragmentRastreo;
import com.estafeta.estafetamovilv1.Fragments.FragmentOffices;

import com.estafeta.estafetamovilv1.NavigationDrawer.NavigationDrawerCallbacks;
import com.estafeta.estafetamovilv1.NavigationDrawer.NavigationDrawerFragment;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Services.UpdateService;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.MyApp;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;
import com.estafeta.estafetamovilv1.Utils.Utilities;
import com.liveperson.mobile.android.LivePerson;


import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks ,DecisionDialogWithListener, CommunicationBetweenFragments {

    public static final String          ACT_TAG     = "MainActivity";

    private Toolbar                     mToolbar;
    private CharSequence                mTitle;

    public  Fragment                    auxFragment;

    private NavigationDrawerFragment    mNavigationDrawerFragment;
    public boolean                      isDrawerOpen;
    private FragmentTransaction         fragmentTransaction;
    private FragmentManager             fragmentManager;
    private int                         position = -1;
    private TrackerFragment             fragment;
            String                      CURRENT_FRAGMENT_TAG;
    public  int                         depthCounter    = 0;

    private long                        mLastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(ACT_TAG, "Creating view:" + depthCounter);

        RequestManager.sharedInstance().setActivity(this);
        DialogManager.sharedInstance().setActivity(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        mNavigationDrawerFragment.selectItem(0);

        auxFragment = null;

        //LivePerson.init(this);

    }

    @Override
    protected void onResume(){
        super.onResume();



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean alertUp = preferences.getBoolean("alertUp", false);


        if(alertUp) {
            Log.d("MainActivity", "update service is running");
        }else{
            /**
             * Servicio que consulta si hay actualizaciones en favoritos.
             */
            Intent intent = new Intent(getApplicationContext(),
                    UpdateService.class);
            startService(intent);
            Log.d("MainActivity", "update service init");
        }

    }

    /*It allows control the actions the 'back' button of the device*/
    @Override
    public void onBackPressed() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if(currentFragment instanceof FragmentAR){
            ((FragmentAR)currentFragment).finalizar();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            getSupportActionBar().show();
            super.onBackPressed();
            //mNavigationDrawerFragment.selectItem(NavigationDrawerFragment.OFICINAS);
            //System.gc();
            return;
        }

        currentFragment = getSupportFragmentManager().findFragmentByTag(FragmentDialogQRCode.TAG);


        Log.d(ACT_TAG, "Deep depthCounter Back 1:" + depthCounter);

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
            return;
        }

        if (depthCounter == 0) {


            logOut();

        } else {
                if(currentFragment instanceof FragmentDialogQRCode){ //Para que no se limpien los datos de FragmentPrefilledSender
                    auxFragment = new FragmentDialogQRCode();
                    Log.d(ACT_TAG, "-------------:" + depthCounter);
                    super.onBackPressed();
                    FragmentPrefilledSender fragment = (FragmentPrefilledSender)getSupportFragmentManager().findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_REMITENTE.toString());
                    fragment.clearAll();
                    auxFragment = null;
                }else{
                    super.onBackPressed();
                }
        }

        if (depthCounter > 0)
            depthCounter--;

        Log.d(ACT_TAG, "Deep depthCounter Back 2:" + depthCounter);
    }

    public void logOut() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setCancelable(false)
                .setMessage(getString(R.string.log_out))
                .setPositiveButton(getString(R.string.done),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                moveTaskToBack(true);
                                dialog.cancel();
                            }
                        }
                )
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }
                );

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setTextColor(getResources().getColor(R.color.estafeta_red));

        Button button2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        button2.setTextColor(getResources().getColor(R.color.estafeta_text));

    }

    /**
     * It allows control the actions of the Drawer item that was selected.
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {

        fragmentManager = getSupportFragmentManager();

        if (fragmentManager == null)
            Log.d("MAIN ACTIVITY","Error fragment manager");

        fragmentTransaction  = fragmentManager.beginTransaction();
        if(this.position == position){
            Log.d("MAIN ACTIVITY", "this.position == position");
            return;
        }

        invalidateOptionsMenu();
        this.position = position;
        switch (position) {
            case NavigationDrawerFragment.RASTREO:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //Google Analytics
                MyApp.tracker().send(new HitBuilders.EventBuilder("Menu", "Tap")
                        .setLabel("pantalla_rastreo")
                        .build());

                CURRENT_FRAGMENT_TAG = TrackerFragment.FRAGMENT_TAG.FRAG_RASTREO.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment = (TrackerFragment)fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else
                    fragment = new FragmentRastreo();
            break;

            case NavigationDrawerFragment.OFICINAS:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //Google Analytics
                MyApp.tracker().send(new HitBuilders.EventBuilder("Menu", "Tap")
                        .setLabel("pantalla_oficinas")
                        .build());

                CURRENT_FRAGMENT_TAG = TrackerFragment.FRAGMENT_TAG.FRAG_OFFICES.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment = (TrackerFragment)fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else
                    fragment = new FragmentOffices();

                break;

            case NavigationDrawerFragment.CODIGO_POSTAL:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //Google Analytics
                MyApp.tracker().send(new HitBuilders.EventBuilder("Menu", "Tap")
                        .setLabel("pantalla_cp")
                        .build());

                CURRENT_FRAGMENT_TAG = TrackerFragment.FRAGMENT_TAG.FRAG_CODIGO_POSTAL.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment = (TrackerFragment) fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else{
                    fragment = new FragmentCodigoPostal();
                }
                break;

            case NavigationDrawerFragment.AVISO_PRIVACIDAD:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //Google Analytics
                MyApp.tracker().send(new HitBuilders.EventBuilder("Menu", "Tap")
                        .setLabel("pantalla_cp")
                        .build());

                CURRENT_FRAGMENT_TAG = TrackerFragment.FRAGMENT_TAG.FRAG_AVISO_PRIVACIDAD.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment = (TrackerFragment) fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else{
                    fragment = new FragmentAvisoPrivacidad();
                }
                break;

            case NavigationDrawerFragment.COTIZADOR:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //Google Analytics
                MyApp.tracker().send(new HitBuilders.EventBuilder("Menu", "Tap")
                        .setLabel("pantalla_cotizador")
                        .build());

                CURRENT_FRAGMENT_TAG = TrackerFragment.FRAGMENT_TAG.FRAG_QUOTATION.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment = (TrackerFragment) fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else{
                    fragment = new FragmentQuotation();
                }
                break;

            case NavigationDrawerFragment.PRELLENADO:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                CURRENT_FRAGMENT_TAG = TrackerFragment.FRAGMENT_TAG.FRAG_PRELLENADO.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment = (TrackerFragment) fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else{
                    fragment = new FragmentPrefilled();
                }

                break;

            case NavigationDrawerFragment.HISTORIAL:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                CURRENT_FRAGMENT_TAG = TrackerFragment.FRAGMENT_TAG.FRAG_OPERATION_HISTORY.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment = (TrackerFragment) fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else{
                    fragment = new FragmentOperationHistory();
                }

                break;
        }

        if (fragment != null)
            prepareTransaction();
        else
            Toast.makeText(this,"Módulo en desarrollo",Toast.LENGTH_SHORT).show();
    }

    /**
     * Prepare switching between fragments.
     */
    public void prepareTransaction(){

        fragment.section_index = position;
        fragment.tag = CURRENT_FRAGMENT_TAG;

        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.shrink_out, R.anim.slide_from_left, R.anim.shrink_out);
        depthCounter = 0;


        fragmentTransaction.replace(R.id.container, fragment, CURRENT_FRAGMENT_TAG).commit();

    }

    /**
     * Names the ActionBar.
     * @param number It indicates the current position of the Drawer.
     */
    public void onSectionAttached(int number) {
        mTitle = getResources().getStringArray(R.array.drawer_items)[number];
    }

    /**
     * Increases the depth to enter the screens.
     */
    public void incrementDepthCounter(){
        depthCounter++;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        //Código para cambiar tamaño de letra en actionbar
        /*SpannableString spannableString = new SpannableString(mTitle);
        spannableString.setSpan(new RelativeSizeSpan(0.75f), 0, mTitle.length(), 0);
        actionBar.setTitle(spannableString);*/
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        isDrawerOpen = mNavigationDrawerFragment.isDrawerOpen();
        if (!isDrawerOpen) {
            restoreActionBar();
        }
        return false;
    }



    @Override
    public void responseFromDecisionDialog(String confirmMessage, String option) {
        if (option.equalsIgnoreCase("OK")){
            Utilities.flag = true;
            this.finish();
        }
    }

    @Override
    public void setDataSender(Map<String, String> dataSender) {

        FragmentPrefilled fragment = (FragmentPrefilled)fragmentManager.findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_PRELLENADO.toString());
        fragment.setDataSender(dataSender);

    }

    @Override
    public Map<String, String> getDataSender() {
        FragmentPrefilled fragment = (FragmentPrefilled)fragmentManager.findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_PRELLENADO.toString());
        return fragment.getDataSender();
    }

    @Override
    public void setDataSenderQuotation(Map<String, String> dataSender) {
        FragmentQuotationBuy fragment = (FragmentQuotationBuy)fragmentManager.findFragmentByTag(FragmentQuotationBuy.TAG);
        fragment.setDataSenderQuotation(dataSender);
    }

    @Override
    public Map<String, String> getDataSenderQuotation() {
        FragmentQuotationBuy fragment = (FragmentQuotationBuy)fragmentManager.findFragmentByTag(FragmentQuotationBuy.TAG);
        return fragment.getDataSenderQuotation();
    }

    @Override
    public void temporarilySaveDataPrefilledSender(List<String> listColony, List<String> listCity, List<String> listState) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_PRELLENADO.toString());
        ((FragmentPrefilled)fragment).temporarilySaveDataPrefilledSender(listColony,listCity,listState);
    }

    @Override
    public Bundle getDataPrefilledSender() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_PRELLENADO.toString());
        return ((FragmentPrefilled)fragment).getDataPrefilledSender();
    }

    @Override
    public void clearBundlePrefilledSender() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_PRELLENADO.toString());
        ((FragmentPrefilled)fragment).clearBundlePrefilledSender();
    }

    /*@Override
    public void showMenu(boolean visible) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_PRELLENADO.toString());
        ((FragmentPrefilled)fragment).showMenu(visible);
    }*/

    public NavigationDrawerFragment getmNavigationDrawerFragment() {
        return mNavigationDrawerFragment;
    }

    public void hideDrawer(boolean status){

        mNavigationDrawerFragment.setDrawerState(status);


    }
}