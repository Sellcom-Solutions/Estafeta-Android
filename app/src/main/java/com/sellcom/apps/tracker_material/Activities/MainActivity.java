package com.sellcom.apps.tracker_material.Activities;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.nfc.Tag;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sellcom.apps.tracker_material.Async_Request.DecisionDialogWithListener;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;

import com.sellcom.apps.tracker_material.Fragments.FragmentAR;
import com.sellcom.apps.tracker_material.Fragments.FragmentAvisoPrivacidad;
import com.sellcom.apps.tracker_material.Fragments.FragmentCodigoPostal;
import com.sellcom.apps.tracker_material.Fragments.FragmentHistorial;
import com.sellcom.apps.tracker_material.Fragments.FragmentQuotation;
import com.sellcom.apps.tracker_material.Fragments.FragmentQuotationBuy;
import com.sellcom.apps.tracker_material.Fragments.FragmentQuotationBuyFields;
import com.sellcom.apps.tracker_material.Fragments.FragmentRastreo;
import com.sellcom.apps.tracker_material.Fragments.FragmentOffices;

import com.sellcom.apps.tracker_material.NavigationDrawer.NavigationDrawerCallbacks;
import com.sellcom.apps.tracker_material.NavigationDrawer.NavigationDrawerFragment;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.Utilities;


import java.util.Map;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks, UIResponseListenerInterface ,DecisionDialogWithListener {

    String ACT_TAG = "MainActivity";

    private Toolbar                     mToolbar;
    private CharSequence                mTitle;

    private NavigationDrawerFragment    mNavigationDrawerFragment;
    public boolean                      isDrawerOpen;
    private FragmentTransaction         fragmentTransaction;
    private FragmentManager             fragmentManager;
    private int                         position;
    private TrackerFragment             Fragment_Default,fragment;
            String                      CURRENT_FRAGMENT_TAG;
    public  int                         depthCounter   = 0;


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
        Log.d("MainActivity","Super prueba de comas"+Utilities.setReceiptMoneyNumberFormat(108271827.23322,2));

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void returnToHome(){
        mNavigationDrawerFragment.selectItem(0);
    }

    @Override
    public void onBackPressed() {

        Fragment currentFragment=getSupportFragmentManager().findFragmentById(R.id.container);

        if(currentFragment instanceof FragmentAR){
            ((FragmentAR)currentFragment).finalizar();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            getSupportActionBar().show();
            mNavigationDrawerFragment.selectItem(NavigationDrawerFragment.OFICINAS);
            //System.gc();
            return;
        }

        else if (currentFragment instanceof FragmentQuotationBuy){
            if ( ((FragmentQuotationBuy)currentFragment).getCurrent() == FragmentQuotationBuyFields.DESTINY)
                ((FragmentQuotationBuy)currentFragment).handleBackPressed();
            else
                super.onBackPressed();
            return;
        }

        Log.d(ACT_TAG,"Deep depthCounter Back 1:"+depthCounter);

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
            return;
        }

        if (depthCounter == 0) {
            /*
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment tracking   = fragmentManager.findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_RASTREO.toString());

            if(tracking != null && tracking.isAdded()){
                fragmentManager.beginTransaction().remove(tracking).commit();
                this.recreate();
            }*/

            RequestManager.sharedInstance().showDecisionDialogWithListener(getString(R.string.req_man_confirm_exit), this, this);

        } else {
            //Fragment home = getSupportFragmentManager().findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_RASTREO.toString());
            /*if(home != null && home.isAdded()){
                Log.d("---->","Is already added");
                moveTaskToBack(true);
            } else*/
                super.onBackPressed();
        }

        if (depthCounter > 0)
            depthCounter--;

        Log.d(ACT_TAG, "Deep depthCounter Back 2:" + depthCounter);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        fragmentManager = getSupportFragmentManager();

        if (fragmentManager == null)
            Log.d("MAIN ACTIVITY","Error fragment manager");

        fragmentTransaction         = fragmentManager.beginTransaction();
        this.position               = position;
        Fragment_Default = null;
        switch (position) {
            case NavigationDrawerFragment.RASTREO:
                CURRENT_FRAGMENT_TAG    = TrackerFragment.FRAGMENT_TAG.FRAG_RASTREO.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment            = (TrackerFragment)fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                    Fragment_Default    = new FragmentRastreo();
                }else
                    fragment = new FragmentRastreo();
            break;

            case NavigationDrawerFragment.OFICINAS:
                CURRENT_FRAGMENT_TAG    = TrackerFragment.FRAGMENT_TAG.FRAG_OFFICES.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment            = (TrackerFragment)fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else
                    fragment = new FragmentOffices();
                break;

            //Created by Jose Luis 26/05/2015
            case NavigationDrawerFragment.CODIGO_POSTAL://codigo postal //prellenado
                CURRENT_FRAGMENT_TAG    = TrackerFragment.FRAGMENT_TAG.FRAG_CODIGO_POSTAL.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment            = (TrackerFragment) fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else{
                    fragment = new FragmentCodigoPostal();
                }
                break;

            case NavigationDrawerFragment.AVISO_PRIVACIDAD://aviso de privasidad HISTORIA
                CURRENT_FRAGMENT_TAG    = TrackerFragment.FRAGMENT_TAG.FRAG_AVISO_PRIVACIDAD.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment            = (TrackerFragment) fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else{
                    fragment = new FragmentAvisoPrivacidad();
                }
                break;

            case NavigationDrawerFragment.COTIZADOR://cotizador
                CURRENT_FRAGMENT_TAG    = TrackerFragment.FRAGMENT_TAG.FRAG_QUOTATION.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment            = (TrackerFragment) fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else{
                    fragment = new FragmentQuotation();
                }
                break;

            /*case NavigationDrawerFragment.PRELLENADO://prellenado COTIZADOR
                fragment = null;
                break;

            case NavigationDrawerFragment.HISTORIAL://historial AVISO DE PRIVACIDAD
                CURRENT_FRAGMENT_TAG    = TrackerFragment.FRAGMENT_TAG.FRAG_HISTORIAL.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment            = (TrackerFragment) fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else{
                    fragment = new FragmentHistorial();
                }
                break;
*/
            default:
                //Toast.makeText(this,"Módulo no implementado",Toast.LENGTH_SHORT).show();
                return;
        }
        if (fragment != null)
            prepareTransaction();
        else
            Toast.makeText(this,"Modulo en desarrollo",Toast.LENGTH_SHORT).show();
    }

    public void prepareTransaction(){

        fragment.section_index  = position;
        fragment.tag            = CURRENT_FRAGMENT_TAG;
        /*
        if (position > 0) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.shrink_out, R.anim.slide_from_left, R.anim.shrink_out);
            depthCounter = 1;
        } else if (position == 0) {
            depthCounter = 0;
        }*/

        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.shrink_out, R.anim.slide_from_left, R.anim.shrink_out);
        depthCounter = 0;
/*
        if(fragmentManager.findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_RASTREO.toString()) != null)
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_RASTREO.toString()));
*/
        if(Fragment_Default != null)
            fragment = Fragment_Default;

        fragmentTransaction.replace(R.id.container, fragment, CURRENT_FRAGMENT_TAG).commit();

    }

    public void onSectionAttached(int number) {
        mTitle = getResources().getStringArray(R.array.drawer_items)[number];
    }

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        FragmentManager     fragmentManager     = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //this.prepareRequest(method, params, true);

    }

    @Override
    public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {
        RequestManager.sharedInstance().setListener(this);
        //RequestManager.sharedInstance().makeRequestWithDataAndMethodIncludeCredentials(params, method,includeCredentials);
    }

    @Override
    public void decodeResponse(String stringResponse) {

    }


    @Override
    public void responseFromDecisionDialog(String confirmMessage, String option) {
        if (option.equalsIgnoreCase("OK")){
            Utilities.flag = true;
            //android.os.Process.killProcess(android.os.Process.myPid());
            this.finish();
        }


    }

}