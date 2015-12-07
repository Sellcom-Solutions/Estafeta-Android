package com.estafeta.estafetamovilv1.Utils;

import android.app.Activity;

import com.estafeta.estafetamovilv1.NavigationDrawer.NavigationItem;
import com.estafeta.estafetamovilv1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raymundo.piedra on 07/02/15.
 * This class allows the Drawer highlight the currently selected item.
 */
public class ProfileManager {

    public static List<NavigationItem> getDrawerModulesFromProfile(Activity context){

        List<NavigationItem> items = new ArrayList<NavigationItem>();

        String[] titlesArray    = context.getResources().getStringArray(R.array.drawer_items);

            for (int i=0; i<titlesArray.length-3; i++){
                /*if(i==2 || i==4){
                    //Nothing
                }else{*/
                    items.add(new NavigationItem(titlesArray[i]));
                //}

            }

        return items;
    }
}
