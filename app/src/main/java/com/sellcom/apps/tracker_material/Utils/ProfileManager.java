package com.sellcom.apps.tracker_material.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;

import com.sellcom.apps.tracker_material.NavigationDrawer.NavigationItem;
import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raymundo.piedra on 07/02/15.
 */
public class ProfileManager {

    public static List<NavigationItem> getDrawerModulesFromProfile(Activity context){

        List<NavigationItem> items = new ArrayList<NavigationItem>();

        String[] titlesArray    = context.getResources().getStringArray(R.array.drawer_items);

            for (int i=0; i<titlesArray.length-1; i++){
                /*if(i==2 || i==4){
                    //Nothing
                }else{*/
                    items.add(new NavigationItem(titlesArray[i]));
                //}

            }

        return items;
    }
}
