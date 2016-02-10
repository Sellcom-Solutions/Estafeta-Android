package com.estafeta.estafetamovilv1.Activities;

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
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.estafeta.estafetamovilv1.Fragments.Fase_2.Prefilled.FragmentPrefilled;
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


import java.util.Map;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks ,DecisionDialogWithListener, CommunicationBetweenFragments {

    public static final String          ACT_TAG     = "MainActivity";

    private Toolbar                     mToolbar;
    private CharSequence                mTitle;


    private NavigationDrawerFragment    mNavigationDrawerFragment;
    public boolean                      isDrawerOpen;
    private FragmentTransaction         fragmentTransaction;
    private FragmentManager             fragmentManager;
    private int                         position = -1;
    private TrackerFragment             fragment;
            String                      CURRENT_FRAGMENT_TAG;
    public  int                         depthCounter    = 0;

    private long            mLastClickTime;


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

        /*else if (currentFragment instanceof FragmentQuotationBuy){
            if ( ((FragmentQuotationBuy)currentFragment).getCurrent() == FragmentQuotationBuyFields.DESTINY)
                ((FragmentQuotationBuy)currentFragment).handleBackPressed();
            else
                super.onBackPressed();
            return;
        }*/

        Log.d(ACT_TAG, "Deep depthCounter Back 1:" + depthCounter);

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
            return;
        }

        if (depthCounter == 0) {

            moveTaskToBack(true);

        } else {
                super.onBackPressed();
        }

        if (depthCounter > 0)
            depthCounter--;

        Log.d(ACT_TAG, "Deep depthCounter Back 2:" + depthCounter);
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

            /*case NavigationDrawerFragment.HISTORIAL:

                fragment = null;

                break;*/
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
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        isDrawerOpen = mNavigationDrawerFragment.isDrawerOpen();
        if (!isDrawerOpen) {
            restoreActionBar();
        }
        return false;
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        FragmentManager     fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //this.prepareRequest(method, params, true);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

    }*/



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

    public NavigationDrawerFragment getmNavigationDrawerFragment() {
        return mNavigationDrawerFragment;
    }

    public void hideDrawer(boolean status){

        mNavigationDrawerFragment.setDrawerState(status);


    }
}