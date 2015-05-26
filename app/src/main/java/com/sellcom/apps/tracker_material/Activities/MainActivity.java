package com.sellcom.apps.tracker_material.Activities;

import android.content.res.Configuration;
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

import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;

import com.sellcom.apps.tracker_material.Fragments.FragmentCodigoPostal;
import com.sellcom.apps.tracker_material.Fragments.FragmentRastreo;
import com.sellcom.apps.tracker_material.Fragments.FragmentOffices;

import com.sellcom.apps.tracker_material.NavigationDrawer.NavigationDrawerCallbacks;
import com.sellcom.apps.tracker_material.NavigationDrawer.NavigationDrawerFragment;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;


import java.util.Map;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks, UIResponseListenerInterface {

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

        Log.d(ACT_TAG,"Creating view:"+depthCounter);

        RequestManager.sharedInstance().setActivity(this);

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
    }

    public void returnToHome(){
        mNavigationDrawerFragment.selectItem(0);
    }

    @Override
    public void onBackPressed() {

        Log.d(ACT_TAG,"Deep depthCounter Back 1:"+depthCounter);

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
            return;
        }

        if (depthCounter == 1) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment tracking   = fragmentManager.findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_RASTREO.toString());

            if(tracking != null && tracking.isAdded()){
                fragmentManager.beginTransaction().remove(tracking).commit();
                this.recreate();
            }

        } else {
            Fragment home = getSupportFragmentManager().findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_RASTREO.toString());
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
            case NavigationDrawerFragment.CODIGO_POSTAL:
                CURRENT_FRAGMENT_TAG    = TrackerFragment.FRAGMENT_TAG.FRAG_CODIGO_POSTAL.toString();
                if(fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG) != null){
                    fragment            = (TrackerFragment) fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
                }else{
                    fragment = new FragmentCodigoPostal();
                }
                break;

            default:
                Toast.makeText(this,"Módulo no implementado",Toast.LENGTH_SHORT).show();
                return;
        }
        prepareTransaction();
    }

    public void prepareTransaction(){
        fragment.section_index  = position;
        fragment.tag            = CURRENT_FRAGMENT_TAG;
        if (position > 0) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.shrink_out, R.anim.slide_from_left, R.anim.shrink_out);
            depthCounter = 1;
        } else if (position == 0) {
            depthCounter = 0;
        }

        if(fragmentManager.findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_RASTREO.toString()) != null)
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(TrackerFragment.FRAGMENT_TAG.FRAG_RASTREO.toString()));

        if(Fragment_Default != null)
            fragment = Fragment_Default;

        fragmentTransaction.replace(R.id.container, fragment, CURRENT_FRAGMENT_TAG).commit();
    }

    public void onSectionAttached(int number) {
        mTitle = getResources().getStringArray(R.array.drawer_items)[number];
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

    }

    @Override
    public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {
        RequestManager.sharedInstance().setListener(this);
        RequestManager.sharedInstance().makeRequestWithDataAndMethodIncludeCredentials(params, method,includeCredentials);
    }

    @Override
    public void decodeResponse(String stringResponse) {

    }


}