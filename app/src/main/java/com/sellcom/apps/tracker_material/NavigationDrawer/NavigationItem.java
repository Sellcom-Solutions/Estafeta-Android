package com.sellcom.apps.tracker_material.NavigationDrawer;

import android.graphics.drawable.Drawable;

/**
 * Created by raymundo.piedra on 07/02/15.
 */
public class NavigationItem {
    private String      nav_item_text;

    public NavigationItem(String text) {
        nav_item_text   = text;
    }

    public String getText() {
        return nav_item_text;
    }

    public void setText(String text) {
        nav_item_text = text;
    }

}
