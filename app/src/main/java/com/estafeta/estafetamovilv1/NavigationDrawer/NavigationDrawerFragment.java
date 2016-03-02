package com.estafeta.estafetamovilv1.NavigationDrawer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.ImageView;

import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.ProfileManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class NavigationDrawerFragment extends Fragment implements NavigationDrawerCallbacks {

    public static final int RASTREO             = 0;
    public static final int OFICINAS            = 1;
    public static final int PRELLENADO          = 2;
    public static final int COTIZADOR           = 3;
    public static final int HISTORIAL           = 4;
    public static final int CODIGO_POSTAL       = 5;
    public static final int AVISO_PRIVACIDAD    = 6;


    public static final int RECOMMENDATIONS = 2;
    public static final int BUDGETS         = 3;


    private NavigationDrawerCallbacks mCallbacks;
    private RecyclerView mDrawerList;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition    = -1;
    private TextView txv_version,footer;

    private ImageView img_estafeta = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerList = (RecyclerView) view.findViewById(R.id.drawerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDrawerList.setLayoutManager(layoutManager);
        mDrawerList.setHasFixedSize(true);

        txv_version= (TextView)view.findViewById(R.id.versionName);
        footer = (TextView)view.findViewById(R.id.footer);

        img_estafeta = (ImageView)view.findViewById(R.id.img_estafeta);
        img_estafeta.setImageDrawable(getResources().getDrawable(R.drawable.menu_estafeta));


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

        String currentYear = formatter.format(new Date());

        footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));

        final List<NavigationItem> navigationItems = ProfileManager.getDrawerModulesFromProfile(getActivity());
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(navigationItems);
        adapter.setNavigationDrawerCallbacks(this);
        mDrawerList.setAdapter(adapter);
        selectItem(mCurrentSelectedPosition);

        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            txv_version.setText("Version: "+info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //setRetainInstance(true);

        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(img_estafeta != null) {
            img_estafeta.setImageDrawable(getResources().getDrawable(R.drawable.menu_estafeta));
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        if(img_estafeta != null) {
            img_estafeta.setImageDrawable(getResources().getDrawable(R.drawable.menu_estafeta));
        }
        return mActionBarDrawerToggle;
    }

    public void setActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
        mActionBarDrawerToggle = actionBarDrawerToggle;
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        mFragmentContainerView  = getActivity().findViewById(fragmentId);
        mDrawerLayout           = drawerLayout;
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.white));

        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return;
                    getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(toolbar.getWindowToken(), 0);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                //Utilities.hideKe  yboard(getActivity(), new EditText(getActivity()));
                img_estafeta.setImageDrawable(getResources().getDrawable(R.drawable.menu_estafeta));
                getActivity().invalidateOptionsMenu();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        };

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
        Log.e("cerro: ", String.valueOf(mFragmentContainerView));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void selectItem(int position) {

        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }

        ((NavigationDrawerAdapter) mDrawerList.getAdapter()).selectPosition(position);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        boolean flag = false;
        if(isDrawerOpen()){
            flag = true;
        }

        mActionBarDrawerToggle.onConfigurationChanged(newConfig);

        ViewGroup viewGroup = (ViewGroup) getView();
        viewGroup.removeAllViewsInLayout();
        View view = onCreateView(getActivity().getLayoutInflater(), viewGroup, null);
        viewGroup.addView(view);

        if(flag) {
            if (mDrawerLayout != null) {
                openDrawer();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }

    public void setDrawerState(boolean isEnabled) {
        if ( isEnabled ) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mActionBarDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            mActionBarDrawerToggle.syncState();
            //getActivity().getActionBar().setHomeButtonEnabled(true);

        }
        else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mActionBarDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            mActionBarDrawerToggle.syncState();
            //getActivity().getActionBar().setHomeButtonEnabled(false);
        }
    }


}
