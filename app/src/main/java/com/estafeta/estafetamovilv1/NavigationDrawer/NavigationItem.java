package com.estafeta.estafetamovilv1.NavigationDrawer;

/**
 * Created by raymundo.piedra on 07/02/15.
 * DTO used in NavigationDrawerFragment.
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
